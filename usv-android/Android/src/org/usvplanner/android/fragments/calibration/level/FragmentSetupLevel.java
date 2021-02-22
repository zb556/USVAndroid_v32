package org.usvplanner.android.fragments.calibration.level;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.o3dr.android.client.Drone;
import com.o3dr.android.client.apis.CalibrationApi;
import com.o3dr.services.android.lib.drone.attribute.AttributeEvent;
import com.o3dr.services.android.lib.drone.attribute.AttributeEventExtra;
import com.o3dr.services.android.lib.drone.attribute.AttributeType;
import com.o3dr.services.android.lib.drone.property.State;
import com.o3dr.services.android.lib.model.SimpleCommandListener;

import org.usvplanner.android.R;
import org.usvplanner.android.fragments.helpers.ApiListenerFragment;
import org.usvplanner.android.notifications.TTSNotificationProvider;

public class FragmentSetupLevel extends ApiListenerFragment {

	private final static long TIMEOUT_MAX = 30000l; //ms
    private final static long UPDATE_TIMEOUT_PERIOD = 100l; //ms
    private static final String EXTRA_UPDATE_TIMESTAMP = "extra_update_timestamp";

    private static final IntentFilter intentFilter = new IntentFilter();
    static {
        intentFilter.addAction(AttributeEvent.CALIBRATION_LEVEL);
        intentFilter.addAction(AttributeEvent.CALIBRATION_LEVEL_TIMEOUT);
        intentFilter.addAction(AttributeEvent.STATE_CONNECTED);
        intentFilter.addAction(AttributeEvent.STATE_DISCONNECTED);
    }

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            switch (action) {
                case AttributeEvent.CALIBRATION_LEVEL: {
                    calibration_step = 2;
                    updateDescription(calibration_step);
                }
                case AttributeEvent.STATE_CONNECTED:
                    if (calibration_step == 0) {
                        //Reset the screen, and enable the calibration button
                        resetCalibration();
                        btnStep.setEnabled(true);
                    }
                    break;
                case AttributeEvent.STATE_DISCONNECTED:
                    //Reset the screen, and disable the calibration button
                    btnStep.setEnabled(false);
                    resetCalibration();
                    break;
                case AttributeEvent.CALIBRATION_LEVEL_TIMEOUT:
                    if (getDrone().isConnected()) {
                        String message = intent.getStringExtra(AttributeEventExtra.EXTRA_CALIBRATION_LEVEL_MESSAGE);
                        if (message != null)
                            relayInstructions(message);
                    }
                    break;
            }
        }
    };

	private int calibration_step = 0;
    private Button btnStep;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_setup_level_main, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

        btnStep = (Button) view.findViewById(R.id.buttonStep);
        btnStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processCalibrationStep(calibration_step);
            }
        });
	}

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
    }

    private void resetCalibration(){
        calibration_step = 0;
        updateDescription(calibration_step);
    }

    @Override
    public void onApiConnected() {
        Drone drone = getDrone();
        State droneState = drone.getAttribute(AttributeType.STATE);
        if (drone.isConnected()) {
            btnStep.setEnabled(true);
            if (!droneState.isCalibrating()) {
                resetCalibration();
            }
        } else {
            btnStep.setEnabled(false);
            resetCalibration();
        }

        getBroadcastManager().registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    public void onApiDisconnected() {
        getBroadcastManager().unregisterReceiver(broadcastReceiver);
    }

	private void processCalibrationStep(int step) {
	    switch (step) {
            case 0:
                startCalibration();
                updateDescription(calibration_step);
                break;
            case 1:
                updateDescription(calibration_step);
                break;
            case 2:
            case 3:
            case 4:
                calibration_step = 0;
                resetCalibration();
                updateDescription(calibration_step);
                break;
        }
	}

    public void updateDescription(int calibration_step) {
        if (btnStep != null) {
            if (calibration_step == 0)
                btnStep.setText("水平校准");
            else if (calibration_step == 1)
                btnStep.setText(R.string.button_setup_calibrating);
            else if(calibration_step == 2)
                btnStep.setText(R.string.button_setup_done);
            else if(calibration_step == 3)
                btnStep.setText(R.string.button_setup_fail);
            else if(calibration_step == 4)
                btnStep.setText(R.string.button_setup_have_done);
        }
    }

	private void startCalibration() {
        Drone dpApi = getDrone();
		if (dpApi.isConnected()) {
            CalibrationApi.getApi(dpApi).startLevelCalibration(new SimpleCommandListener(){
                @Override
                public void onError(int error){
                    //失败
                    calibration_step = 3;
                    updateDescription(calibration_step);
                    Toast.makeText(getActivity(), R.string.level_calibration_start_error, Toast.LENGTH_LONG).show();
                }

                @Override
                public void onSuccess() {
                    super.onSuccess();
                    //已经校准完成
                    calibration_step = 4;
                    updateDescription(calibration_step);
                }

                @Override
                public void onTimeout() {
                    super.onTimeout();
                    //实测带ack返回的会超时，但其实校准成功了
                    calibration_step = 2;
                    updateDescription(calibration_step);
                }
            });
		}
	}

    private void relayInstructions(String instructions){
        final Activity activity = getActivity();
        if(activity == null) return;

        final Context context = activity.getApplicationContext();

        getBroadcastManager()
                .sendBroadcast(new Intent(TTSNotificationProvider.ACTION_SPEAK_MESSAGE)
                        .putExtra(TTSNotificationProvider.EXTRA_MESSAGE_TO_SPEAK, instructions));

        Toast.makeText(context, instructions, Toast.LENGTH_LONG).show();
    }
}
