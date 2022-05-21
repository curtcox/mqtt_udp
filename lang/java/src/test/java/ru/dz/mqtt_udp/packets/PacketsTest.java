package ru.dz.mqtt_udp.packets;

import org.junit.Test;
import ru.dz.mqtt_udp.IPacket;
import ru.dz.mqtt_udp.MqttProtocolException;
import ru.dz.mqtt_udp.io.IPacketAddress;

import java.io.IOException;

import static org.junit.Assert.*;
import static ru.dz.mqtt_udp.TestUtil.assertEqualBytes;
import static ru.dz.mqtt_udp.packets.Packets.encodeTTR;
import static ru.dz.mqtt_udp.packets.Packets.encodeTotalLength;

public class PacketsTest {

    @Test(expected = MqttProtocolException.class)
    public void fromBytes_throws_MQTT_exception_for_empty_array() throws MqttProtocolException {
        Packets.fromBytes(new byte[0],null);
    }

    @Test
    public void empty_ping() throws MqttProtocolException {
        fromBytes(new PingReqPacket());
    }

    @Test
    public void empty_ping_response() throws MqttProtocolException {
        fromBytes(new PingRespPacket());
    }

    @Test
    public void empty_publish() throws MqttProtocolException {
        fromBytes(new PublishPacket());
    }

    @Test
    public void empty_subscribe() throws MqttProtocolException {
        fromBytes(new SubscribePacket());
    }

    @Test
    public void fromBytes() throws MqttProtocolException {
        IPacket[] packets = new IPacket[]{
                new PingReqPacket(),
                new PingRespPacket(),
                new PublishPacket(),
                new SubscribePacket(),
        };
        for (IPacket packet: packets) {
            fromBytes(packet);
        }
    }

    private void fromBytes(IPacket original) throws MqttProtocolException {
        IPacket packet = Packets.fromBytes(original.toBytes(), IPacketAddress.LOCAL);
        assertEqualBytes(packet.toBytes(),original.toBytes());
    }

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
        Flags flags = packet.flags;
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
