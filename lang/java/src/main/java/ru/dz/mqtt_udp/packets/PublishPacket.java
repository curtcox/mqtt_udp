package ru.dz.mqtt_udp.packets;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import ru.dz.mqtt_udp.IPacket;
import ru.dz.mqtt_udp.io.IPacketAddress;
import ru.dz.mqtt_udp.util.Flags;
import ru.dz.mqtt_udp.util.mqtt_udp_defs;

import static ru.dz.mqtt_udp.util.Check.notNull;

/**
 * PUBLISH packet. Carries actual topic data update.
 * @author dz
 *
 */
public final class PublishPacket extends TopicPacket {

	private final byte[]  value;

	public PublishPacket(Flags flags, Topic topic, IPacketAddress from,byte[] value) {
		super(flags,topic,from);
		this.value = notNull(value);
	}

	public static PublishPacket from(byte[] raw, Flags flags, Topic topic, IPacketAddress from) {
		return new PublishPacket(flags,topic,from,value(raw));
	}

	public static PublishPacket from(String value, Flags flags, Topic topic, IPacketAddress from)  {
		notNull(value,flags,topic,from);
		try {
			return new PublishPacket(flags,topic,from,value.getBytes(IPacket.MQTT_CHARSET));
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	private static byte[] value(byte[] raw) {
		int tlen = Packets.decodeTopicLen( raw );
		int vlen = raw.length - tlen - 2;
		byte[] value = new byte[vlen];
		System.arraycopy( raw, tlen+2, value, 0, vlen );
		return value;
	}

	/**
	 * Get value as byte array.
	 * @return Packet value.
	 */
	public byte[] getValueRaw() {		return value;	}
	/**
	 * Get value as string.
	 * @return Packet value.
	 */
	public String getValueString() {	return new String(value, Charset.forName(MQTT_CHARSET));	}

	/*
	 * (non-Javadoc)
	 * @see ru.dz.mqtt_udp.IPacket#toBytes()
	 */
	@Override
	public byte[] toBytes() {
		byte[] tbytes = getTopic().getBytes();
		int plen = tbytes.length + value.length + 2;
					
		byte [] pkt = new byte[plen]; 

		pkt[0] = (byte) (((tbytes.length >>8) & 0xFF) | (getFlags().toByte() & 0x0F)); // TODO encodeTotalLength does it?
		pkt[1] = (byte) (tbytes.length & 0xFF);
		
		System.arraycopy(tbytes, 0, pkt, 2, tbytes.length);
		System.arraycopy(value, 0, pkt, tbytes.length + 2, value.length );
		
		//return IPacket.encodeTotalLength(pkt, IPacket.PT_PUBLISH);
		return Packets.encodeTotalLength(pkt, mqtt_udp_defs.PTYPE_PUBLISH, getFlags(), null, this );
	}

	@Override
	public String toString() {		
		return String.format("MQTT/UDP PUBLISH '%s'='%s'", getTopic(), getValueString() );
	}

	@Override
	public int getType() {
		return mqtt_udp_defs.PTYPE_PUBLISH;
	}

}
