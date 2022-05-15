package ru.dz.mqtt_udp.util;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class GenericPacketTest {

    TestGenericPacket packet = new TestGenericPacket();

    class TestGenericPacket extends GenericPacket {

        @Override
        public byte[] toBytes() {
            return new byte[0];
        }

        @Override
        public int getType() {
            return 0;
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

    @Test
    public void send() throws IOException {
        packet.send();
    }

    @Test
    public void toBytes() {
        packet.toBytes();
    }

    @Test
    public void getQoS() {
        packet.getQoS();
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
