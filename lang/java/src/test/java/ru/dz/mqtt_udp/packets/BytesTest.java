package ru.dz.mqtt_udp.packets;

import org.junit.Test;

import static ru.dz.mqtt_udp.TestUtil.*;

public class BytesTest {

    @Test
    public void from_combines_given_arrays() {
        from(bytes(0),bytes(1),bytes(0,1));
        from(bytes(1),bytes(0),bytes(1,0));
        from(bytes(1,2,3),bytes(4,5),bytes(1,2,3,4,5));
    }

    private void from(Bytes a, Bytes b, Bytes c) {
        assertEqualBytes(c,Bytes.from(a.bytes,b.bytes));
    }

    private Bytes bytes(int... values) {
        byte[] bytes = new byte[values.length];
        for (int i=0; i<values.length; i++) {
            bytes[i] = (byte) values[i];
        }
        return new Bytes(bytes);
    }
}
