package ru.dz.mqtt_udp.packets;


import ru.dz.mqtt_udp.io.IPacketAddress;

import static ru.dz.mqtt_udp.packets.PacketType.PingResponse;

public final class PingRespPacket extends GenericPacket {

	public PingRespPacket(Flags flags, IPacketAddress from) {
		super(PingResponse,flags,from);
	}

	public PingRespPacket() {
		this(new Flags(),IPacketAddress.LOCAL);
	}

}
