package ru.dz.mqtt_udp.util;

import ru.dz.mqtt_udp.packets.Topic;

import java.io.IOException;

public final class PubDemo {

    public static void main(String[] args) throws IOException {
        Pub.sendMessageToTopic("Hello",new Topic("World"));
    }
}
