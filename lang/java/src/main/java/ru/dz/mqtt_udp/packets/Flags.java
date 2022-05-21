package ru.dz.mqtt_udp.packets;

public final class Flags {

    private byte flags;

    public Flags(int i) {
        flags = (byte) i;
    }

    public Flags() {
        this(0);
    }

    public int getQoS() {
        return (flags >> 1) & 0x3;
    }

    public void setQoS(int qos) {
        flags &= ~0x6;
        flags |= (qos & 0x3) << 1;
    }

    public byte toByte() {
        return flags;
    }
}
