/* AUTO-GENERATED FILE.  DO NOT MODIFY.
 *
 * This class was automatically generated by the
 * java mavlink generator tool. It should not be modified by hand.
 */
         
// MESSAGE RANGEFINDER PACKING
package com.MAVLink.usvmega;
import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Parser;

import java.nio.ByteBuffer;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

/**
* Rangefinder reporting
*/
public class msg_rangefinder_test{

public static final int MAVLINK_MSG_ID_RANGEFINDER = 173;
public static final int MAVLINK_MSG_LENGTH = 8;
private static final long serialVersionUID = MAVLINK_MSG_ID_RANGEFINDER;

private Parser parser = new Parser();

public com.MAVLink.usvmega.CRC generateCRC(byte[] packet){
    com.MAVLink.usvmega.CRC crc = new com.MAVLink.usvmega.CRC();
    for (int i = 1; i < packet.length - 2; i++) {
        crc.update_checksum(packet[i] & 0xFF);
    }
    crc.finish_checksum(MAVLINK_MSG_ID_RANGEFINDER);
    return crc;
}

public byte[] generateTestPacket(){
    ByteBuffer payload = ByteBuffer.allocate(6 + MAVLINK_MSG_LENGTH + 2);
    payload.put((byte)MAVLinkPacket.MAVLINK_STX); //stx
    payload.put((byte)MAVLINK_MSG_LENGTH); //len
    payload.put((byte)0); //seq
    payload.put((byte)255); //sysid
    payload.put((byte)190); //comp id
    payload.put((byte)MAVLINK_MSG_ID_RANGEFINDER); //msg id
    payload.putFloat((float)17.0); //distance
    payload.putFloat((float)45.0); //voltage
    
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
    assertArrayEquals("msg_rangefinder", processedPacket, packet);
}
}
        