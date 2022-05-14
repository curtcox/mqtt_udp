package ru.dz.mqtt_udp.config;

final class LocalConfigurableHost extends ConfigurableHost {
    private static String id = ConfigUtil.makeId();

    public LocalConfigurableHost() {
        super(id, null);
    }

}
