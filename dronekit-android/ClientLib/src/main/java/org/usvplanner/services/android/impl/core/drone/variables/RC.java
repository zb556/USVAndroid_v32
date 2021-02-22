package org.usvplanner.services.android.impl.core.drone.variables;

import com.MAVLink.common.msg_rc_channels;
import com.MAVLink.common.msg_rc_channels_raw;
import com.MAVLink.common.msg_servo_output_raw;

import org.usvplanner.services.android.impl.core.drone.DroneInterfaces.DroneEventsType;
import org.usvplanner.services.android.impl.core.drone.DroneVariable;
import org.usvplanner.services.android.impl.core.drone.autopilot.MavLinkDrone;

public class RC extends DroneVariable {
	public int in[] = new int[16];
	public int out[] = new int[8];

	public RC(MavLinkDrone myDrone) {
		super(myDrone);
	}

	public void setRcInputValues(msg_rc_channels_raw msg) {
		in[0] = msg.chan1_raw;
		in[1] = msg.chan2_raw;
		in[2] = msg.chan3_raw;
		in[3] = msg.chan4_raw;
		in[4] = msg.chan5_raw;
		in[5] = msg.chan6_raw;
		in[6] = msg.chan7_raw;
		in[7] = msg.chan8_raw;
//		myDrone.notifyDroneEvent(DroneEventsType.RC_IN);
	}

	public void setRcChannelsInputValues(msg_rc_channels msg) {
		in[0] = msg.chan1_raw;
		in[1] = msg.chan2_raw;
		in[2] = msg.chan3_raw;
		in[3] = msg.chan4_raw;
		in[4] = msg.chan5_raw;
		in[5] = msg.chan6_raw;
		in[6] = msg.chan7_raw;
		in[7] = msg.chan8_raw;
		in[8] = msg.chan9_raw;
		in[9] = msg.chan10_raw;
		in[10] = msg.chan11_raw;
		in[11] = msg.chan12_raw;
		in[12] = msg.chan13_raw;
		in[13] = msg.chan14_raw;
		in[14] = msg.chan15_raw;
		in[15] = msg.chan16_raw;
//		myDrone.notifyDroneEvent(DroneEventsType.RC_IN);
	}

	public int[] getRcInputValues() {
		return in;
	}

	public void setRcOutputValues(msg_servo_output_raw msg) {
		out[0] = msg.servo1_raw;
		out[1] = msg.servo2_raw;
		out[2] = msg.servo3_raw;
		out[3] = msg.servo4_raw;
		out[4] = msg.servo5_raw;
		out[5] = msg.servo6_raw;
		out[6] = msg.servo7_raw;
		out[7] = msg.servo8_raw;
		myDrone.notifyDroneEvent(DroneEventsType.RC_OUT);
	}
}
