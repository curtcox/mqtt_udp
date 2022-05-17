package ru.dz.mqtt_udp.packets;

import ru.dz.mqtt_udp.util.mqtt_udp_defs;

import static ru.dz.mqtt_udp.util.mqtt_udp_defs.*;

public enum PacketType {

    Unknown(0),
    PingRequest(PTYPE_PINGREQ),
    PingResponse(PTYPE_PINGRESP),
    Publish(PTYPE_PUBLISH),
    PublishAck(PTYPE_PUBACK),
    Subscribe(PTYPE_SUBSCRIBE),

    Unsubscribe(PTYPE_UNSUBSCRIBE);

    public int value;

    PacketType(int value) {
        this.value = value;
    }

    public boolean typeWithTopic() {
        return this == Publish || this == Subscribe || this == Unsubscribe;
    }

    public boolean isPingOrResponce() {
        return this == PingRequest || this == PingResponse;
    }

}
