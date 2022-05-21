package ru.dz.mqtt_udp.io;

import ru.dz.mqtt_udp.IPacket;
import ru.dz.mqtt_udp.packets.Bytes;
import ru.dz.mqtt_udp.packets.Packets;

import java.io.IOException;
import java.io.InputStream;

public final class PacketInputStreamReader implements IPacket.Reader {

    private final InputStream in;

    public PacketInputStreamReader(InputStream in) {
        this.in = in;
    }

    @Override
    public IPacket read() throws IOException {
        byte[] bytes = new byte[0];
        in.read(bytes);
        return Packets.fromBytes(new Bytes(bytes),IPacketAddress.LOCAL);
    }
}
