package ru.dz.mqtt_udp.packets;

import java.util.AbstractCollection;
import java.util.ArrayList;

import ru.dz.mqtt_udp.io.IPacketAddress;
import ru.dz.mqtt_udp.proto.TTR_ReplyTo;
import ru.dz.mqtt_udp.proto.TaggedTailRecord;
import ru.dz.mqtt_udp.util.*;

public final class PubAckPacket extends GenericPacket {

	private PublishPacket replyToPkt;

	public PubAckPacket(byte[] raw, Flags flags, IPacketAddress from) {
		super(flags,from);
		if( raw.length > 0 )
			System.err.println("non-empty PubAck Packet");
	}

//	public PubAckPacket(PublishPacket replyTo, int qos) {
//		this.replyToPkt = replyTo;
//		getFlags().setQoS(qos);
//	}

	@Override
	public byte[] toBytes() {
		byte[] pkt = new byte[0];
		AbstractCollection<TaggedTailRecord> ttrs = new ArrayList<TaggedTailRecord>();

		if(!replyToPkt.getPacketNumber().isPresent())
		{
			GlobalErrorHandler.handleError(ErrorType.Protocol, "attempt to PubAck for pkt with no id");
			//throw new MqttProtocolException("attempt to PubAck for pkt with no id");
		}
		else
		{
			TTR_ReplyTo id = new TTR_ReplyTo(replyToPkt.getPacketNumber().get());
			ttrs.add(id);
		}
		
		return Packets.encodeTotalLength(pkt, mqtt_udp_defs.PTYPE_PUBACK, getFlags(), ttrs, this );
	}

	@Override
	public int getType() {		return mqtt_udp_defs.PTYPE_PUBACK;	}

	@Override
	public String toString() {		
		return String.format("MQTT/UDP PubAck" );
	}

}
