package ru.dz.mqtt_udp.proto;

import org.junit.Test;
import ru.dz.mqtt_udp.Engine;
import ru.dz.mqtt_udp.hmac.HMAC;

import static org.junit.Assert.*;
import static ru.dz.mqtt_udp.TestUtil.assertEqualBytes;

public class TTR_SignatureTest {

    @Test
    public void can_create() {
        assertNotNull(new TTR_Signature(null));
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

        TTR_Signature sig = new TTR_Signature(signature);

        assertEqualBytes(sig.toBytes(),sig.toBytes());
    }

}
