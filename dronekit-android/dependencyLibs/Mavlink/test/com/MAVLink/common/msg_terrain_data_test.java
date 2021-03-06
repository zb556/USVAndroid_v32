/* AUTO-GENERATED FILE.  DO NOT MODIFY.
 *
 * This class was automatically generated by the
 * java mavlink generator tool. It should not be modified by hand.
 */
         
// MESSAGE TERRAIN_DATA PACKING
package com.MAVLink.common;
import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Parser;
import com.MAVLink.usvmega.CRC;
import java.nio.ByteBuffer;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

/**
* Terrain data sent from GCS. The lat/lon and grid_spacing must be the same as a lat/lon from a TERRAIN_REQUEST
*/
public class msg_terrain_data_test{

public static final int MAVLINK_MSG_ID_TERRAIN_DATA = 134;
public static final int MAVLINK_MSG_LENGTH = 43;
private static final long serialVersionUID = MAVLINK_MSG_ID_TERRAIN_DATA;

private Parser parser = new Parser();

public CRC generateCRC(byte[] packet){
    CRC crc = new CRC();
    for (int i = 1; i < packet.length - 2; i++) {
        crc.update_checksum(packet[i] & 0xFF);
    }
    crc.finish_checksum(MAVLINK_MSG_ID_TERRAIN_DATA);
    return crc;
}

public byte[] generateTestPacket(){
    ByteBuffer payload = ByteBuffer.allocate(6 + MAVLINK_MSG_LENGTH + 2);
    payload.put((byte)MAVLinkPacket.MAVLINK_STX); //stx
    payload.put((byte)MAVLINK_MSG_LENGTH); //len
    payload.put((byte)0); //seq
    payload.put((byte)255); //sysid
    payload.put((byte)190); //comp id
    payload.put((byte)MAVLINK_MSG_ID_TERRAIN_DATA); //msg id
    payload.putInt((int)963497464); //lat
    payload.putInt((int)963497672); //lon
    payload.putShort((short)17651); //grid_spacing
    //data
    payload.putShort((short)17755);
    payload.putShort((short)17756);
    payload.putShort((short)17757);
    payload.putShort((short)17758);
    payload.putShort((short)17759);
    payload.putShort((short)17760);
    payload.putShort((short)17761);
    payload.putShort((short)17762);
    payload.putShort((short)17763);
    payload.putShort((short)17764);
    payload.putShort((short)17765);
    payload.putShort((short)17766);
    payload.putShort((short)17767);
    payload.putShort((short)17768);
    payload.putShort((short)17769);
    payload.putShort((short)17770);
    payload.put((byte)3); //gridbit
    
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
    assertArrayEquals("msg_terrain_data", processedPacket, packet);
}
}
        