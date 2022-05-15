package ru.dz.mqtt_udp.util;

import ru.dz.mqtt_udp.io.IPacketAddress;

/**
 * Packets with topic field.
 * Support ifTopicIs filtering.
 * @author dz
 *
 */
public abstract class TopicPacket extends GenericPacket {

	private final String  topic;

	/**
	 * Packet from net.
	 * @param from Source address.
	 */
	public TopicPacket(Flags flags,String topic, IPacketAddress from) {
		super(flags,from);
		this.topic = topic;
	}

	/**
	 * Get topic value.
	 * @return Topic string.
	 */
	final public String getTopic() {			return topic;	}

}
