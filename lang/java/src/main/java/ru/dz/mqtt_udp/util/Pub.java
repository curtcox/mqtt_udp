package ru.dz.mqtt_udp.util;

import java.io.IOException;

import ru.dz.mqtt_udp.*;
import ru.dz.mqtt_udp.io.PacketOutputStreamWriter;
import ru.dz.mqtt_udp.packets.Flags;
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
		sendMessageToTopic(params.msg,params.topic,new PacketOutputStreamWriter(System.out));
	}

	static void sendMessageToTopic(String msg, Topic topic, IPacket.Writer writer) throws IOException {
		System.out.println("Will send "+msg+" to "+topic);

		writer.write(PublishPacket.from(msg,new Flags(),topic,null));

		System.out.println("Sent ok");
	}

	public static void usage() {
		System.err.println("usage: Pub [-s SignaturePassword] topic message");
	}

}
