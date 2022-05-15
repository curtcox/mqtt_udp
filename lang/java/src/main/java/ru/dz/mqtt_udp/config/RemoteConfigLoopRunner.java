package ru.dz.mqtt_udp.config;

import ru.dz.mqtt_udp.util.LoopRunner;

public abstract class RemoteConfigLoopRunner extends LoopRunner {

    RemoteConfigLoopRunner() {
        super("Remote Config Controllable");
    }

    @Override
    protected void onStop() { /** empty */ }
    @Override
    protected void onStart() { /** empty */ }

}
