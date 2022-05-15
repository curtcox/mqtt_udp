package ru.dz.mqtt_udp.packets;

import ru.dz.mqtt_udp.io.IPacketAddress;
import ru.dz.mqtt_udp.util.Flags;
import ru.dz.mqtt_udp.util.GenericPacket;
import ru.dz.mqtt_udp.util.mqtt_udp_defs;

public final class PingReqPacket extends GenericPacket {

	/**
	 * Construct from incoming UDP data. 
	 * @param raw Data from UDP packet, starting after packet type and length.
	 * @param flags Flags from packet header.
	 * @param from Source IP address.
	 */

	public PingReqPacket(byte[] raw, Flags flags, IPacketAddress from) {
		super(flags,from);
		if( raw.length > 0 )
			System.err.println("nonempty PingReqPacket");
	}

	/**
	 * Create packet to be sent.
	 */
	public PingReqPacket() {
	}
	
	/*
	 * (non-Javadoc)
	 * @see ru.dz.mqtt_udp.IPacket#toBytes()
	 */
	@Override
	public byte[] toBytes() {
		byte[] pkt = new byte[0];
		return Packets.encodeTotalLength(pkt, mqtt_udp_defs.PTYPE_PINGREQ, getFlags(), null, this );
	}

	/*
	 * (non-Javadoc)
	 * @see ru.dz.mqtt_udp.IPacket#getType()
	 */
	@Override
	public int getType() {		return mqtt_udp_defs.PTYPE_PINGREQ;	}
	
	/*
	 * (non-Javadoc)
	 * @see ru.dz.mqtt_udp.util.GenericPacket#toString()
	 */
	@Override
	public String toString() {		
		return String.format("MQTT/UDP PING Request" );
	}	
	
}
