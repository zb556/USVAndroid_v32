/* AUTO-GENERATED FILE.  DO NOT MODIFY.
 *
 * This class was automatically generated by the
 * java mavlink generator tool. It should not be modified by hand.
 */
         
// MESSAGE PARAM_MAP_RC PACKING
package com.MAVLink.common;
import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Parser;
import com.MAVLink.usvmega.CRC;
import java.nio.ByteBuffer;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

/**
* Bind a RC channel to a parameter. The parameter should change accoding to the RC channel value.
*/
public class msg_param_map_rc_test{

public static final int MAVLINK_MSG_ID_PARAM_MAP_RC = 50;
public static final int MAVLINK_MSG_LENGTH = 37;
private static final long serialVersionUID = MAVLINK_MSG_ID_PARAM_MAP_RC;

private Parser parser = new Parser();

public CRC generateCRC(byte[] packet){
    CRC crc = new CRC();
    for (int i = 1; i < packet.length - 2; i++) {
        crc.update_checksum(packet[i] & 0xFF);
    }
    crc.finish_checksum(MAVLINK_MSG_ID_PARAM_MAP_RC);
    return crc;
}

public byte[] generateTestPacket(){
    ByteBuffer payload = ByteBuffer.allocate(6 + MAVLINK_MSG_LENGTH + 2);
    payload.put((byte)MAVLinkPacket.MAVLINK_STX); //stx
    payload.put((byte)MAVLINK_MSG_LENGTH); //len
    payload.put((byte)0); //seq
    payload.put((byte)255); //sysid
    payload.put((byte)190); //comp id
    payload.put((byte)MAVLINK_MSG_ID_PARAM_MAP_RC); //msg id
    payload.putFloat((float)17.0); //param_value0
    payload.putFloat((float)45.0); //scale
    payload.putFloat((float)73.0); //param_value_min
    payload.putFloat((float)101.0); //param_value_max
    payload.putShort((short)18067); //param_index
    payload.put((byte)187); //target_system
    payload.put((byte)254); //target_component
    //param_id
    payload.put((byte)'U');
    payload.put((byte)'V');
    payload.put((byte)'W');
    payload.put((byte)'X');
    payload.put((byte)'Y');
    payload.put((byte)'Z');
    payload.put((byte)'A');
    payload.put((byte)'B');
    payload.put((byte)'C');
    payload.put((byte)'D');
    payload.put((byte)'E');
    payload.put((byte)'F');
    payload.put((byte)'G');
    payload.put((byte)'H');
    payload.put((byte)'I');
    payload.put((byte)'U');
    payload.put((byte)113); //parameter_rc_channel_index
    
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
    assertArrayEquals("msg_param_map_rc", processedPacket, packet);
}
}
        