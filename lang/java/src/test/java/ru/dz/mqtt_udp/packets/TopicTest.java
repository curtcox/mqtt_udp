package ru.dz.mqtt_udp.packets;

import org.junit.Test;

import static org.junit.Assert.*;

public class TopicTest {

    @Test
    public void can_create() {
        assertNotNull(new Topic("foo"));
    }

    @Test
    public void equal_topics() {
        assertEquals(new Topic("this"),new Topic("this"));
        assertEquals(new Topic(toString()),new Topic(toString()));
        assertEquals(new Topic(toString().split("p")[0]),new Topic(toString().split("p")[0]));
    }

    @Test
    public void unequal_topics() {
        assertNotEquals(new Topic("foo"),new Topic("bar"));
    }

}
