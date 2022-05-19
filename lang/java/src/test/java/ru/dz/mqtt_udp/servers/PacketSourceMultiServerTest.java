package ru.dz.mqtt_udp.servers;

import org.junit.Test;
import ru.dz.mqtt_udp.IPacket;
import ru.dz.mqtt_udp.items.AbstractItem;
import ru.dz.mqtt_udp.packets.Packets;
import ru.dz.mqtt_udp.packets.PingReqPacket;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

public class PacketSourceMultiServerTest {

    IPacket.IO io = Packets.io;

    @Test
    public void can_create() {
        assertNotNull(new PacketSourceMultiServer(io));
    }

    @Test
    public void processPacket_does_nothing_when_no_sinks() {
        PacketSourceMultiServer server = new PacketSourceMultiServer(io);
        server.processPacket(new PingReqPacket());
    }

    @Test
    public void processPacket_notifies_added_packet_sink() {
        PacketSourceMultiServer server = new PacketSourceMultiServer(io);

        IPacket[] packets = new IPacket[1];
        server.addPacketSink(iPacket -> packets[0] = iPacket);

        IPacket expected = new PingReqPacket();
        server.processPacket(expected);

        assertSame(expected,packets[0]);
    }

    @Test
    public void processPacket_notifies_added_item_sink() {
        PacketSourceMultiServer server = new PacketSourceMultiServer(io);

        AbstractItem[] items = new AbstractItem[1];
        server.addItemSink(item -> items[0] = item);

        IPacket packet = new PingReqPacket();
        server.processPacket(packet);

        AbstractItem item = items[0];
        assertSame(packet.getType(),item.toPacket().getType());
    }

}
