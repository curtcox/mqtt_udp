package ru.dz.mqtt_udp.config;

import org.junit.Test;
import ru.dz.mqtt_udp.IPacket;
import ru.dz.mqtt_udp.io.IPacketAddress;
import ru.dz.mqtt_udp.packets.Packets;
import ru.dz.mqtt_udp.packets.PublishPacket;
import ru.dz.mqtt_udp.packets.Topic;
import ru.dz.mqtt_udp.util.Flags;

import java.io.IOException;

import static org.junit.Assert.*;

public class RequesterTest {

    final IPacket.Writer writer = Packets.out;

    @Test
    public void can_create() {
        assertNotNull(new Requester(writer));
    }


    @Test
    public void done_before_any_topics_are_added() {
        Requester requester = new Requester(writer);

        assertTrue(requester.isDone());
    }

    @Test
    public void not_done_after_new_topic_added() throws IOException {
        Topic topic = new Topic("stuff");
        Requester requester = new Requester(writer);

        requester.addTopic(topic);
        assertFalse(requester.isDone());
    }

    @Test(timeout=1000)
    public void waitForAll_returns_true_when_no_topics_requested() {
        Requester requester = new Requester(writer);

        assertTrue(requester.waitForAll(1));
        assertTrue(requester.isDone());
    }

    @Test(timeout=1000)
    public void waitForAll_returns_false_when_awaiting_1_requested_topic() throws Exception {
        Requester requester = new Requester(writer);
        requester.addTopic(new Topic("Anything"));

        assertFalse(requester.waitForAll(1));
        assertFalse(requester.isDone());
    }

    @Test(timeout=1000)
    public void waitForAll_returns_true_when_requested_topic_received() throws Exception {
        Requester requester = new Requester(writer);
        Topic topic = new Topic("Stuff");
        requester.addTopic(topic);
        requester.accept(PublishPacket.from("", new Flags(),topic, IPacketAddress.LOCAL));

        assertTrue(requester.waitForAll(1));
        assertTrue(requester.isDone());
    }

}
