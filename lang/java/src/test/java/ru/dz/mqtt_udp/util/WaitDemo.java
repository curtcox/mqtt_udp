package ru.dz.mqtt_udp.util;

import ru.dz.mqtt_udp.packets.Topic;

public class WaitDemo {

    public static void main(String[] args) {
        Wait.startWait("Hello",new Topic("World"));
    }

}
