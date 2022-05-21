package ru.dz.mqtt_udp.packets;

import java.nio.charset.Charset;

import ru.dz.mqtt_udp.io.IPacketAddress;

import static ru.dz.mqtt_udp.packets.PacketType.Publish;
import static ru.dz.mqtt_udp.util.Check.notNull;

/**
 * PUBLISH packet. Carries actual topic data update.
 * @author dz
 *
 */
public final class PublishPacket extends TopicPacket {

	private final Bytes value;

	public PublishPacket(Flags flags, Topic topic, IPacketAddress from,Bytes value) {
		super(flags,topic,from);
		this.value = notNull(value);
	}

	public PublishPacket() {
		this(new Flags(),Topic.UnknownPacket,IPacketAddress.LOCAL,new Bytes());
	}

	public String getValueString() {	return new String(value.bytes, Charset.forName(MQTT_CHARSET));	}

	@Override
	public Bytes toBytes() {
		Bytes tbytes = getTopic().getBytes();
		int plen = tbytes.length + value.length + 2;
					
		byte [] pkt = new byte[plen]; 

		pkt[0] = (byte) (((tbytes.length >>8) & 0xFF) | (flags.toByte() & 0x0F)); // TODO encodeTotalLength does it?
		pkt[1] = (byte) (tbytes.length & 0xFF);
		
		System.arraycopy(tbytes, 0, pkt, 2, tbytes.length);
		System.arraycopy(value, 0, pkt, tbytes.length + 2, value.length );
		
		//return IPacket.encodeTotalLength(pkt, IPacket.PT_PUBLISH);
		return Packets.encodeTotalLength(new Bytes(pkt), Publish, flags, null, this );
	}

	@Override
	public String toString() {		
		return String.format("MQTT/UDP PUBLISH '%s'='%s'", getTopic(), getValueString() );
	}

	@Override
	public PacketType getType() {
		return Publish;
	}

}
