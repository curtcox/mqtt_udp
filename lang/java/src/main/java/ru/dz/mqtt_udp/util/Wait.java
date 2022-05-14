package ru.dz.mqtt_udp.util;

import java.io.IOException;

import ru.dz.mqtt_udp.Engine;
import ru.dz.mqtt_udp.IPacket;
import ru.dz.mqtt_udp.MqttProtocolException;
import ru.dz.mqtt_udp.PublishPacket;
import ru.dz.mqtt_udp.SubServer;

/**
 * 
 * NB!! ------------- PART OF GLOBAL REGRESS TEST, do not change -----------------------
 * 
 * 
 * @author dz
 *
 */
public final class Wait extends SubServer
{

	private String topic;
	private String value;

	public static void main(String[] args) throws IOException, MqttProtocolException {
		String topic;
		String value;
		
		if(args.length == 4) {
			if( !args[0].equals("-s") ) {
				usage();
				return;
			}
			Engine.setSignatureKey(args[1]);
			topic = args[2];
			value = args[3];
		} else {
			if(args.length != 2) {
				usage();
				return;
			}

			topic = args[0];
			value = args[1];
		}
		
		startWait( value, topic );
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

	public static void usage() {
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

