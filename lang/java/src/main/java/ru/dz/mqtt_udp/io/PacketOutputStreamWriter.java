package ru.dz.mqtt_udp.io;

import ru.dz.mqtt_udp.IPacket;

import java.io.IOException;
import java.io.OutputStream;

import static ru.dz.mqtt_udp.util.Check.notNull;

public final class PacketOutputStreamWriter implements IPacket.Writer {

    private final OutputStream out;

    public PacketOutputStreamWriter(OutputStream out) {
        this.out = notNull(out);
    }

    @Override
    public void write(IPacket packet) throws IOException {
        out.write(packet.toBytes().bytes);
    }
}
