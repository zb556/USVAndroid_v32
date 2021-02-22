/* AUTO-GENERATED FILE.  DO NOT MODIFY.
 *
 * This class was automatically generated by the
 * java mavlink generator tool. It should not be modified by hand.
 */
         
// MESSAGE DATA64 PACKING
package com.MAVLink.usvmega;
import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Parser;

import java.nio.ByteBuffer;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

/**
* Data packet, size 64
*/
public class msg_data64_test{

public static final int MAVLINK_MSG_ID_DATA64 = 171;
public static final int MAVLINK_MSG_LENGTH = 66;
private static final long serialVersionUID = MAVLINK_MSG_ID_DATA64;

private Parser parser = new Parser();

public com.MAVLink.usvmega.CRC generateCRC(byte[] packet){
    com.MAVLink.usvmega.CRC crc = new com.MAVLink.usvmega.CRC();
    for (int i = 1; i < packet.length - 2; i++) {
        crc.update_checksum(packet[i] & 0xFF);
    }
    crc.finish_checksum(MAVLINK_MSG_ID_DATA64);
    return crc;
}

public byte[] generateTestPacket(){
    ByteBuffer payload = ByteBuffer.allocate(6 + MAVLINK_MSG_LENGTH + 2);
    payload.put((byte)MAVLinkPacket.MAVLINK_STX); //stx
    payload.put((byte)MAVLINK_MSG_LENGTH); //len
    payload.put((byte)0); //seq
    payload.put((byte)255); //sysid
    payload.put((byte)190); //comp id
    payload.put((byte)MAVLINK_MSG_ID_DATA64); //msg id
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
    payload.put((byte)155);
    payload.put((byte)156);
    payload.put((byte)157);
    payload.put((byte)158);
    payload.put((byte)159);
    payload.put((byte)160);
    payload.put((byte)161);
    payload.put((byte)162);
    payload.put((byte)163);
    payload.put((byte)164);
    payload.put((byte)165);
    payload.put((byte)166);
    payload.put((byte)167);
    payload.put((byte)168);
    payload.put((byte)169);
    payload.put((byte)170);
    payload.put((byte)171);
    payload.put((byte)172);
    payload.put((byte)173);
    payload.put((byte)174);
    payload.put((byte)175);
    payload.put((byte)176);
    payload.put((byte)177);
    payload.put((byte)178);
    payload.put((byte)179);
    payload.put((byte)180);
    payload.put((byte)181);
    payload.put((byte)182);
    payload.put((byte)183);
    payload.put((byte)184);
    payload.put((byte)185);
    payload.put((byte)186);
    payload.put((byte)187);
    payload.put((byte)188);
    payload.put((byte)189);
    payload.put((byte)190);
    payload.put((byte)191);
    payload.put((byte)192);
    payload.put((byte)193);
    payload.put((byte)194);
    payload.put((byte)195);
    payload.put((byte)196);
    payload.put((byte)197);
    payload.put((byte)198);
    payload.put((byte)199);
    payload.put((byte)200);
    payload.put((byte)201);
    payload.put((byte)202);
    
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
    assertArrayEquals("msg_data64", processedPacket, packet);
}
}
        