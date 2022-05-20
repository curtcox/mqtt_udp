package ru.dz.mqtt_udp.packets;

import org.junit.Test;
import ru.dz.mqtt_udp.IPacket;
import ru.dz.mqtt_udp.proto.TaggedTailRecord;
import ru.dz.mqtt_udp.util.Flags;

import java.io.IOException;
import java.util.AbstractCollection;

import static org.junit.Assert.*;
import static ru.dz.mqtt_udp.TestUtil.assertEqualBytes;
import static ru.dz.mqtt_udp.packets.Packets.encodeTTR;
import static ru.dz.mqtt_udp.packets.Packets.encodeTotalLength;

public class PacketsTest {

    @Test
    public void packets_written_to_net_can_be_read_from_net() throws IOException {
        IPacket.IO net = Packets.net;
        IPacket ping = new PingReqPacket();
        net.write(ping);
        IPacket read = net.read();

        assertEquals(ping, read);
    }

    @Test
    public void encodeTotalLength_returns_the_same_contents_when_invoked_twice() {
        PingReqPacket packet = new PingReqPacket();
        byte[] bytes = new byte[0];
        PacketType packetType = packet.getType();
        Flags flags = packet.getFlags();
        assertEqualBytes(
                encodeTotalLength(bytes, packetType, flags, null, packet),
                encodeTotalLength(bytes, packetType, flags, null, packet)
        );
    }

    @Test
    public void encodeTTR_returns_the_same_contents_when_invoked_twice() {
        PingReqPacket packet = new PingReqPacket();
        byte[] packetBeginning = new byte[0];
        assertEqualBytes(
                encodeTTR(null, packetBeginning, packet),
                encodeTTR(null, packetBeginning, packet)
        );
    }
}
