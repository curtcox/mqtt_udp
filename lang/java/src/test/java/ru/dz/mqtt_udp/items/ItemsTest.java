package ru.dz.mqtt_udp.items;

import org.junit.Test;
import ru.dz.mqtt_udp.IPacket;
import ru.dz.mqtt_udp.io.IpAddress;
import ru.dz.mqtt_udp.packets.Bytes;
import ru.dz.mqtt_udp.packets.PublishPacket;
import ru.dz.mqtt_udp.packets.Topic;
import ru.dz.mqtt_udp.packets.Flags;

import java.net.SocketAddress;

import static org.junit.Assert.*;

public class ItemsTest {

    @Test
    public void creates_item_from_packet() {
        IPacket packet = new PublishPacket(new Flags(), new Topic(""), new IpAddress(new SocketAddress() {}),new Bytes());

        TopicItem item = Items.fromPacket(packet);

        assertNotNull(item);
    }
}
