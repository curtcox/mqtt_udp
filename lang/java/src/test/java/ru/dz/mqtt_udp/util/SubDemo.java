package ru.dz.mqtt_udp.util;

import ru.dz.mqtt_udp.packets.Packets;

public final class SubDemo {

    public static void main(String[] args) {
        Sub.startSub(Packets.io);
    }

}
