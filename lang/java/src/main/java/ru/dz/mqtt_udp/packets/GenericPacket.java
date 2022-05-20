package ru.dz.mqtt_udp.packets;

import ru.dz.mqtt_udp.IPacket;
import ru.dz.mqtt_udp.io.IPacketAddress;
import ru.dz.mqtt_udp.util.Flags;

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
	private final Flags flags;

	/**
	 * Packet source address, if packet is received from net.
	 * Locally created ones have LOCAL here.
	 */
	private final IPacketAddress from;

	private final int packetNumber = 0;

	/** 
	 * Broadcast IP address.
	 */
	private static final byte[] broadcast =  { (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF } ;

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
	
	final public Flags getFlags() {		return flags;	}

	
	@Override
	public String toString() {
		return String.format("MQTT/UDP packet of unknown type from '%s', please redefine toString in %s", from, getClass().getName());
	}

	final public int getPacketNumber() {
		return packetNumber;
	}

	private final boolean signed = false;

	@Override
	final public boolean isSigned() {		return signed;	}

}
