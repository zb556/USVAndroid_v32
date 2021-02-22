/* AUTO-GENERATED FILE.  DO NOT MODIFY.
 *
 * This class was automatically generated by the
 * java mavlink generator tool. It should not be modified by hand.
 */
         
// MESSAGE HIL_CONTROLS PACKING
package com.MAVLink.common;
import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Parser;
import com.MAVLink.usvmega.CRC;
import java.nio.ByteBuffer;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

/**
* Sent from autopilot to simulation. Hardware in the loop control outputs
*/
public class msg_hil_controls_test{

public static final int MAVLINK_MSG_ID_HIL_CONTROLS = 91;
public static final int MAVLINK_MSG_LENGTH = 42;
private static final long serialVersionUID = MAVLINK_MSG_ID_HIL_CONTROLS;

private Parser parser = new Parser();

public CRC generateCRC(byte[] packet){
    CRC crc = new CRC();
    for (int i = 1; i < packet.length - 2; i++) {
        crc.update_checksum(packet[i] & 0xFF);
    }
    crc.finish_checksum(MAVLINK_MSG_ID_HIL_CONTROLS);
    return crc;
}

public byte[] generateTestPacket(){
    ByteBuffer payload = ByteBuffer.allocate(6 + MAVLINK_MSG_LENGTH + 2);
    payload.put((byte)MAVLinkPacket.MAVLINK_STX); //stx
    payload.put((byte)MAVLINK_MSG_LENGTH); //len
    payload.put((byte)0); //seq
    payload.put((byte)255); //sysid
    payload.put((byte)190); //comp id
    payload.put((byte)MAVLINK_MSG_ID_HIL_CONTROLS); //msg id
    payload.putLong((long)93372036854775807L); //time_usec
    payload.putFloat((float)73.0); //roll_ailerons
    payload.putFloat((float)101.0); //pitch_elevator
    payload.putFloat((float)129.0); //yaw_rudder
    payload.putFloat((float)157.0); //throttle
    payload.putFloat((float)185.0); //aux1
    payload.putFloat((float)213.0); //aux2
    payload.putFloat((float)241.0); //aux3
    payload.putFloat((float)269.0); //aux4
    payload.put((byte)125); //mode
    payload.put((byte)192); //nav_mode
    
    CRC crc = generateCRC(payload.array());
    payload.put((byte)crc.getLSB());
    payload.put((byte)crc.getMSB());
    return payload.array();
}

@Test
public void test(){
    byte[] packet = generateTestPacket();
    for(int i = 0; i < packet.length - 1; i++){
        parser.mavlink_parse_char(packet[i] & 0xFF);
    }
    MAVLinkPacket m = parser.mavlink_parse_char(packet[packet.length - 1] & 0xFF);
    byte[] processedPacket = m.encodePacket();
    assertArrayEquals("msg_hil_controls", processedPacket, packet);
}
}
        