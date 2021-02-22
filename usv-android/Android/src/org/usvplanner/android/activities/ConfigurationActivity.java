package org.usvplanner.android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import org.usvplanner.android.R;
import org.usvplanner.android.fragments.ChecklistFragment;
import org.usvplanner.android.fragments.ParamsFragment;
import org.usvplanner.android.fragments.calibration.compass.FragmentSetupCompass;
import org.usvplanner.android.fragments.calibration.imu.FragmentSetupIMU;
import org.usvplanner.android.fragments.calibration.level.FragmentSetupLevel;
import org.usvplanner.android.fragments.calibration.rc.FragmentSetupRC;

/**
 * This class implements and handles the various ui used for the drone
 * configuration.
 */
public class ConfigurationActivity extends DrawerNavigationUI {

	/**
	 * Used as logging tag.
	 */
	private static final String TAG = ConfigurationActivity.class.getSimpleName();

	public static final String EXTRA_CONFIG_SCREEN_ID = ConfigurationActivity.class.getPackage()
			.getName() + ".EXTRA_CONFIG_SCREEN_ID";

    private int mConfigScreenId = 1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_configuration);

        if(savedInstanceState != null){
            mConfigScreenId = savedInstanceState.getInt(EXTRA_CONFIG_SCREEN_ID, mConfigScreenId);
        }

		handleIntent(getIntent());
	}

    @Override
    protected int getToolbarId() {
        return R.id.actionbar_toolbar;
    }

    @Override
    protected int getNavigationDrawerMenuItemId() {
        return mConfigScreenId;
    }

    @Override
	public void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
        setIntent(intent);
        handleIntent(intent);
	}

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putInt(EXTRA_CONFIG_SCREEN_ID, mConfigScreenId);
    }

	private void handleIntent(Intent intent) {
		final int configScreenId = intent.getIntExtra(EXTRA_CONFIG_SCREEN_ID, mConfigScreenId);
        final Fragment currentFragment = getCurrentFragment();
        if(currentFragment == null || getIdForFragment(currentFragment) != configScreenId){
            mConfigScreenId = configScreenId;
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.configuration_screen, getFragmentForId(configScreenId))
                    .commit();
        }
	}

    public Fragment getCurrentFragment(){
        return getSupportFragmentManager().findFragmentById(R.id.configuration_screen);
    }

    private Fragment getFragmentForId(int fragmentId){
        final Fragment fragment;
        switch(fragmentId){
            case 3:
                fragment = new FragmentSetupIMU();
                break;

            case 4:
                fragment = new FragmentSetupCompass();
                break;
            case 5:
                fragment = new FragmentSetupRC();
                break;
            case 6:
                fragment = new FragmentSetupLevel();
                break;
            case 2:
                fragment = new ChecklistFragment();
                break;

            case 1:
            default:
                fragment = new ParamsFragment();
                break;
        }

        return fragment;
    }

    private int getIdForFragment(Fragment fragment){
        if(fragment instanceof FragmentSetupIMU){
            return 3;
        }
        else if(fragment instanceof FragmentSetupCompass){
            return 4;
        }
        else if(fragment instanceof FragmentSetupRC){
            return 5;
        }
        else if(fragment instanceof ChecklistFragment){
            return  2;
        }
        else if(fragment instanceof ParamsFragment) {
            return 1;
        }
        else {
            return 6;
        }
    }
}
