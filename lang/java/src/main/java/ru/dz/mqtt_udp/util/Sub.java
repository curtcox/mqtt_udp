package ru.dz.mqtt_udp.util;

import ru.dz.mqtt_udp.Engine;
import ru.dz.mqtt_udp.IPacket;
import ru.dz.mqtt_udp.SubServer;

public final class Sub extends SubServer {

	public static void main(String[] args) {
		if(args.length == 2) {
			if( !args[0].equals("-s") ) {
				usage();
				return;
			}
			Engine.setSignatureKey(args[1]);
		} else
			if(args.length != 0) {
				usage();
				return;
			}

		startSub();
	}

	static void startSub() {
		Sub srv = new Sub();
		srv.start();
	}

	public static void usage() {
		System.err.println("usage: Sub [-s SignaturePassword]");
	}

	@Override
	protected void processPacket(IPacket p) {
		System.out.println(p);

		/*
		if (p instanceof PublishPacket) {
			PublishPacket pp = (PublishPacket) p;

			// now use pp.getTopic() and pp.getValueString() or pp.getValueRaw()
		}*/
	}
}

