package org.usvplanner.android.fragments.calibration.rc;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.o3dr.android.client.Drone;
import com.o3dr.android.client.apis.VehicleApi;
import com.o3dr.services.android.lib.drone.attribute.AttributeEvent;
import com.o3dr.services.android.lib.drone.attribute.AttributeType;
import com.o3dr.services.android.lib.drone.property.Parameter;
import com.o3dr.services.android.lib.drone.property.Parameters;
import com.o3dr.services.android.lib.drone.property.RcIn;
import com.o3dr.services.android.lib.drone.property.State;

import org.usvplanner.android.R;
import org.usvplanner.android.fragments.helpers.ApiListenerFragment;

import java.util.ArrayList;
import java.util.List;

public class FragmentSetupRC extends ApiListenerFragment {

    private static final IntentFilter intentFilter = new IntentFilter();

    static {
        intentFilter.addAction(AttributeEvent.STATE_CONNECTED);
        intentFilter.addAction(AttributeEvent.STATE_DISCONNECTED);
        intentFilter.addAction(AttributeEvent.RC_IN_UPDATE);
    }

    protected Drone drone;

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            switch (action) {
                case AttributeEvent.STATE_CONNECTED:
                    //使能校准按钮
                    calibrateBtn.setEnabled(true);
                    break;
                case AttributeEvent.STATE_DISCONNECTED:
                    //禁用校准按钮，复位校准
                    calibrateBtn.setEnabled(false);
                    if(step == 0) {
                        resetCalibration();
                    }
                    break;
                case AttributeEvent.RC_IN_UPDATE:
                    //使能校准按钮
                    calibrateBtn.setEnabled(true);
                    if (drone != null && drone.isConnected()) {
                        final RcIn rcIn = drone.getAttribute(AttributeType.RCIN);
                        //获取RC值
                        chroll = rcIn.getChRoll();
                        chpitch = rcIn.getChPitch();
                        chthro = rcIn.getChthro();
                        chyaw = rcIn.getChyaw();
                        ch5 = rcIn.getCh5();
                        ch6 = rcIn.getCh6();
                        ch7 = rcIn.getCh7();
                        ch8 = rcIn.getCh8();
                        ch9 = rcIn.getCh9();
                        ch10 = rcIn.getCh10();
                        ch11 = rcIn.getCh11();
                        ch12 = rcIn.getCh12();
                        //设置进度条显示
                        rollBar.setProgress(chroll);
                        pitchBar.setProgress(chpitch);
                        throBar.setProgress(chthro);
                        yawBar.setProgress(chyaw);
                        ch5Bar.setProgress(ch5);
                        ch6Bar.setProgress(ch6);
                        ch7Bar.setProgress(ch7);
                        ch8Bar.setProgress(ch8);
                        ch9Bar.setProgress(ch9);
                        ch10Bar.setProgress(ch10);
                        ch11Bar.setProgress(ch11);
                        ch12Bar.setProgress(ch12);
                        //校准第二阶段
                        //获取最大最小值
                        if (run && step == 2) {
                            int cnt = 0;
                            if (rcmax[cnt] < chroll) {
                                rcmax[cnt] = chroll;
//                                rollBar.setMax(rcmax[cnt]);
                            }
                            if (rcmin[cnt] > chroll) {
                                rcmin[cnt] = chroll;
//                                rollBar.setMin(rcmin[cnt]);
                            }
                            cnt++;
                            if (rcmax[cnt] < chpitch) {
                                rcmax[cnt] = chpitch;
                            }
                            if (rcmin[cnt] > chpitch) {
                                rcmin[cnt] = chpitch;
                            }
                            cnt++;
                            if (rcmax[cnt] < chthro) {
                                rcmax[cnt] = chthro;
                            }
                            if (rcmin[cnt] > chthro) {
                                rcmin[cnt] = chthro;
                            }
                            cnt++;
                            if (rcmax[cnt] < chyaw) {
                                rcmax[cnt] = chyaw;
                            }
                            if (rcmin[cnt] > chyaw) {
                                rcmin[cnt] = chyaw;
                            }
                            cnt++;
                            if (rcmax[cnt] < ch5) {
                                rcmax[cnt] = ch5;
                            }
                            if (rcmin[cnt] > ch5) {
                                rcmin[cnt] = ch5;
                            }
                            cnt++;
                            if (rcmax[cnt] < ch6) {
                                rcmax[cnt] = ch6;
                            }
                            if (rcmin[cnt] > ch6) {
                                rcmin[cnt] = ch6;
                            }
                            cnt++;
                            if (rcmax[cnt] < ch7) {
                                rcmax[cnt] = ch7;
                            }
                            if (rcmin[cnt] > ch7) {
                                rcmin[cnt] = ch7;
                            }
                            cnt++;
                            if (rcmax[cnt] < ch8) {
                                rcmax[cnt] = ch8;
                            }
                            if (rcmin[cnt] > ch8) {
                                rcmin[cnt] = ch8;
                            }
                            cnt++;
                            if (rcmax[cnt] < ch9) {
                                rcmax[cnt] = ch9;
                            }
                            if (rcmin[cnt] > ch9) {
                                rcmin[cnt] = ch9;
                            }
                            cnt++;
                            if (rcmax[cnt] < ch10) {
                                rcmax[cnt] = ch10;
                            }
                            if (rcmin[cnt] > ch10) {
                                rcmin[cnt] = ch10;
                            }
                            cnt++;
                            if (rcmax[cnt] < ch11) {
                                rcmax[cnt] = ch11;
                            }
                            if (rcmin[cnt] > ch11) {
                                rcmin[cnt] = ch11;
                            }
                            cnt++;
                            if (rcmax[cnt] < ch12) {
                                rcmax[cnt] = ch12;
                            }
                            if (rcmin[cnt] > ch12) {
                                rcmin[cnt] = ch12;
                            }
                            roll_pitch_text.setText("Roll: " + chpitch + " \nMin:" + rcmin[0] + " Max:" + rcmax[0] + "\nPitch: " + chroll + " \nMin:" + rcmin[1] + " Max:" + rcmax[1]);
                            thr_yaw_text.setText("Throttle: " + chthro + " \nMin:" + rcmin[2] + " Max:" + rcmax[2] + "\nYaw: " + chyaw + " \nMin:" + rcmin[3] + " Max:" + rcmax[3]);
                            ch_5_text.setText("Ch 5: " + ch5 + "\nMin:" + rcmin[4] + " Max:" + rcmax[4]);
                            ch_6_text.setText("Ch 6: " + ch6 + "\nMin:" + rcmin[5] + " Max:" + rcmax[5]);
                            ch_7_text.setText("Ch 7: " + ch7 + "\nMin:" + rcmin[6] + " Max:" + rcmax[6]);
                            ch_8_text.setText("Ch 8: " + ch8 + "\nMin:" + rcmin[7] + " Max:" + rcmax[7]);
                            ch_9_text.setText("Ch 9: " + ch9 + "\nMin:" + rcmin[8] + " Max:" + rcmax[8]);
                            ch_10_text.setText("Ch 10: " + ch10 + "\nMin:" + rcmin[9] + " Max:" + rcmax[9]);
                            ch_11_text.setText("Ch 11: " + ch11 + "\nMin:" + rcmin[10] + " Max:" + rcmax[10]);
                            ch_12_text.setText("Ch 12: " + ch12 + "\nMin:" + rcmin[11] + " Max:" + rcmax[11]);
                        } else {
                            roll_pitch_text.setText("Roll: " + chpitch + "\nPitch: " + chroll);
                            thr_yaw_text.setText("Throttle: " + chthro + "\nYaw: " + chyaw);
                            ch_5_text.setText("Ch 5: " + ch5);
                            ch_6_text.setText("Ch 6: " + ch6);
                            ch_7_text.setText("Ch 7: " + ch7);
                            ch_8_text.setText("Ch 8: " + ch8);
                            ch_9_text.setText("Ch 9: " + ch9);
                            ch_10_text.setText("Ch 10: " + ch10);
                            ch_11_text.setText("Ch 11: " + ch11);
                            ch_12_text.setText("Ch 12: " + ch12);
                        }
                    }
                    break;
            }
        }
    };

    private Button calibrateBtn;
    private TextView thr_yaw_text;
    private TextView roll_pitch_text;
    private TextView ch_5_text;
    private TextView ch_6_text;
    private TextView ch_7_text;
    private TextView ch_8_text;
    private TextView ch_9_text;
    private TextView ch_10_text;
    private TextView ch_11_text;
    private TextView ch_12_text;
    ProgressBar pitchBar;
    ProgressBar rollBar;
    ProgressBar throBar;
    ProgressBar yawBar;
    ProgressBar ch5Bar;
    ProgressBar ch6Bar;
    ProgressBar ch7Bar;
    ProgressBar ch8Bar;
    ProgressBar ch9Bar;
    ProgressBar ch10Bar;
    ProgressBar ch11Bar;
    ProgressBar ch12Bar;

    private int[] rcmax = new int[16];
    private int[] rcmin = new int[16];
    private int[] rctrim = new int[16];
    private int chpitch = 0;
    private int chroll = 0;
    private int chthro = 0;
    private int chyaw = 0;
    private int ch5 = 0;
    private int ch6 = 0;
    private int ch7 = 0;
    private int ch8 = 0;
    private int ch9 = 0;
    private int ch10 = 0;
    private int ch11 = 0;
    private int ch12 = 0;

    private boolean run;
    private int step = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_setup_rc_main, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        thr_yaw_text = (TextView) view.findViewById(R.id.thr_yaw_text);
        roll_pitch_text = (TextView) view.findViewById(R.id.roll_pitch_text);
        ch_5_text = view.findViewById(R.id.ch_5_text);
        ch_6_text = view.findViewById(R.id.ch_6_text);
        ch_7_text = view.findViewById(R.id.ch_7_text);
        ch_8_text = view.findViewById(R.id.ch_8_text);
        ch_9_text = view.findViewById(R.id.ch_9_text);
        ch_10_text = view.findViewById(R.id.ch_10_text);
        ch_11_text = view.findViewById(R.id.ch_11_text);
        ch_12_text = view.findViewById(R.id.ch_12_text);

        pitchBar = view.findViewById(R.id.fillBar_pitch);
        rollBar = view.findViewById(R.id.fillBar_roll);
        throBar = view.findViewById(R.id.fillBar_throttle);
        yawBar = view.findViewById(R.id.fillBar_yaw);
        ch5Bar = view.findViewById(R.id.fillBar_ch_5);
        ch6Bar = view.findViewById(R.id.fillBar_ch_6);
        ch7Bar = view.findViewById(R.id.fillBar_ch_7);
        ch8Bar = view.findViewById(R.id.fillBar_ch_8);
        ch9Bar = view.findViewById(R.id.fillBar_ch_9);
        ch10Bar = view.findViewById(R.id.fillBar_ch_10);
        ch11Bar = view.findViewById(R.id.fillBar_ch_11);
        ch12Bar = view.findViewById(R.id.fillBar_ch_12);

        calibrateBtn = (Button) view.findViewById(R.id.calibrateBtn);
        calibrateBtn.setEnabled(false);
        calibrateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processCalibration();
            }
        });

        for (int n = 0; n < rcmin.length; n++) {
            rcmin[n] = 3000;
            rcmax[n] = 0;
            rctrim[n] = 1500;
        }
    }

    private void resetCalibration() {
        run = false;
        step = 0;
        for (int n = 0; n < rcmin.length; n++) {
            rcmin[n] = 3000;
            rcmax[n] = 0;
            rctrim[n] = 1500;
        }
//        updateDescription(calibration_step);
    }

    @Override
    public void onApiConnected() {
        drone = getDrone();
        State droneState = drone.getAttribute(AttributeType.STATE);
        if (drone.isConnected()) {
            calibrateBtn.setEnabled(true);
            if (droneState.isCalibrating()) {
            } else {
                resetCalibration();
            }
        } else {
            calibrateBtn.setEnabled(false);
            resetCalibration();
        }

        getBroadcastManager().registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    public void onApiDisconnected() {
        getBroadcastManager().unregisterReceiver(broadcastReceiver);
    }

    private void processCalibration() {
        switch (step) {
            case 0:
                if (!run) {
                    run = true;
                    step = 1;
                    calibrateBtn.setText("归位摇杆");
                }
                break;
            case 1:
                //校准第一阶段
                //获取默认数值
                //校准第二阶段
                int cnt = 0;
                rctrim[cnt++] = chroll;
                rctrim[cnt++] = chpitch;
                rctrim[cnt++] = chthro;
                rctrim[cnt++] = chyaw;
                rctrim[cnt++] = ch5;
                rctrim[cnt++] = ch6;
                rctrim[cnt++] = ch7;
                rctrim[cnt++] = ch8;
                rctrim[cnt++] = ch9;
                rctrim[cnt++] = ch10;
                rctrim[cnt++] = ch11;
                rctrim[cnt++] = ch12;
                calibrateBtn.setText("推动摇杆");
                step = 2;
                break;
            case 2:
                //校准完成
                if (drone != null && drone.isConnected()) {
                    int type = 4;
                    final Parameters droneParams = getDrone().getAttribute(AttributeType.PARAMETERS);
                    if (droneParams != null) {
                        List<Parameter> parametersList = droneParams.getParameters();
                        for (int i = 0; i < parametersList.size(); i++) {
                            if (parametersList.get(i).getName().equals("RC1_MAX")) {
                                //获取type
                                type = parametersList.get(i).getType();
                                List<Parameter> changeParameterList = new ArrayList<Parameter>();
                                //写入参数
                                for (int j = 0; j < rcmax.length; j++) {
                                    if(rcmax[j] != rcmin[j]) {
                                        Parameter maxparameter = new Parameter("RC" + (j + 1) + "_MAX", rcmax[j], type);
                                        changeParameterList.add(maxparameter);
                                        Parameter minparameter = new Parameter("RC" + (j + 1) + "_MIN", rcmin[j], type);
                                        changeParameterList.add(minparameter);
                                        if(rctrim[j] < 1195 || rctrim[j] > 1205) {
                                            Parameter trimparameter = new Parameter("RC" + (j + 1) + "_TRIM", rctrim[j], type);
                                            changeParameterList.add(trimparameter);
                                        }
                                    }
                                }
                                VehicleApi.getApi(drone).writeParameters(new Parameters(changeParameterList));
                            }
                        }
                    }
                }
                run = false;
                step = 0;
                calibrateBtn.setText("校准完成");
                break;
        }
    }
}
