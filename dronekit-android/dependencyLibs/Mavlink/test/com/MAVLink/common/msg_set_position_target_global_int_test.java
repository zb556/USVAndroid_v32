/* AUTO-GENERATED FILE.  DO NOT MODIFY.
 *
 * This class was automatically generated by the
 * java mavlink generator tool. It should not be modified by hand.
 */
         
// MESSAGE SET_POSITION_TARGET_GLOBAL_INT PACKING
package com.MAVLink.common;
import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Parser;
import com.MAVLink.usvmega.CRC;
import java.nio.ByteBuffer;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

/**
* Set vehicle position, velocity and acceleration setpoint in the WGS84 coordinate system.
*/
public class msg_set_position_target_global_int_test{

public static final int MAVLINK_MSG_ID_SET_POSITION_TARGET_GLOBAL_INT = 86;
public static final int MAVLINK_MSG_LENGTH = 53;
private static final long serialVersionUID = MAVLINK_MSG_ID_SET_POSITION_TARGET_GLOBAL_INT;

private Parser parser = new Parser();

public CRC generateCRC(byte[] packet){
    CRC crc = new CRC();
    for (int i = 1; i < packet.length - 2; i++) {
        crc.update_checksum(packet[i] & 0xFF);
    }
    crc.finish_checksum(MAVLINK_MSG_ID_SET_POSITION_TARGET_GLOBAL_INT);
    return crc;
}

public byte[] generateTestPacket(){
    ByteBuffer payload = ByteBuffer.allocate(6 + MAVLINK_MSG_LENGTH + 2);
    payload.put((byte)MAVLinkPacket.MAVLINK_STX); //stx
    payload.put((byte)MAVLINK_MSG_LENGTH); //len
    payload.put((byte)0); //seq
    payload.put((byte)255); //sysid
    payload.put((byte)190); //comp id
    payload.put((byte)MAVLINK_MSG_ID_SET_POSITION_TARGET_GLOBAL_INT); //msg id
    payload.putInt((int)963497464); //time_boot_ms
    payload.putInt((int)963497672); //lat_int
    payload.putInt((int)963497880); //lon_int
    payload.putFloat((float)101.0); //alt
    payload.putFloat((float)129.0); //vx
    payload.putFloat((float)157.0); //vy
    payload.putFloat((float)185.0); //vz
    payload.putFloat((float)213.0); //afx
    payload.putFloat((float)241.0); //afy
    payload.putFloat((float)269.0); //afz
    payload.putFloat((float)297.0); //yaw
    payload.putFloat((float)325.0); //yaw_rate
    payload.putShort((short)19731); //type_mask
    payload.put((byte)27); //target_system
    payload.put((byte)94); //target_component
    payload.put((byte)161); //coordinate_frame
    
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
    assertArrayEquals("msg_set_position_target_global_int", processedPacket, packet);
}
}
        