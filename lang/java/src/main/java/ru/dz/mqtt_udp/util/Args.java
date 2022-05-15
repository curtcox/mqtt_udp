package ru.dz.mqtt_udp.util;

import ru.dz.mqtt_udp.packets.Topic;

final class Args {

    final boolean areValid;
    final Topic topic;
    final String msg;
    final String signatureKey;

    static final Args INVALID = new Args(false, null, null, null);

    Args(boolean areValid, String topic, String msg, String signatureKey) {
        this.areValid = areValid;
        this.topic = new Topic(topic);
        this.msg = msg;
        this.signatureKey = signatureKey;
    }

    static Args parse(String[] args) {
        if (args.length == 2) {
            return new Args(true,args[0],args[1],null);
        }

        return INVALID;

    }

}
