package ru.dz.mqtt_udp.packets;

import ru.dz.mqtt_udp.io.IPacketAddress;

import static ru.dz.mqtt_udp.util.Check.notNull;

/**
 * Packets with topic field.
 * Support ifTopicIs filtering.
 * @author dz
 *
 */
public abstract class TopicPacket extends GenericPacket {

	private final Topic  topic;

	/**
	 * Packet from net.
	 * @param from Source address.
	 */
	public TopicPacket(Flags flags, Topic topic, IPacketAddress from) {
		super(flags,from);
		this.topic = notNull(topic);
	}

	final public Topic getTopic() {			return topic;	}

}
