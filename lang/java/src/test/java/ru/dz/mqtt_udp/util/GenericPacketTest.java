package ru.dz.mqtt_udp.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class GenericPacketTest {

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
}
