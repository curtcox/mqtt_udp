package ru.dz.mqtt_udp.packets;

import org.junit.Test;
import ru.dz.mqtt_udp.io.IPacketAddress;
import ru.dz.mqtt_udp.util.Flags;

import java.io.IOException;

import static org.junit.Assert.*;

public class GenericPacketTest {

    TestGenericPacket packet = new TestGenericPacket();

    class TestGenericPacket extends GenericPacket {

        TestGenericPacket() {
            super(new Flags(), IPacketAddress.LOCAL);
        }

        @Override
        public byte[] toBytes() {
            return new byte[0];
        }

        @Override
        public PacketType getType() {
            return PacketType.Unknown;
        }
    }

    @Test
    public void can_create() {
        assertNotNull(new TestGenericPacket());
    }

    @Test
    public void getPacketNumber() {
        packet.getPacketNumber();
    }

    @Test
    public void flags() {
        packet.getFlags();
    }

    @Test
    public void from() {
        packet.getFrom();
    }

    @Test
    public void getType() {
        packet.getType();
    }

//    @Test
//    public void send() throws IOException {
//        packet.send();
//    }

    @Test
    public void toBytes() {
        packet.toBytes();
    }

    @Test
    public void getQoS() {
        packet.getFlags().getQoS();
    }

    @Test
    public void getAckCount() {
        packet.getAckCount();
    }

    @Test
    public void incrementAckCount() {
        packet.incrementAckCount();
    }

    @Test
    public void isSigned() {
        packet.isSigned();
    }

}
