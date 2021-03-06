package org.usvplanner.android.proxy.mission.item.fragments;

import org.usvplanner.android.R;

import com.o3dr.services.android.lib.drone.mission.MissionItemType;

public class MissionRTLFragment extends MissionDetailFragment {

	@Override
	protected int getResource() {
		return R.layout.fragment_editor_detail_rtl;
	}

	@Override
    public void onApiConnected(){
        super.onApiConnected();
        typeSpinner.setSelection(commandAdapter.getPosition(MissionItemType.RETURN_TO_LAUNCH));
    }

}
