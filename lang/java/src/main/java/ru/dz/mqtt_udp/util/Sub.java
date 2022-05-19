package ru.dz.mqtt_udp.util;

import ru.dz.mqtt_udp.Engine;
import ru.dz.mqtt_udp.IPacket;
import ru.dz.mqtt_udp.packets.Packets;
import ru.dz.mqtt_udp.servers.SubServer;

public final class Sub extends SubServer {

	Sub(IPacket.IO io) {
        super(io);
    }

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

		startSub(Packets.io);
	}

	static void startSub(IPacket.IO io) {
		Sub srv = new Sub(io);
		srv.start();
	}

	public static void usage() {
		System.err.println("usage: Sub [-s SignaturePassword]");
	}

	@Override
	protected void processPacket(IPacket p) {
		System.out.println(p);
	}
}

