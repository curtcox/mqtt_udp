package ru.dz.mqtt_udp.util;

import java.io.IOException;

import ru.dz.mqtt_udp.*;
import ru.dz.mqtt_udp.packets.PublishPacket;
import ru.dz.mqtt_udp.packets.Topic;

/**
 * NB!
 * 
 * This program is used in global regress test and its output is checked.
 * See test/runner
 * 
 * @author dz
 *
 */
public final class Pub {

	public static void main(String[] args) throws IOException {
		Args params = Args.parse(args);
		if (!params.areValid) {
			usage();
			return;
		}
		if (params.signatureKey!=null) {
			Engine.setSignatureKey(params.signatureKey);
		}
		sendMessageToTopic(params.msg,params.topic);
	}

	static void sendMessageToTopic(String msg, Topic topic) throws IOException {
		System.out.println("Will send "+msg+" to "+topic);

		PublishPacket pp = PublishPacket.from(msg,new Flags(),topic,null);
		pp.send();
		System.out.println("Sent ok");
	}

	public static void usage() {
		System.err.println("usage: Pub [-s SignaturePassword] topic message");
	}

}
