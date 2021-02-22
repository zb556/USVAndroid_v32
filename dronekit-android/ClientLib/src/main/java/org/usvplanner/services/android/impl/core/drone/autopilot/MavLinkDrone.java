package org.usvplanner.services.android.impl.core.drone.autopilot;

import com.MAVLink.Messages.MAVLinkMessage;

import org.usvplanner.services.android.impl.communication.model.DataLink;
import org.usvplanner.services.android.impl.core.MAVLink.WaypointManager;
import org.usvplanner.services.android.impl.core.drone.profiles.ParameterManager;
import org.usvplanner.services.android.impl.core.drone.variables.Camera;
import org.usvplanner.services.android.impl.core.drone.variables.GuidedPoint;
import org.usvplanner.services.android.impl.core.drone.variables.MissionStats;
import org.usvplanner.services.android.impl.core.drone.variables.State;
import org.usvplanner.services.android.impl.core.drone.variables.StreamRates;
import org.usvplanner.services.android.impl.core.drone.variables.calibration.AccelCalibration;
import org.usvplanner.services.android.impl.core.drone.variables.calibration.LevelCalibration;
import org.usvplanner.services.android.impl.core.drone.variables.calibration.MagnetometerCalibrationImpl;
import org.usvplanner.services.android.impl.core.firmware.FirmwareType;
import org.usvplanner.services.android.impl.core.mission.MissionImpl;

public interface MavLinkDrone extends Drone {

    String PACKAGE_NAME = "org.droidplanner.services.android.core.drone.autopilot";

    String ACTION_REQUEST_HOME_UPDATE = PACKAGE_NAME + ".action.REQUEST_HOME_UPDATE";

    boolean isConnectionAlive();

    int getMavlinkVersion();

    void onMavLinkMessageReceived(MAVLinkMessage message);

    public short getSysid();

    public short getCompid();

    public State getState();

    public ParameterManager getParameterManager();

    public int getType();

    public FirmwareType getFirmwareType();

    public DataLink.DataLinkProvider<MAVLinkMessage> getMavClient();

    public WaypointManager getWaypointManager();

    public MissionImpl getMission();

    public StreamRates getStreamRates();

    public MissionStats getMissionStats();

    public GuidedPoint getGuidedPoint();

    public AccelCalibration getCalibrationSetup();

    public LevelCalibration getLevelCalibration();
    public MagnetometerCalibrationImpl getMagnetometerCalibration();

    public String getFirmwareVersion();

    public Camera getCamera();

}
