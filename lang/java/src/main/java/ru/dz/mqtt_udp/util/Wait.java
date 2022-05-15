package ru.dz.mqtt_udp.util;

import java.io.IOException;

import ru.dz.mqtt_udp.Engine;
import ru.dz.mqtt_udp.IPacket;
import ru.dz.mqtt_udp.MqttProtocolException;
import ru.dz.mqtt_udp.packets.PublishPacket;
import ru.dz.mqtt_udp.servers.SubServer;

/**
 * 
 * NB!! ------------- PART OF GLOBAL REGRESS TEST, do not change -----------------------
 * 
 * 
 * @author dz
 *
 */
public final class Wait extends SubServer {

	final private String topic;
	final private String value;

	public static void main(String[] args) throws IOException, MqttProtocolException {
		Args params = Args.parse(args);
		if (!params.areValid) {
			usage();
			return;
		}
		if (params.signatureKey!=null) {
			Engine.setSignatureKey(params.signatureKey);
		}
		startWait(params.msg,params.topic);
	}

	static void startWait(String value,String topic) {
		Thread timer = new Thread(() -> {
			sleep(4000);
			System.out.println("Timed out");
			System.exit(-1);
		});
		timer.start();

		Wait srv = new Wait( topic, value );
		srv.start();
	}

	static void usage() {
		System.err.println("usage: Wait [-s SignaturePassword] topic message");
		System.err.println("Will wait for given topic==value, part of global regress test");
		System.exit(2);
	}
	
	public Wait(String topic, String value) {
		this.topic = topic;
		this.value = value;
		System.out.println("Will wait for "+topic+" = "+value);
	}

	@Override
	protected void processPacket(IPacket p) {
		//System.out.println(p);

		if (p instanceof PublishPacket) {
			PublishPacket pp = (PublishPacket) p;
			if( pp.getTopic().equals(topic) && pp.getValueString().equals(value) ) {
				System.out.println("Got it!");
				System.exit(0);
			}
		}
	}
}

