/* AUTO-GENERATED FILE.  DO NOT MODIFY.
 *
 * This class was automatically generated by the
 * java mavlink generator tool. It should not be modified by hand.
 */
         
// MESSAGE FILE_TRANSFER_PROTOCOL PACKING
package com.MAVLink.common;
import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Parser;
import com.MAVLink.usvmega.CRC;
import java.nio.ByteBuffer;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

/**
* File transfer message
*/
public class msg_file_transfer_protocol_test{

public static final int MAVLINK_MSG_ID_FILE_TRANSFER_PROTOCOL = 110;
public static final int MAVLINK_MSG_LENGTH = 254;
private static final long serialVersionUID = MAVLINK_MSG_ID_FILE_TRANSFER_PROTOCOL;

private Parser parser = new Parser();

public CRC generateCRC(byte[] packet){
    CRC crc = new CRC();
    for (int i = 1; i < packet.length - 2; i++) {
        crc.update_checksum(packet[i] & 0xFF);
    }
    crc.finish_checksum(MAVLINK_MSG_ID_FILE_TRANSFER_PROTOCOL);
    return crc;
}

public byte[] generateTestPacket(){
    ByteBuffer payload = ByteBuffer.allocate(6 + MAVLINK_MSG_LENGTH + 2);
    payload.put((byte)MAVLinkPacket.MAVLINK_STX); //stx
    payload.put((byte)MAVLINK_MSG_LENGTH); //len
    payload.put((byte)0); //seq
    payload.put((byte)255); //sysid
    payload.put((byte)190); //comp id
    payload.put((byte)MAVLINK_MSG_ID_FILE_TRANSFER_PROTOCOL); //msg id
    payload.put((byte)5); //target_network
    payload.put((byte)72); //target_system
    payload.put((byte)139); //target_component
    //payload
    payload.put((byte)206);
    payload.put((byte)207);
    payload.put((byte)208);
    payload.put((byte)209);
    payload.put((byte)210);
    payload.put((byte)211);
    payload.put((byte)212);
    payload.put((byte)213);
    payload.put((byte)214);
    payload.put((byte)215);
    payload.put((byte)216);
    payload.put((byte)217);
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
    payload.put((byte)52);
    payload.put((byte)53);
    payload.put((byte)54);
    payload.put((byte)55);
    payload.put((byte)56);
    payload.put((byte)57);
    payload.put((byte)58);
    payload.put((byte)59);
    payload.put((byte)60);
    payload.put((byte)61);
    payload.put((byte)62);
    payload.put((byte)63);
    payload.put((byte)64);
    payload.put((byte)65);
    payload.put((byte)66);
    payload.put((byte)67);
    payload.put((byte)68);
    payload.put((byte)69);
    payload.put((byte)70);
    payload.put((byte)71);
    payload.put((byte)72);
    payload.put((byte)73);
    payload.put((byte)74);
    payload.put((byte)75);
    payload.put((byte)76);
    payload.put((byte)77);
    payload.put((byte)78);
    payload.put((byte)79);
    payload.put((byte)80);
    payload.put((byte)81);
    payload.put((byte)82);
    payload.put((byte)83);
    payload.put((byte)84);
    payload.put((byte)85);
    payload.put((byte)86);
    payload.put((byte)87);
    payload.put((byte)88);
    payload.put((byte)89);
    payload.put((byte)90);
    payload.put((byte)91);
    payload.put((byte)92);
    payload.put((byte)93);
    payload.put((byte)94);
    payload.put((byte)95);
    payload.put((byte)96);
    payload.put((byte)97);
    payload.put((byte)98);
    payload.put((byte)99);
    payload.put((byte)100);
    payload.put((byte)101);
    payload.put((byte)102);
    payload.put((byte)103);
    payload.put((byte)104);
    payload.put((byte)105);
    payload.put((byte)106);
    payload.put((byte)107);
    payload.put((byte)108);
    payload.put((byte)109);
    payload.put((byte)110);
    payload.put((byte)111);
    payload.put((byte)112);
    payload.put((byte)113);
    payload.put((byte)114);
    payload.put((byte)115);
    payload.put((byte)116);
    payload.put((byte)117);
    payload.put((byte)118);
    payload.put((byte)119);
    payload.put((byte)120);
    payload.put((byte)121);
    payload.put((byte)122);
    payload.put((byte)123);
    payload.put((byte)124);
    payload.put((byte)125);
    payload.put((byte)126);
    payload.put((byte)127);
    payload.put((byte)128);
    payload.put((byte)129);
    payload.put((byte)130);
    payload.put((byte)131);
    payload.put((byte)132);
    payload.put((byte)133);
    payload.put((byte)134);
    payload.put((byte)135);
    payload.put((byte)136);
    payload.put((byte)137);
    payload.put((byte)138);
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
    assertArrayEquals("msg_file_transfer_protocol", processedPacket, packet);
}
}
        