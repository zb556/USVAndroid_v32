/* AUTO-GENERATED FILE.  DO NOT MODIFY.
 *
 * This class was automatically generated by the
 * java mavlink generator tool. It should not be modified by hand.
 */
         
// MESSAGE DATA16 PACKING
package com.MAVLink.usvmega;
import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Parser;

import java.nio.ByteBuffer;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

/**
* Data packet, size 16
*/
public class msg_data16_test{

public static final int MAVLINK_MSG_ID_DATA16 = 169;
public static final int MAVLINK_MSG_LENGTH = 18;
private static final long serialVersionUID = MAVLINK_MSG_ID_DATA16;

private Parser parser = new Parser();

public com.MAVLink.usvmega.CRC generateCRC(byte[] packet){
    com.MAVLink.usvmega.CRC crc = new com.MAVLink.usvmega.CRC();
    for (int i = 1; i < packet.length - 2; i++) {
        crc.update_checksum(packet[i] & 0xFF);
    }
    crc.finish_checksum(MAVLINK_MSG_ID_DATA16);
    return crc;
}

public byte[] generateTestPacket(){
    ByteBuffer payload = ByteBuffer.allocate(6 + MAVLINK_MSG_LENGTH + 2);
    payload.put((byte)MAVLinkPacket.MAVLINK_STX); //stx
    payload.put((byte)MAVLINK_MSG_LENGTH); //len
    payload.put((byte)0); //seq
    payload.put((byte)255); //sysid
    payload.put((byte)190); //comp id
    payload.put((byte)MAVLINK_MSG_ID_DATA16); //msg id
    payload.put((byte)5); //type
    payload.put((byte)72); //len
    //data
    payload.put((byte)139);
    payload.put((byte)140);
    payload.put((byte)141);
    payload.put((byte)142);
    payload.put((byte)143);
    payload.put((byte)144);
    payload.put((byte)145);
    payload.put((byte)146);
    payload.put((byte)147);
    payload.put((byte)148);
    payload.put((byte)149);
    payload.put((byte)150);
    payload.put((byte)151);
    payload.put((byte)152);
    payload.put((byte)153);
    payload.put((byte)154);
    
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
    assertArrayEquals("msg_data16", processedPacket, packet);
}
}
        