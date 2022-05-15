package ru.dz.mqtt_udp.util;

import ru.dz.mqtt_udp.io.IPacketAddress;

/**
 * Packets with topic field.
 * Support ifTopicIs filtering.
 * @author dz
 *
 */
public abstract class TopicPacket extends GenericPacket {

	/**
	 * Packet from net.
	 * @param from Source address.
	 */
	public TopicPacket(IPacketAddress from) {
		super(from);
	}

	protected String  topic;
	/**
	 * Get topic value.
	 * @return Topic string.
	 */
	public String getTopic() {			return topic;	}

}
