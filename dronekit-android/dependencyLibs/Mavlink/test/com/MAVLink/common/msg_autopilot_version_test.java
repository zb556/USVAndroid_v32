/* AUTO-GENERATED FILE.  DO NOT MODIFY.
 *
 * This class was automatically generated by the
 * java mavlink generator tool. It should not be modified by hand.
 */
         
// MESSAGE AUTOPILOT_VERSION PACKING
package com.MAVLink.common;
import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Parser;
import com.MAVLink.usvmega.CRC;
import java.nio.ByteBuffer;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

/**
* Version and capability of autopilot software
*/
public class msg_autopilot_version_test{

public static final int MAVLINK_MSG_ID_AUTOPILOT_VERSION = 148;
public static final int MAVLINK_MSG_LENGTH = 60;
private static final long serialVersionUID = MAVLINK_MSG_ID_AUTOPILOT_VERSION;

private Parser parser = new Parser();

public CRC generateCRC(byte[] packet){
    CRC crc = new CRC();
    for (int i = 1; i < packet.length - 2; i++) {
        crc.update_checksum(packet[i] & 0xFF);
    }
    crc.finish_checksum(MAVLINK_MSG_ID_AUTOPILOT_VERSION);
    return crc;
}

public byte[] generateTestPacket(){
    ByteBuffer payload = ByteBuffer.allocate(6 + MAVLINK_MSG_LENGTH + 2);
    payload.put((byte)MAVLinkPacket.MAVLINK_STX); //stx
    payload.put((byte)MAVLINK_MSG_LENGTH); //len
    payload.put((byte)0); //seq
    payload.put((byte)255); //sysid
    payload.put((byte)190); //comp id
    payload.put((byte)MAVLINK_MSG_ID_AUTOPILOT_VERSION); //msg id
    payload.putLong((long)93372036854775807L); //capabilities
    payload.putLong((long)93372036854776311L); //uid
    payload.putInt((int)963498296); //flight_sw_version
    payload.putInt((int)963498504); //middleware_sw_version
    payload.putInt((int)963498712); //os_sw_version
    payload.putInt((int)963498920); //board_version
    payload.putShort((short)18899); //vendor_id
    payload.putShort((short)19003); //product_id
    //flight_custom_version
    payload.put((byte)113);
    payload.put((byte)114);
    payload.put((byte)115);
    payload.put((byte)116);
    payload.put((byte)117);
    payload.put((byte)118);
    payload.put((byte)119);
    payload.put((byte)120);
    //middleware_custom_version
    payload.put((byte)137);
    payload.put((byte)138);
    payload.put((byte)139);
    payload.put((byte)140);
    payload.put((byte)141);
    payload.put((byte)142);
    payload.put((byte)143);
    payload.put((byte)144);
    //os_custom_version
    payload.put((byte)161);
    payload.put((byte)162);
    payload.put((byte)163);
    payload.put((byte)164);
    payload.put((byte)165);
    payload.put((byte)166);
    payload.put((byte)167);
    payload.put((byte)168);
    
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
    assertArrayEquals("msg_autopilot_version", processedPacket, packet);
}
}
        