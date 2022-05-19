package ru.dz.mqtt_udp.packets;

import org.junit.Test;

import static org.junit.Assert.*;
import static ru.dz.mqtt_udp.TestUtil.assertEqualBytes;

public class PingReqPacketTest {

    @Test
    public void can_create() {
        assertNotNull(new PingReqPacket());
    }

    @Test
    public void toBytes_returns_the_same_contents_when_invoked_twice() {
        PingReqPacket packet = new PingReqPacket();
        assertEqualBytes(packet.toBytes(),packet.toBytes());
    }
}
