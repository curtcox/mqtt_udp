package ru.dz.mqtt_udp;

import java.io.IOException;

public final class MqttProtocolException extends IOException {
	
	private static final long serialVersionUID = 9117724925722139158L;

	public MqttProtocolException(String message) {
		super(message);
	}
	
}
