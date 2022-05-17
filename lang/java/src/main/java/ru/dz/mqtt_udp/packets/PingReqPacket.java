package ru.dz.mqtt_udp.packets;

import ru.dz.mqtt_udp.io.IPacketAddress;
import ru.dz.mqtt_udp.util.Flags;
import ru.dz.mqtt_udp.util.mqtt_udp_defs;

public final class PingReqPacket extends GenericPacket {

	public PingReqPacket(byte[] raw, Flags flags, IPacketAddress from) {
		super(flags,from);
		if( raw.length > 0 )
			System.err.println("nonempty PingReqPacket");
	}

	public PingReqPacket() {
		this(new byte[0],new Flags(),IPacketAddress.LOCAL);
	}

	@Override
	public byte[] toBytes() {
		byte[] pkt = new byte[0];
		return Packets.encodeTotalLength(pkt, mqtt_udp_defs.PTYPE_PINGREQ, getFlags(), null, this );
	}

	@Override
	public int getType() {		return mqtt_udp_defs.PTYPE_PINGREQ;	}
	
	@Override
	public String toString() {		
		return String.format("MQTT/UDP PING Request" );
	}	
	
}
