package ru.dz.mqtt_udp.io;

import org.junit.Test;

import java.net.SocketAddress;

import static org.junit.Assert.*;

public class IpAddressTest {

    @Test(expected = NullPointerException.class)
    public void requires_socket_address() {
        assertNotNull(new IpAddress(null));
    }

    @Test
    public void can_create() {
        assertNotNull(new IpAddress(new SocketAddress() {
        }));
    }
}
