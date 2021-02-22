package org.usvplanner.android.activities.interfaces;

import com.o3dr.services.android.lib.coordinate.LatLong;

import org.usvplanner.android.proxy.mission.item.MissionItemProxy;

public interface OnEditorInteraction {
	void onItemClick(MissionItemProxy item, boolean zoomToFit);

	void onMapClick(LatLong coord);

	void onListVisibilityChanged();
}
