package ru.dz.mqtt_udp.config;

import org.junit.Test;
import ru.dz.mqtt_udp.packets.PublishPacket;
import ru.dz.mqtt_udp.packets.Topic;
import ru.dz.mqtt_udp.util.Flags;

import java.io.IOException;

import static org.junit.Assert.*;

public class RequesterTest {

    @Test
    public void can_create() {
        assertNotNull(new Requester());
    }

    @Test
    public void addTopic() throws IOException {
        Topic topic = new Topic("stuff");
        Requester requester = new Requester();

        requester.addTopic(topic);
    }

    @Test(timeout=1000)
    public void waitForAll_returns_true_when_no_topics_requested() {
        Requester requester = new Requester();
        assertTrue(requester.waitForAll(1));
    }

    @Test(timeout=1000)
    public void waitForAll_returns_false_when_awaiting_1_requested_topic() throws Exception {
        Requester requester = new Requester();
        requester.addTopic(new Topic("Anything"));
        assertFalse(requester.waitForAll(1));
    }

    @Test(timeout=1000)
    public void waitForAll_returns_true_when_requested_topic_received() throws Exception {
        Requester requester = new Requester();
        Topic topic = new Topic("Stuff");
        requester.addTopic(topic);
        requester.accept(PublishPacket.from("", new Flags(),topic,null));
        assertFalse(requester.waitForAll(1));
    }

}
