package ru.dz.mqtt_udp.util;

import java.io.IOException;

final class Args {

    final boolean areValid;
    final String topic;
    final String msg;
    final String signatureKey;

    static final Args INVALID = new Args(false, null, null, null);

    Args(boolean areValid, String topic, String msg, String signatureKey) {
        this.areValid = areValid;
        this.topic = topic;
        this.msg = msg;
        this.signatureKey = signatureKey;
    }

    static Args parse(String[] args) throws IOException {
        String topic;
        String msg;
        String signatureKey;

        if (args.length == 2) {
            return new Args(true,args[0],args[1],null);
        }

        return INVALID;

    }

}
