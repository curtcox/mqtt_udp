package ru.dz.mqtt_udp.config;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class ConfigUtilTest {

    @Test
    public void makeId() {
        assertNotNull(ConfigUtil.makeId());
    }

    @Test
    public void getMachineMacAddressString() throws Exception {
        assertNotNull(ConfigUtil.getMachineMacAddressString());
    }

    @Test
    public void getNetworkInterface() throws Exception {
        assertNotNull(ConfigUtil.getNetworkInterface());
    }

}
