/* AUTO-GENERATED FILE.  DO NOT MODIFY.
 *
 * This class was automatically generated by the
 * java mavlink generator tool. It should not be modified by hand.
 */
         
// MESSAGE LOG_DATA PACKING
package com.MAVLink.common;
import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Parser;
import com.MAVLink.usvmega.CRC;
import java.nio.ByteBuffer;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

/**
* Reply to LOG_REQUEST_DATA
*/
public class msg_log_data_test{

public static final int MAVLINK_MSG_ID_LOG_DATA = 120;
public static final int MAVLINK_MSG_LENGTH = 97;
private static final long serialVersionUID = MAVLINK_MSG_ID_LOG_DATA;

private Parser parser = new Parser();

public CRC generateCRC(byte[] packet){
    CRC crc = new CRC();
    for (int i = 1; i < packet.length - 2; i++) {
        crc.update_checksum(packet[i] & 0xFF);
    }
    crc.finish_checksum(MAVLINK_MSG_ID_LOG_DATA);
    return crc;
}

public byte[] generateTestPacket(){
    ByteBuffer payload = ByteBuffer.allocate(6 + MAVLINK_MSG_LENGTH + 2);
    payload.put((byte)MAVLinkPacket.MAVLINK_STX); //stx
    payload.put((byte)MAVLINK_MSG_LENGTH); //len
    payload.put((byte)0); //seq
    payload.put((byte)255); //sysid
    payload.put((byte)190); //comp id
    payload.put((byte)MAVLINK_MSG_ID_LOG_DATA); //msg id
    payload.putInt((int)963497464); //ofs
    payload.putShort((short)17443); //id
    payload.put((byte)151); //count
    //data
    payload.put((byte)218);
    payload.put((byte)219);
    payload.put((byte)220);
    payload.put((byte)221);
    payload.put((byte)222);
    payload.put((byte)223);
    payload.put((byte)224);
    payload.put((byte)225);
    payload.put((byte)226);
    payload.put((byte)227);
    payload.put((byte)228);
    payload.put((byte)229);
    payload.put((byte)230);
    payload.put((byte)231);
    payload.put((byte)232);
    payload.put((byte)233);
    payload.put((byte)234);
    payload.put((byte)235);
    payload.put((byte)236);
    payload.put((byte)237);
    payload.put((byte)238);
    payload.put((byte)239);
    payload.put((byte)240);
    payload.put((byte)241);
    payload.put((byte)242);
    payload.put((byte)243);
    payload.put((byte)244);
    payload.put((byte)245);
    payload.put((byte)246);
    payload.put((byte)247);
    payload.put((byte)248);
    payload.put((byte)249);
    payload.put((byte)250);
    payload.put((byte)251);
    payload.put((byte)252);
    payload.put((byte)253);
    payload.put((byte)254);
    payload.put((byte)255);
    payload.put((byte)0);
    payload.put((byte)1);
    payload.put((byte)2);
    payload.put((byte)3);
    payload.put((byte)4);
    payload.put((byte)5);
    payload.put((byte)6);
    payload.put((byte)7);
    payload.put((byte)8);
    payload.put((byte)9);
    payload.put((byte)10);
    payload.put((byte)11);
    payload.put((byte)12);
    payload.put((byte)13);
    payload.put((byte)14);
    payload.put((byte)15);
    payload.put((byte)16);
    payload.put((byte)17);
    payload.put((byte)18);
    payload.put((byte)19);
    payload.put((byte)20);
    payload.put((byte)21);
    payload.put((byte)22);
    payload.put((byte)23);
    payload.put((byte)24);
    payload.put((byte)25);
    payload.put((byte)26);
    payload.put((byte)27);
    payload.put((byte)28);
    payload.put((byte)29);
    payload.put((byte)30);
    payload.put((byte)31);
    payload.put((byte)32);
    payload.put((byte)33);
    payload.put((byte)34);
    payload.put((byte)35);
    payload.put((byte)36);
    payload.put((byte)37);
    payload.put((byte)38);
    payload.put((byte)39);
    payload.put((byte)40);
    payload.put((byte)41);
    payload.put((byte)42);
    payload.put((byte)43);
    payload.put((byte)44);
    payload.put((byte)45);
    payload.put((byte)46);
    payload.put((byte)47);
    payload.put((byte)48);
    payload.put((byte)49);
    payload.put((byte)50);
    payload.put((byte)51);
    
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
    assertArrayEquals("msg_log_data", processedPacket, packet);
}
}
        