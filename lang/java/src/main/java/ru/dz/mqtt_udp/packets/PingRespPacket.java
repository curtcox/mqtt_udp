package ru.dz.mqtt_udp.packets;


import ru.dz.mqtt_udp.io.IPacketAddress;

import static ru.dz.mqtt_udp.packets.PacketType.PingResponse;

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
		return Packets.encodeTotalLength(pkt, PingResponse, flags, null, this );
	}

	@Override
	public PacketType getType() {		return PingResponse;	}
	
	
	@Override
	public String toString() {		
		return String.format("MQTT/UDP PING Responce" );
	}
	

}
