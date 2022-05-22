package ru.dz.mqtt_udp.packets;

import java.util.AbstractCollection;
import java.util.ArrayList;

import ru.dz.mqtt_udp.io.IPacketAddress;
import ru.dz.mqtt_udp.proto.TTR_ReplyTo;
import ru.dz.mqtt_udp.proto.TaggedTailRecord;

import static ru.dz.mqtt_udp.packets.PacketType.PublishAck;
import static ru.dz.mqtt_udp.util.Check.notNull;

public final class PubAckPacket extends GenericPacket {

    public final PacketNumber replyToPkt;

	public PubAckPacket(PacketNumber replyToPkt, Flags flags, IPacketAddress from) {
		super(PublishAck,flags,from);
		this.replyToPkt = notNull(replyToPkt);
	}

	public static GenericPacket from(Bytes sub, Flags flags, IPacketAddress from) {
		throw new UnsupportedOperationException();
	}

	public Bytes typeSpecificBytes() {
		AbstractCollection<TaggedTailRecord> ttrs = new ArrayList<>();

		TTR_ReplyTo id = new TTR_ReplyTo(replyToPkt.value);
		ttrs.add(id);
		return new Bytes();
	}

}
