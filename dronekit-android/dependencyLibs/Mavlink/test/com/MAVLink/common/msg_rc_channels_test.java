/* AUTO-GENERATED FILE.  DO NOT MODIFY.
 *
 * This class was automatically generated by the
 * java mavlink generator tool. It should not be modified by hand.
 */
         
// MESSAGE RC_CHANNELS PACKING
package com.MAVLink.common;
import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Parser;
import com.MAVLink.usvmega.CRC;
import java.nio.ByteBuffer;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

/**
* The PPM values of the RC channels received. The standard PPM modulation is as follows: 1000 microseconds: 0%, 2000 microseconds: 100%. Individual receivers/transmitters might violate this specification.
*/
public class msg_rc_channels_test{

public static final int MAVLINK_MSG_ID_RC_CHANNELS = 65;
public static final int MAVLINK_MSG_LENGTH = 42;
private static final long serialVersionUID = MAVLINK_MSG_ID_RC_CHANNELS;

private Parser parser = new Parser();

public CRC generateCRC(byte[] packet){
    CRC crc = new CRC();
    for (int i = 1; i < packet.length - 2; i++) {
        crc.update_checksum(packet[i] & 0xFF);
    }
    crc.finish_checksum(MAVLINK_MSG_ID_RC_CHANNELS);
    return crc;
}

public byte[] generateTestPacket(){
    ByteBuffer payload = ByteBuffer.allocate(6 + MAVLINK_MSG_LENGTH + 2);
    payload.put((byte)MAVLinkPacket.MAVLINK_STX); //stx
    payload.put((byte)MAVLINK_MSG_LENGTH); //len
    payload.put((byte)0); //seq
    payload.put((byte)255); //sysid
    payload.put((byte)190); //comp id
    payload.put((byte)MAVLINK_MSG_ID_RC_CHANNELS); //msg id
    payload.putInt((int)963497464); //time_boot_ms
    payload.putShort((short)17443); //chan1_raw
    payload.putShort((short)17547); //chan2_raw
    payload.putShort((short)17651); //chan3_raw
    payload.putShort((short)17755); //chan4_raw
    payload.putShort((short)17859); //chan5_raw
    payload.putShort((short)17963); //chan6_raw
    payload.putShort((short)18067); //chan7_raw
    payload.putShort((short)18171); //chan8_raw
    payload.putShort((short)18275); //chan9_raw
    payload.putShort((short)18379); //chan10_raw
    payload.putShort((short)18483); //chan11_raw
    payload.putShort((short)18587); //chan12_raw
    payload.putShort((short)18691); //chan13_raw
    payload.putShort((short)18795); //chan14_raw
    payload.putShort((short)18899); //chan15_raw
    payload.putShort((short)19003); //chan16_raw
    payload.putShort((short)19107); //chan17_raw
    payload.putShort((short)19211); //chan18_raw
    payload.put((byte)125); //chancount
    payload.put((byte)192); //rssi
    
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
    assertArrayEquals("msg_rc_channels", processedPacket, packet);
}
}
        