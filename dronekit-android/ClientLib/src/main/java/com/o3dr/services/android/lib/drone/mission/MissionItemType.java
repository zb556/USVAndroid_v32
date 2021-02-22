package com.o3dr.services.android.lib.drone.mission;

import android.os.Bundle;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

import com.o3dr.android.client.R;
import com.o3dr.services.android.lib.drone.mission.item.MissionItem;
import com.o3dr.services.android.lib.drone.mission.item.command.CameraTrigger;
import com.o3dr.services.android.lib.drone.mission.item.command.ChangeSpeed;
import com.o3dr.services.android.lib.drone.mission.item.command.DoJump;
import com.o3dr.services.android.lib.drone.mission.item.command.EpmGripper;
import com.o3dr.services.android.lib.drone.mission.item.command.ResetROI;
import com.o3dr.services.android.lib.drone.mission.item.command.ReturnToLaunch;
import com.o3dr.services.android.lib.drone.mission.item.command.SetRelay;
import com.o3dr.services.android.lib.drone.mission.item.command.SetServo;
import com.o3dr.services.android.lib.drone.mission.item.command.Takeoff;
import com.o3dr.services.android.lib.drone.mission.item.command.YawCondition;
import com.o3dr.services.android.lib.drone.mission.item.complex.SplineSurvey;
import com.o3dr.services.android.lib.drone.mission.item.complex.StructureScanner;
import com.o3dr.services.android.lib.drone.mission.item.complex.Survey;
import com.o3dr.services.android.lib.drone.mission.item.spatial.Circle;
import com.o3dr.services.android.lib.drone.mission.item.spatial.DoLandStart;
import com.o3dr.services.android.lib.drone.mission.item.spatial.Land;
import com.o3dr.services.android.lib.drone.mission.item.spatial.RegionOfInterest;
import com.o3dr.services.android.lib.drone.mission.item.spatial.SplineWaypoint;
import com.o3dr.services.android.lib.drone.mission.item.spatial.Waypoint;
import com.o3dr.services.android.lib.drone.property.Type;
import com.o3dr.services.android.lib.util.ParcelableUtils;

import java.util.Locale;

import static android.provider.Settings.Global.getString;

/**
 * /**
 * List of mission item types.
 */
public enum MissionItemType {

    WAYPOINT("Waypoint","任意航点","任意航线") {
        @Override
        public MissionItem getNewItem() {
            return new Waypoint();
        }

        @Override
        protected Parcelable.Creator<Waypoint> getMissionItemCreator() {
            return Waypoint.CREATOR;
        }
    },

    SPLINE_WAYPOINT("Spline Waypoint","曲线航点","曲线航线") {
        @Override
        public MissionItem getNewItem() {
            return new SplineWaypoint();
        }

        @Override
        protected Parcelable.Creator<SplineWaypoint> getMissionItemCreator() {
            return SplineWaypoint.CREATOR;
        }
    },

    TAKEOFF("Takeoff","起航","起航") {
        @Override
        public MissionItem getNewItem() {
            return new Takeoff();
        }

        @Override
        protected Parcelable.Creator<Takeoff> getMissionItemCreator() {
            return Takeoff.CREATOR;
        }
    },

    CHANGE_SPEED("Change Speed","改变速度","改变速度") {
        @Override
        public MissionItem getNewItem() {
            return new ChangeSpeed();
        }

        @Override
        protected Parcelable.Creator<ChangeSpeed> getMissionItemCreator() {
            return ChangeSpeed.CREATOR;
        }
    },

    CAMERA_TRIGGER("Camera Trigger","探测器快门","探测器快门") {
        @Override
        public MissionItem getNewItem() {
            return new CameraTrigger();
        }

        @Override
        protected Creator<CameraTrigger> getMissionItemCreator() {
            return CameraTrigger.CREATOR;
        }
    },

    EPM_GRIPPER("EPM Gripper","投放","投放") {
        @Override
        public MissionItem getNewItem() {
            return new EpmGripper();
        }

        @Override
        protected Creator<EpmGripper> getMissionItemCreator() {
            return EpmGripper.CREATOR;
        }

    },

    RETURN_TO_LAUNCH("Return to Launch","返回起始点","返回起始点") {
        @Override
        public MissionItem getNewItem() {
            return new ReturnToLaunch();
        }

        @Override
        protected Creator<ReturnToLaunch> getMissionItemCreator() {
            return ReturnToLaunch.CREATOR;
        }
    },

    LAND("Land","停泊","停泊") {
        @Override
        public MissionItem getNewItem() {
            return new Land();
        }

        @Override
        protected Creator<Land> getMissionItemCreator() {
            return Land.CREATOR;
        }
    },

    CIRCLE("Circle","绕圈航点","绕圈航点") {
        @Override
        public MissionItem getNewItem() {
            return new Circle();
        }

        @Override
        protected Creator<Circle> getMissionItemCreator() {
            return Circle.CREATOR;
        }
    },

    REGION_OF_INTEREST("Region of Interest","航行区域","航行区域") {
        @Override
        public MissionItem getNewItem() {
            return new RegionOfInterest();
        }

        @Override
        protected Creator<RegionOfInterest> getMissionItemCreator() {
            return RegionOfInterest.CREATOR;
        }
    },

    SURVEY("Survey","地图测绘","地图测绘") {
        @Override
        public MissionItem getNewItem() {
            return new Survey();
        }

        @Override
        protected Creator<Survey> getMissionItemCreator() {
            return Survey.CREATOR;
        }
    },

    STRUCTURE_SCANNER("Structure Scanner","构建模型","构建模型") {
        @Override
        public MissionItem getNewItem() {
            return new StructureScanner();
        }

        @Override
        protected Creator<StructureScanner> getMissionItemCreator() {
            return StructureScanner.CREATOR;
        }
    },

    SET_SERVO("Set Servo","设定舵机","设定舵机") {
        @Override
        public MissionItem getNewItem() {
            return new SetServo();
        }

        @Override
        protected Creator<SetServo> getMissionItemCreator() {
            return SetServo.CREATOR;
        }
    },

    YAW_CONDITION("Set Yaw","设定Yaw","设定Yaw") {
        @Override
        public MissionItem getNewItem() {
            return new YawCondition();
        }

        @Override
        protected Creator<YawCondition> getMissionItemCreator() {
            return YawCondition.CREATOR;
        }
    },

    SET_RELAY("Set Relay","触发继电器","触发继电器") {
        @Override
        public MissionItem getNewItem() {
            return new SetRelay();
        }

        @Override
        protected Creator<SetRelay> getMissionItemCreator() {
            return SetRelay.CREATOR;
        }
    },

    DO_LAND_START("Do Land start","开始着陆","开始着陆") {
        @Override
        public boolean isTypeSupported(Type vehicleType){
            return super.isTypeSupported(vehicleType) && vehicleType.getDroneType() == Type.TYPE_PLANE;
        }

        @Override
        public MissionItem getNewItem() {
            return new DoLandStart();
        }

        @Override
        protected Creator<DoLandStart> getMissionItemCreator() {
            return DoLandStart.CREATOR;
        }
    },

    SPLINE_SURVEY("Spline Survey","曲线测绘","曲线测绘") {
        @Override
        public MissionItem getNewItem() {
            return new SplineSurvey();
        }

        @Override
        protected Creator<SplineSurvey> getMissionItemCreator() {
            return SplineSurvey.CREATOR;
        }
    },

    DO_JUMP("Do Jump","航点跳转","航点跳转") {
        @Override
        public MissionItem getNewItem() {
            return new DoJump();
        }

        @Override
        protected Creator<DoJump> getMissionItemCreator() {
            return DoJump.CREATOR;
        }
    },

    RESET_ROI("Reset ROI","重设兴趣区域","重设兴趣区域") {
        @Override
        public MissionItem getNewItem() {
            return new ResetROI();
        }

        @Override
        protected Creator<ResetROI> getMissionItemCreator() {
            return ResetROI.CREATOR;
        }
    };

    private final static String EXTRA_MISSION_ITEM_TYPE = "extra_mission_item_type";
    private final static String EXTRA_MISSION_ITEM = "extra_mission_item";

    private final String label1;
    private final String label2;
    private final String label3;

    private MissionItemType(String label1,String label2,String label3) {
        this.label1 = label1;
        this.label2 = label2;
        this.label3 = label3;
    }

    public String getLabel1() {
        return this.label1;
    }

    public String getLabel2() {
        return this.label2;
    }

    public String getLabel3() {
        return this.label3;
    }

    public String toString(boolean wp) {
        if(wp){
            return getLabel2();
        }
        else {
            return getLabel3();
        }
    }

    public abstract MissionItem getNewItem();

    public final Bundle storeMissionItem(MissionItem item) {
        Bundle bundle = new Bundle(2);
        storeMissionItem(item, bundle);
        return bundle;
    }

    public void storeMissionItem(MissionItem missionItem, Bundle bundle) {
        bundle.putString(EXTRA_MISSION_ITEM_TYPE, name());
        bundle.putByteArray(EXTRA_MISSION_ITEM, ParcelableUtils.marshall(missionItem));
    }

    protected abstract <T extends MissionItem> Creator<T> getMissionItemCreator();

    public static <T extends MissionItem> T restoreMissionItemFromBundle(Bundle bundle) {
        if (bundle == null)
            return null;

        String typeName = bundle.getString(EXTRA_MISSION_ITEM_TYPE);
        byte[] marshalledItem = bundle.getByteArray(EXTRA_MISSION_ITEM);
        if (typeName == null || marshalledItem == null)
            return null;

        MissionItemType type = MissionItemType.valueOf(typeName);
        if (type == null)
            return null;

        T missionItem = ParcelableUtils.unmarshall(marshalledItem, type.<T>getMissionItemCreator());
        return missionItem;
    }

    /**
     * Indicates if the mission item is supported on the given vehicle type.
     * @param vehicleType Vehicle type to check against (i.e: plane, copter, rover...)
     * @return true the mission item is supported, false otherwise.
     */
    public boolean isTypeSupported(Type vehicleType){
        return vehicleType != null;
    }
}
