package ru.dz.mqtt_udp.packets;


import ru.dz.mqtt_udp.io.IPacketAddress;

import static ru.dz.mqtt_udp.packets.PacketType.PingResponse;

public final class PingRespPacket extends GenericPacket {

	public PingRespPacket(Flags flags, IPacketAddress from) {
		super(flags,from);
	}

	public PingRespPacket() {
		this(new Flags(),IPacketAddress.LOCAL);
	}


	@Override
	public Bytes toBytes() {
		return Packets.encodeTotalLength(new Bytes(), PingResponse, flags, null, this );
	}

	@Override
	public PacketType getType() {		return PingResponse;	}
	
	
	@Override
	public String toString() {		
		return String.format("MQTT/UDP PING Responce" );
	}
	

}
