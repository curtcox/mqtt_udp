package ru.dz.mqtt_udp.config;

import org.junit.Test;
import ru.dz.mqtt_udp.IPacket;
import ru.dz.mqtt_udp.io.IPacketAddress;
import ru.dz.mqtt_udp.io.IpAddress;
import ru.dz.mqtt_udp.packets.Packets;
import ru.dz.mqtt_udp.packets.SubscribePacket;
import ru.dz.mqtt_udp.packets.Topic;
import ru.dz.mqtt_udp.packets.Flags;

import java.net.SocketAddress;

import static org.junit.Assert.*;

public class ProviderTest {

    IPacket.Writer writer = Packets.out;

    @Test
    public void can_create() {
        assertNotNull(new Provider(writer));
    }

    @Test
    public void addTopic() {
        Provider provider = new Provider(writer);
        Topic topicName = new Topic("topicName");
        String topicValue = "topicValue";
        provider.addTopic(topicName,topicValue);
    }

    @Test
    public void accept() {
        Provider provider = new Provider(writer);
        Topic topic = new Topic("schmopic");
        Flags flags = new Flags();
        IPacketAddress from = new IpAddress(new SocketAddress() {});
        SubscribePacket packet = new SubscribePacket(topic,flags,from);

        provider.accept(packet);
    }

}
