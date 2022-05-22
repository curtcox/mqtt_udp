package ru.dz.mqtt_udp.packets;

import org.junit.Test;
import ru.dz.mqtt_udp.io.IPacketAddress;

import static org.junit.Assert.*;
import static ru.dz.mqtt_udp.packets.PacketType.Unknown;

public class GenericPacketTest {

    TestGenericPacket packet = new TestGenericPacket();

    class TestGenericPacket extends GenericPacket {

        TestGenericPacket() {
            super(Unknown, new Flags(), IPacketAddress.LOCAL);
        }

    }

    @Test
    public void can_create() {
        assertNotNull(new TestGenericPacket());
    }

    @Test
    public void flags() {
        assertNotNull(packet.flags);
    }

    @Test
    public void from() {
        packet.getFrom();
    }

    @Test
    public void getType() {
        packet.getType();
    }

    @Test
    public void toBytes() {
        packet.toBytes();
    }

    @Test
    public void getQoS() {
        packet.flags.getQoS();
    }

    @Test
    public void isSigned() {
        packet.isSigned();
    }

}
