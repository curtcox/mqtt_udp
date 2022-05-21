package ru.dz.mqtt_udp.packets;

import ru.dz.mqtt_udp.util.NoEncodingRuntimeException;
import ru.dz.mqtt_udp.util.mqtt_udp_defs;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import static ru.dz.mqtt_udp.IPacket.MQTT_CHARSET;

public final class Topic {
    final String topic;

    public static final Topic UnknownPacket = new Topic("UnknownPacket");
    public static final Topic SYS_CONF_WILD = new Topic(mqtt_udp_defs.SYS_CONF_PREFIX+"/#");


    public Topic(String topic) {
        this.topic = topic;
    }

    static Topic from(byte[] raw) {
        int tlen = Packets.decodeTopicLen( raw );
        return new Topic(new String(raw, 2, tlen, Charset.forName(MQTT_CHARSET)));
    }


    @Override
    public boolean equals(Object other) {
        Topic that = (Topic) other;
        return topic.equals(that.topic);
    }

    @Override
    public int hashCode() {
        return topic.hashCode();
    }

    @Override
    public String toString() {
        return topic;
    }

    public String suffix() {
        return topic.substring(mqtt_udp_defs.SYS_CONF_PREFIX.length());
    }

    byte[] getBytes() {
        try {
            return topic.getBytes(MQTT_CHARSET);
        } catch (UnsupportedEncodingException e) {
            throw new NoEncodingRuntimeException(e);
        }
    }

}
