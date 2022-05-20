package ru.dz.mqtt_udp.packets;

import java.util.AbstractCollection;
import java.util.ArrayList;

import ru.dz.mqtt_udp.io.IPacketAddress;
import ru.dz.mqtt_udp.proto.TTR_ReplyTo;
import ru.dz.mqtt_udp.proto.TaggedTailRecord;
import ru.dz.mqtt_udp.util.*;

import static ru.dz.mqtt_udp.packets.PacketType.PublishAck;

public final class PubAckPacket extends GenericPacket {

	private PublishPacket replyToPkt;

	public PubAckPacket(byte[] raw, Flags flags, IPacketAddress from) {
		super(flags,from);
		if( raw.length > 0 )
			System.err.println("non-empty PubAck Packet");
	}


	@Override
	public byte[] toBytes() {
		byte[] pkt = new byte[0];
		AbstractCollection<TaggedTailRecord> ttrs = new ArrayList<TaggedTailRecord>();

		TTR_ReplyTo id = new TTR_ReplyTo(replyToPkt.getPacketNumber());
		ttrs.add(id);

		return Packets.encodeTotalLength(pkt, PublishAck, getFlags(), ttrs, this );
	}

	@Override
	public PacketType getType() {		return PublishAck;	}

	@Override
	public String toString() {		
		return String.format("MQTT/UDP PubAck" );
	}

}
