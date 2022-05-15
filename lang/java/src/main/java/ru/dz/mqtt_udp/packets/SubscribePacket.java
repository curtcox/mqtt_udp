package ru.dz.mqtt_udp.packets;

import ru.dz.mqtt_udp.util.Flags;

import ru.dz.mqtt_udp.io.IPacketAddress;
import ru.dz.mqtt_udp.util.mqtt_udp_defs;

public final class SubscribePacket extends TopicPacket {

	public SubscribePacket(Topic topic, Flags flags, IPacketAddress from) {
		super(flags,topic,from);
	}

	/**
	 * Create packet to be sent.
	 * @param topic Topic string.
	 */
	public SubscribePacket(Topic topic) {
		super(new Flags(),topic, null);
	}

	/*
	 * (non-Javadoc)
	 * @see ru.dz.mqtt_udp.IPacket#toBytes()
	 */
	@Override
	public byte[] toBytes() {
		byte[] tbytes = getTopic().getBytes();
		int plen = tbytes.length + 2 + 1; // + QoS byte

		byte [] pkt = new byte[plen]; 

		pkt[0] = (byte) (((tbytes.length >>8) & 0xFF) | (getFlags().toByte() & 0x0F)); // TODO encodeTotalLength does it?
		pkt[1] = (byte) (tbytes.length & 0xFF);

		System.arraycopy(tbytes, 0, pkt, 2, tbytes.length);
		//System.arraycopy(value, 0, pkt, tbytes.length + 2, value.length );

		pkt[tbytes.length + 2] = 0; // Requested QoS is allways zero now - TODO add property
		
		return Packets.encodeTotalLength(pkt, mqtt_udp_defs.PTYPE_SUBSCRIBE, getFlags(), null, this );
	}

	@Override
	public String toString() {		
		return String.format("MQTT/UDP SUBSCRIBE '%s'", getTopic() );
	}

	/*
	 * (non-Javadoc)
	 * @see ru.dz.mqtt_udp.IPacket#getType()
	 */
	@Override
	public int getType() {
		return mqtt_udp_defs.PTYPE_SUBSCRIBE;
	}

}
