package ru.dz.mqtt_udp.packets;

import ru.dz.mqtt_udp.io.IPacketAddress;

import static ru.dz.mqtt_udp.packets.PacketType.PingRequest;

public final class PingReqPacket extends GenericPacket {

	public PingReqPacket(Flags flags, IPacketAddress from) {
		super(PingRequest,flags,from);
	}

	public PingReqPacket() {
		this(new Flags(),IPacketAddress.LOCAL);
	}

}
