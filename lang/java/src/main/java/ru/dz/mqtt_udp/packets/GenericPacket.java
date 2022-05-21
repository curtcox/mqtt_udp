package ru.dz.mqtt_udp.packets;

import ru.dz.mqtt_udp.IPacket;
import ru.dz.mqtt_udp.io.IPacketAddress;

import static ru.dz.mqtt_udp.util.Check.notNull;

/**
 * Network IO work horse for MQTT/UDP packets.
 * @author dz
 *
 */
public abstract class GenericPacket implements IPacket {

	/**
	 * Packet header flags.
	 */
	public final Flags flags;

	/**
	 * Packet source address, if packet is received from net.
	 * Locally created ones have LOCAL here.
	 */
	public final IPacketAddress from;

	public final int packetNumber = 0;
	public final boolean signed = false;
	@Override
	final public boolean isSigned() {		return signed;	}

	/**
	 * Construct packet from network.
	 * @param from Sender's address.
	 */
	protected GenericPacket(Flags flags,IPacketAddress from) {
		this.flags = notNull(flags);
		this.from = notNull(from);
	}

	@Override
	final public IPacketAddress getFrom() { return from; }

	@Override
	public String toString() {
		return String.format("MQTT/UDP packet of unknown type from '%s', please redefine toString in %s", from, getClass().getName());
	}

}
