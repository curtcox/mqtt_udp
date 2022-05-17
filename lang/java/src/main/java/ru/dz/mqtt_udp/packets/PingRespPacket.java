package ru.dz.mqtt_udp.packets;


import ru.dz.mqtt_udp.io.IPacketAddress;
import ru.dz.mqtt_udp.util.Flags;
import ru.dz.mqtt_udp.util.mqtt_udp_defs;

public final class PingRespPacket extends GenericPacket {

	public PingRespPacket(byte[] raw, Flags flags, IPacketAddress from) {
		super(flags,from);
		if( raw.length > 0 )
			System.err.println("nonempty PingRespPacket");
	}

	public PingRespPacket() {
		this(new byte[0],new Flags(),IPacketAddress.LOCAL);
	}


	@Override
	public byte[] toBytes() {
		byte[] pkt = new byte[0];
		return Packets.encodeTotalLength(pkt, mqtt_udp_defs.PTYPE_PINGRESP, getFlags(), null, this );
	}

	@Override
	public int getType() {		return mqtt_udp_defs.PTYPE_PINGRESP;	}
	
	
	@Override
	public String toString() {		
		return String.format("MQTT/UDP PING Responce" );
	}
	

}
