package ru.dz.mqtt_udp.packets;

import ru.dz.mqtt_udp.io.IPacketAddress;

import static ru.dz.mqtt_udp.packets.PacketType.Subscribe;
import static ru.dz.mqtt_udp.util.Check.notNull;

public final class SubscribePacket extends TopicPacket {

	public SubscribePacket(Topic topic, Flags flags, IPacketAddress from) {
		super(Subscribe,flags,topic,from);
	}

	/**
	 * Create packet to be sent.
	 * @param topic Topic string.
	 */
	public SubscribePacket(Topic topic) {
		super(Subscribe,new Flags(),notNull(topic), IPacketAddress.LOCAL);
	}

	public SubscribePacket() {
		this(Topic.UnknownPacket,new Flags(),IPacketAddress.LOCAL);
	}

	@Override
	public Bytes typeSpecificBytes() {
		Bytes tbytes = getTopic().getBytes();
		int plen = tbytes.length + 2 + 1; // + QoS byte

		byte [] pkt = new byte[plen]; 

		pkt[0] = (byte) (((tbytes.length >>8) & 0xFF) | (flags.toByte() & 0x0F)); // TODO encodeTotalLength does it?
		pkt[1] = (byte) (tbytes.length & 0xFF);

		System.arraycopy(tbytes, 0, pkt, 2, tbytes.length);
		//System.arraycopy(value, 0, pkt, tbytes.length + 2, value.length );

		pkt[tbytes.length + 2] = 0; // Requested QoS is allways zero now - TODO add property
		
		return new Bytes(pkt);
	}
}
