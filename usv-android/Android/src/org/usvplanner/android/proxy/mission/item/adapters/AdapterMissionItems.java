package org.usvplanner.android.proxy.mission.item.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.o3dr.services.android.lib.drone.mission.MissionItemType;


public class AdapterMissionItems extends ArrayAdapter<MissionItemType> {

	boolean wp;
	public AdapterMissionItems(Context context, int resource, MissionItemType[] objects, boolean wp) {
		super(context, resource, objects);
		this.wp = wp;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = super.getView(position, convertView, parent);
		((TextView) view).setText(getItem(position).toString(wp));
		return view;
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		return getView(position, convertView, parent);
	}

}