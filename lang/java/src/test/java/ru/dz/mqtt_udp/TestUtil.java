package ru.dz.mqtt_udp;

import static org.junit.Assert.assertEquals;

public final class TestUtil {

    public static void assertEqualBytes(byte[] a, byte[] b) {
        assertEquals("Different lengths " + a.length + "!=" + b.length ,a.length,b.length);
        for (int i=0; i<a.length; i++) {
            assertEquals(a[i] + "!=" + b[i] + " at index " + i, a[i],b[i]);
        }
    }

}
