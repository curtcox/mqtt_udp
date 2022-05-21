package ru.dz.mqtt_udp.packets;

import ru.dz.mqtt_udp.io.IPacketAddress;

import static ru.dz.mqtt_udp.packets.PacketType.PingRequest;

public final class PingReqPacket extends GenericPacket {

	public PingReqPacket(Flags flags, IPacketAddress from) {
		super(flags,from);
	}

	public PingReqPacket() {
		this(new Flags(),IPacketAddress.LOCAL);
	}

	@Override
	public Bytes toBytes() {
		return Packets.encodeTotalLength(new Bytes(), PingRequest, flags, null, this );
	}

	@Override
	public PacketType getType() { return PingRequest; }
	
	@Override
	public String toString() {		
		return String.format("MQTT/UDP PING Request" );
	}	
	
}
