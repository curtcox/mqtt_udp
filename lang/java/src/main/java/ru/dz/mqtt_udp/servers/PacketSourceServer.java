package ru.dz.mqtt_udp.servers;

import ru.dz.mqtt_udp.IPacket;
import ru.dz.mqtt_udp.IPacketSource;

import java.util.function.Consumer;

/**
 * Usage:
 * <pre>
 * PacketSourceServer ss = new PacketSourceServer();
 * ss.setSink( pkt -&gt; { System.out.println(pkt);});
 * </pre>
 * 
 * <p>Starts automatically.</p>
 * 
 * @author dz
 *
 */
public final class PacketSourceServer extends SubServer implements IPacketSource {

	private Consumer<IPacket> sink;

	/**
	 * Starts reception thread.
	 */
	public PacketSourceServer(IPacket.IO io) {
        super(io);
        start();
	}
		
	/*
	 * (non-Javadoc)
	 * @see ru.dz.mqtt_udp.IPacketSource#setSink(java.util.function.Consumer)
	 */
	@Override
	public void setSink(Consumer<IPacket> sink) {		this.sink = sink;	}

	@Override
	protected void processPacket(IPacket p) { if(sink != null ) sink.accept(p);	}
	
}


