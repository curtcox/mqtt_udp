package ru.dz.mqtt_udp.servers;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import ru.dz.mqtt_udp.IPacket;
import ru.dz.mqtt_udp.IPacketMultiSource;
import ru.dz.mqtt_udp.items.AbstractItem;
import ru.dz.mqtt_udp.items.Items;

/**
 * 
 * Supports multiple listeners at once.
 * 
 * Usage:
 * <pre>
 * PacketSourceServer ss = new PacketSourceServer();
 * ss.addPacketSink( pkt -&gt; { System.out.println(pkt);});
 * 
 * confing.Provider p = new(); 
 * ss.addPacketSink( p );
 * </pre>
 * 
 * <p>DOES NOT start automatically.</p>
 * 
 * @author dz
 *
 */
public final class PacketSourceMultiServer extends SubServer implements IPacketMultiSource {

	private final List< Consumer<IPacket> > packetSinks = new ArrayList<>();
	private final List< Consumer<AbstractItem> > itemSinks = new ArrayList<>();

	/*
	 * (non-Javadoc)
	 * @see ru.dz.mqtt_udp.IPacketMultiSource#addPacketSink(java.util.function.Consumer)
	 */
	@Override
	public void addPacketSink(Consumer<IPacket> sink) {
		synchronized (packetSinks) {
			packetSinks.add(sink);
		}
	}

	@Override
	public void removePacketSink(Consumer<IPacket> sink) {
		synchronized (packetSinks) {
			packetSinks.remove(sink);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see ru.dz.mqtt_udp.IPacketMultiSource#addItemSink(java.util.function.Consumer)
	 */
	@Override
	public void addItemSink(Consumer<AbstractItem> sink) {
		synchronized (itemSinks) {
			itemSinks.add(sink);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see ru.dz.mqtt_udp.IPacketMultiSource#removeItemSink(java.util.function.Consumer)
	 */
	@Override
	public void removeItemSink(Consumer<AbstractItem> sink) {
		synchronized (itemSinks) {
			itemSinks.remove(sink);
		}
	}


	@Override
	protected void processPacket(IPacket p) {

		synchronized (itemSinks) {
			if ( itemSinks.size() > 0 ) {
				AbstractItem item = Items.fromPacket(p);
				for( Consumer<AbstractItem> itemSink : itemSinks) {
					itemSink.accept(item);
				}
			}
		}

		synchronized (packetSinks) {
			for ( Consumer<IPacket> packetSink : packetSinks) {
				packetSink.accept(p);
			}
		}

	}

}
