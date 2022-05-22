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

	private final PacketType packetType;

	/**
	 * Packet source address, if packet is received from net.
	 * Locally created ones have LOCAL here.
	 */
	public final IPacketAddress from;

	public final PacketNumber packetNumber = new PacketNumber(0);
	public final boolean signed = false;
	@Override
	final public boolean isSigned() {		return signed;	}

	/**
	 * Construct packet from network.
	 * @param from Sender's address.
	 */
	protected GenericPacket(PacketType packetType, Flags flags,IPacketAddress from) {
		this.packetType = notNull(packetType);
		this.flags = notNull(flags);
		this.from = notNull(from);
	}

	@Override
	final public IPacketAddress getFrom() { return from; }

	@Override
	final public Bytes toBytes() {
		return Packets.encodeTotalLength(typeSpecificBytes(), getType(), flags, null, packetNumber );
	}

	@Override
	final public PacketType getType() { return packetType;	}

	public Bytes typeSpecificBytes() {
		return new Bytes();
	}

	@Override
	final public String toString() {
		return String.format("MQTT/UDP " + packetType );
	}

}
