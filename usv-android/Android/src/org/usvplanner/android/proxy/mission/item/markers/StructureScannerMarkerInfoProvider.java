package org.usvplanner.android.proxy.mission.item.markers;

import org.usvplanner.android.R;
import org.usvplanner.android.proxy.mission.item.MissionItemProxy;

/**
 *
 */
public class StructureScannerMarkerInfoProvider extends MissionItemMarkerInfo {

	protected StructureScannerMarkerInfoProvider(MissionItemProxy origin) {
		super(origin);
	}

	@Override
	protected int getSelectedIconResource() {
		return R.drawable.ic_wp_loiter_selected;
	}

	@Override
	protected int getIconResource() {
		return R.drawable.ic_wp_circle_ccw;
	}
}
