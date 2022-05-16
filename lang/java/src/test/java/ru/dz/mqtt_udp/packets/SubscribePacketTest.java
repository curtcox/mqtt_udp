package ru.dz.mqtt_udp.packets;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class SubscribePacketTest {

    @Test(expected = NullPointerException.class)
    public void requires_topic() {
        assertNotNull(new SubscribePacket(null));
    }

    @Test
    public void can_create() {
        assertNotNull(new SubscribePacket(Topic.UnknownPacket));
    }
}
