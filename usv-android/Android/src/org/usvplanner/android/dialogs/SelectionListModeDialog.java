package org.usvplanner.android.dialogs;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.o3dr.android.client.Drone;

import org.usvplanner.android.R;
import org.usvplanner.android.fragments.actionbar.FlightModeAdapter;
import org.usvplanner.android.fragments.actionbar.SelectionListAdapter;

/**
 * Created by Fredia Huya-Kouadio on 4/6/15.
 */
public class SelectionListModeDialog extends DialogFragment implements SelectionListAdapter.SelectionListener{

    Drone drone;
    ListView selectionsView;
    SelectionListAdapter adapter;

    public static SelectionListModeDialog newInstance(Drone drone){
        final SelectionListModeDialog modeDialog = new SelectionListModeDialog();
        modeDialog.drone = drone;
        return modeDialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.CustomDialogTheme);
        setCancelable(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.dialog_selection_list, container, false);
    }

    @Override
    public void onStart(){
        super.onStart();
        getDialog().setCanceledOnTouchOutside(true);
    }

    @Override
    public void onPause(){
        super.onPause();
        dismiss();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        selectionsView = view.findViewById(R.id.selection_list);
        adapter = new FlightModeAdapter(getContext(), drone);
        selectionsView.setAdapter(adapter);
        if(adapter != null) {
            selectionsView.setSelection(adapter.getSelection());
            adapter.setSelectionListener(this);
        }
    }

    @Override
    public void onSelection() {
        dismiss();
    }
}
