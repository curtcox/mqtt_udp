package ru.dz.mqtt_udp.util;

public class Check {

    public static <T> T notNull(T t) {
        if (t==null) {
            throw new NullPointerException();
        }
        return t;
    }

    public static void notNull(Object... args) {
        for (Object arg: args) {
            if (arg==null) {
                throw new NullPointerException();
            }
        }
    }

}
