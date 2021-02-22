package org.usvplanner.android.view.checklist.row;

import org.usvplanner.android.view.checklist.CheckListItem;

import android.view.View;

public interface ListRow_Interface {
	interface OnRowItemChangeListener {
		void onRowItemChanged(CheckListItem listItem);

		void onRowItemGetData(CheckListItem listItem, String sysTag);
	}

	View getView(View convertView);

	int getViewType();
}
