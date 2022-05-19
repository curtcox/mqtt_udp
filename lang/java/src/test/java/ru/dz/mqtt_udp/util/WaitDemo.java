package ru.dz.mqtt_udp.util;

import ru.dz.mqtt_udp.packets.Packets;
import ru.dz.mqtt_udp.packets.Topic;

public class WaitDemo {

    public static void main(String[] args) {
        Wait.startWait(Packets.io,"Hello",new Topic("World"));
    }

}
