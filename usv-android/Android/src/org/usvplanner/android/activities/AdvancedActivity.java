package org.usvplanner.android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.usvplanner.android.R;

/**
 * This class implements and handles the various ui used for the drone
 * configuration.
 */
public class AdvancedActivity extends DrawerNavigationUI implements View.OnClickListener {

	/**
	 * Used as logging tag.
	 */
	private static final String TAG = AdvancedActivity.class.getSimpleName();

    TextView advance_params;
    TextView advance_checklist;
    TextView advance_imu_calibration;
    TextView advance_compass_calibration;
    TextView advance_rc_calibration;
    TextView advance_level_calibration;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_advanced);
        advance_params = findViewById(R.id.advance_params);
        advance_params.setOnClickListener(this);
        advance_checklist = findViewById(R.id.advance_checklist);
        advance_checklist.setOnClickListener(this);
        advance_imu_calibration = findViewById(R.id.advance_imu_calibration);
        advance_imu_calibration.setOnClickListener(this);
        advance_compass_calibration = findViewById(R.id.advance_compass_calibration);
        advance_compass_calibration.setOnClickListener(this);
        advance_rc_calibration = findViewById(R.id.advance_rc_calibration);
        advance_rc_calibration.setOnClickListener(this);
        advance_level_calibration = findViewById(R.id.advance_level_calibration);
        advance_level_calibration.setOnClickListener(this);
	}

    @Override
    protected int getToolbarId() {
        return R.id.actionbar_toolbar;
    }

    @Override
    protected int getNavigationDrawerMenuItemId() {
        return 0;
    }

    @Override
	public void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
        setIntent(intent);
	}

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.advance_params:
                Intent paramsIntent = new Intent(this, ConfigurationActivity.class).putExtra(ConfigurationActivity.EXTRA_CONFIG_SCREEN_ID, 1);
                startActivity(paramsIntent);
                break;
            case R.id.advance_checklist:
                Intent checklistIntent = new Intent(this, ConfigurationActivity.class).putExtra(ConfigurationActivity.EXTRA_CONFIG_SCREEN_ID, 2);
                startActivity(checklistIntent);
                break;
            case R.id.advance_imu_calibration:
                Intent imucalIntent = new Intent(this, ConfigurationActivity.class).putExtra(ConfigurationActivity.EXTRA_CONFIG_SCREEN_ID, 3);
                startActivity(imucalIntent);
                break;
            case R.id.advance_compass_calibration:
                Intent compcalIntent = new Intent(this, ConfigurationActivity.class).putExtra(ConfigurationActivity.EXTRA_CONFIG_SCREEN_ID, 4);
                startActivity(compcalIntent);
                break;
            case R.id.advance_rc_calibration:
                Intent rccalIntent = new Intent(this, ConfigurationActivity.class).putExtra(ConfigurationActivity.EXTRA_CONFIG_SCREEN_ID, 5);
                startActivity(rccalIntent);
                break;
            case R.id.advance_level_calibration:
                Intent levelcalIntent = new Intent(this, ConfigurationActivity.class).putExtra(ConfigurationActivity.EXTRA_CONFIG_SCREEN_ID, 6);
                startActivity(levelcalIntent);
                break;
        }
    }
}
