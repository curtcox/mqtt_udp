package ru.dz.mqtt_udp.util;

public class Check {

    public static <T> T notNull(T t) {
        if (t==null) {
            throw new NullPointerException();
        }
        return t;
    }

    public static void notNull(Object... args) {
        for (int i=0; i< args.length; i++) {
            Object arg = args[i];
            if (arg==null) {
                throw new NullPointerException(i + " of " + (args.length - 1));
            }
        }
    }

}
