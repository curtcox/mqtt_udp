package ru.dz.mqtt_udp;

import java.util.function.Consumer;

import ru.dz.mqtt_udp.items.AbstractItem;

/**
 * Supports multiple listeners for incoming packets.
 * @author dz
 *
 */
public interface IPacketMultiSource {
	/**
	 * Add sink to consume received packets.
	 * @param sink Consumer&lt;IPacket&gt; to add to consumers list.
	 */
	void addPacketSink(Consumer<IPacket> sink);
	/**
	 * Remove sink from consumers list.
	 * @param sink Consumer&lt;IPacket&gt; to add to consumers list.
	 */
	void removePacketSink(Consumer<IPacket> sink);

	
	
	
	/**
	 * Add sink to consume received packets.
	 * @param sink Consumer&lt;AbstractItem&gt; to add to consumers list.
	 */
	void addItemSink(Consumer<AbstractItem> sink);
	/**
	 * Remove sink from consumers list.
	 * @param sink Consumer&lt;AbstractItem&gt; to add to consumers list.
	 */
	void removeItemSink(Consumer<AbstractItem> sink);
	
	
}
