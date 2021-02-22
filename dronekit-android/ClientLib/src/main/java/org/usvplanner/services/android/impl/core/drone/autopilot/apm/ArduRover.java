package org.usvplanner.services.android.impl.core.drone.autopilot.apm;

import android.content.Context;
import android.os.Handler;

import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.enums.MAV_TYPE;

import org.usvplanner.services.android.impl.communication.model.DataLink;
import org.usvplanner.services.android.impl.core.MAVLink.WaypointManager;
import org.usvplanner.services.android.impl.core.drone.LogMessageListener;
import org.usvplanner.services.android.impl.core.firmware.FirmwareType;
import org.usvplanner.services.android.impl.core.model.AutopilotWarningParser;

/**
 * Created by Fredia Huya-Kouadio on 7/27/15.
 */
public class ArduRover extends ArduPilot {

    public ArduRover(String droneId, Context context, DataLink.DataLinkProvider<MAVLinkMessage> mavClient, Handler handler, AutopilotWarningParser warningParser, LogMessageListener logListener) {
        super(droneId, context, mavClient, handler, warningParser, logListener);
    }

    @Override
    public int getType(){
        return MAV_TYPE.MAV_TYPE_GROUND_ROVER;
    }

    @Override
    public void setType(int type){}

    @Override
    public FirmwareType getFirmwareType() {
        return FirmwareType.ARDU_ROVER;
    }

    @Override
    public void onBeginWaypointEvent(WaypointManager.WaypointEvent_Type wpEvent) {

    }

    @Override
    public void onWaypointEvent(WaypointManager.WaypointEvent_Type wpEvent, int index, int count) {
//        Log.d("EditorActivity","ArduRover WP:" + index + " Total:" + count);
        if(wpEvent == WaypointManager.WaypointEvent_Type.WP_UPLOAD) {
            setMissionUploadPro(index, count);
        }
    }

    @Override
    public void onEndWaypointEvent(WaypointManager.WaypointEvent_Type wpEvent) {

    }
}
