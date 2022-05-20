package ru.dz.mqtt_udp.hmac;

import org.junit.Test;
import ru.dz.mqtt_udp.Engine;

import static org.junit.Assert.*;
import static ru.dz.mqtt_udp.TestUtil.assertEqualBytes;

public class HMACTest {

    @Test
    public void can_create() {
        assertNotNull(new HMAC());
    }

    @Test
    public void toBytes_values_are_consistent() {
        assertConsistent(new byte[0]);
        assertConsistent(new byte[10]);
        assertConsistent(new byte[100]);
        assertConsistent(new byte[1000]);
    }

    void assertConsistent(byte[] toSign) {
        byte[] signature = HMAC.hmacDigestMD5(toSign, Engine.getSignatureKey());
        assertEqualBytes(signature,signature);
    }

}
