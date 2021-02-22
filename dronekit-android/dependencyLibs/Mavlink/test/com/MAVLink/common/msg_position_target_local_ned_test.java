/* AUTO-GENERATED FILE.  DO NOT MODIFY.
 *
 * This class was automatically generated by the
 * java mavlink generator tool. It should not be modified by hand.
 */
         
// MESSAGE POSITION_TARGET_LOCAL_NED PACKING
package com.MAVLink.common;
import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Parser;
import com.MAVLink.usvmega.CRC;
import java.nio.ByteBuffer;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

/**
* Set vehicle position, velocity and acceleration setpoint in local frame.
*/
public class msg_position_target_local_ned_test{

public static final int MAVLINK_MSG_ID_POSITION_TARGET_LOCAL_NED = 85;
public static final int MAVLINK_MSG_LENGTH = 51;
private static final long serialVersionUID = MAVLINK_MSG_ID_POSITION_TARGET_LOCAL_NED;

private Parser parser = new Parser();

public CRC generateCRC(byte[] packet){
    CRC crc = new CRC();
    for (int i = 1; i < packet.length - 2; i++) {
        crc.update_checksum(packet[i] & 0xFF);
    }
    crc.finish_checksum(MAVLINK_MSG_ID_POSITION_TARGET_LOCAL_NED);
    return crc;
}

public byte[] generateTestPacket(){
    ByteBuffer payload = ByteBuffer.allocate(6 + MAVLINK_MSG_LENGTH + 2);
    payload.put((byte)MAVLinkPacket.MAVLINK_STX); //stx
    payload.put((byte)MAVLINK_MSG_LENGTH); //len
    payload.put((byte)0); //seq
    payload.put((byte)255); //sysid
    payload.put((byte)190); //comp id
    payload.put((byte)MAVLINK_MSG_ID_POSITION_TARGET_LOCAL_NED); //msg id
    payload.putInt((int)963497464); //time_boot_ms
    payload.putFloat((float)45.0); //x
    payload.putFloat((float)73.0); //y
    payload.putFloat((float)101.0); //z
    payload.putFloat((float)129.0); //vx
    payload.putFloat((float)157.0); //vy
    payload.putFloat((float)185.0); //vz
    payload.putFloat((float)213.0); //afx
    payload.putFloat((float)241.0); //afy
    payload.putFloat((float)269.0); //afz
    payload.putFloat((float)297.0); //yaw
    payload.putFloat((float)325.0); //yaw_rate
    payload.putShort((short)19731); //type_mask
    payload.put((byte)27); //coordinate_frame
    
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
    assertArrayEquals("msg_position_target_local_ned", processedPacket, packet);
}
}
        