package ru.dz.mqtt_udp.config;

import ru.dz.mqtt_udp.packets.Packets;

public class ControllerDemo {
    public static void main(String[] args) {
        Controller.start(Packets.io);
    }

}
