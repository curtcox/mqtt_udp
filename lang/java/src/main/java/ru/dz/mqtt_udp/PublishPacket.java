package ru.dz.mqtt_udp;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import ru.dz.mqtt_udp.io.IPacketAddress;
import ru.dz.mqtt_udp.util.Flags;
import ru.dz.mqtt_udp.util.NoEncodingRuntimeException;
import ru.dz.mqtt_udp.util.TopicPacket;
import ru.dz.mqtt_udp.util.mqtt_udp_defs;

/**
 * PUBLISH packet. Carries actual topic data update.
 * @author dz
 *
 */
public final class PublishPacket extends TopicPacket {

	//private String  topic;
	private byte[]  value;

	
	/**
	 * Construct from incoming UDP data. 
	 * @param raw Data from UDP packet, starting after packet type and length.
	 * @param flags Flags from packet header.
	 * @param from Source IP address.
	 */
	public PublishPacket(byte[] raw, Flags flags, IPacketAddress from) {
		super(flags,topic(raw),from);
		int tlen = Packets.decodeTopicLen( raw );
		int vlen = raw.length - tlen - 2;		
		value = new byte[vlen];	
		System.arraycopy( raw, tlen+2, value, 0, vlen );
		
		//this.from = from;
	}

	private static String topic(byte[] raw) {
		int tlen = Packets.decodeTopicLen( raw );
		return new String(raw, 2, tlen, Charset.forName(MQTT_CHARSET));
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

	/**
	 * Create packet to be sent.
	 * 
	 * @param topic Topic string.
	 * @param flags Protocol flags.
	 * @param value Value as byte array.
	 */
	public PublishPacket(String topic, Flags flags, byte[] value) {
		super(flags,topic, null);
		makeMe(value);
	}

	/**
	 * Create packet to be sent.
	 * 
	 * @param topic Topic string.
	 * @param value Value string.
	 */
	public PublishPacket(String topic, Flags flags, String value) {
		super(flags,topic,null);
		try {
			makeMe(value.getBytes(MQTT_CHARSET) );
		} catch (UnsupportedEncodingException e) {
			throw new NoEncodingRuntimeException(e);
		}
	}
	
	/**
	 * Create packet to be sent.
	 * 
	 * @param topic Topic string.
	 * @param value Value string.
	 * @param QoS Required QoS, 0-3
	 */
	public PublishPacket(String topic, String value, int QoS ) {
		super(new Flags(QoS),topic,null);
		try {
			makeMe(value.getBytes(MQTT_CHARSET) );
		} catch (UnsupportedEncodingException e) {
			throw new NoEncodingRuntimeException(e);
		}
	}

	private void makeMe(byte[] value) {
		this.value = value;
	}

	/*
	 * (non-Javadoc)
	 * @see ru.dz.mqtt_udp.IPacket#toBytes()
	 */
	@Override
	public byte[] toBytes() {
		byte[] tbytes;
		try {
			tbytes = getTopic().getBytes(MQTT_CHARSET);
		} catch (UnsupportedEncodingException e) {
			throw new NoEncodingRuntimeException(e);
		}
		
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
