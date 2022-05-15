package ru.dz.mqtt_udp.util;

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

    static Args parse(String[] args) {
        if (args.length == 2) {
            return new Args(true,args[0],args[1],null);
        }

        return INVALID;

    }

}
