package ru.dz.mqtt_udp.util;

import java.io.IOException;

public final class PubDemo {

    public static void main(String[] args) throws IOException {
        Pub.sendMessageToTopic("Hello","World");
    }
}
