package ru.dz.mqtt_udp.config;

import org.junit.Test;
import ru.dz.mqtt_udp.io.IPacketAddress;
import ru.dz.mqtt_udp.io.IpAddress;
import ru.dz.mqtt_udp.packets.SubscribePacket;
import ru.dz.mqtt_udp.packets.Topic;
import ru.dz.mqtt_udp.util.Flags;

import static org.junit.Assert.*;

public class ProviderTest {

    @Test
    public void can_create() {
        assertNotNull(new Provider());
    }

    @Test
    public void addTopic() {
        Provider provider = new Provider();
        Topic topicName = new Topic("topicName");
        String topicValue = "topicValue";
        provider.addTopic(topicName,topicValue);
    }

    @Test
    public void accept() {
        Provider provider = new Provider();
        Topic topic = new Topic("schmopic");
        Flags flags = new Flags();
        IPacketAddress from = new IpAddress(null);
        SubscribePacket packet = new SubscribePacket(topic,flags,from);

        provider.accept(packet);
    }

}
