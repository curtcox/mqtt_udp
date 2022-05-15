package ru.dz.mqtt_udp.config;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import ru.dz.mqtt_udp.IPacket;
import ru.dz.mqtt_udp.IPacketMultiSource;
import ru.dz.mqtt_udp.packets.PublishPacket;
import ru.dz.mqtt_udp.packets.SubscribePacket;
import ru.dz.mqtt_udp.items.TopicItem;
import ru.dz.mqtt_udp.packets.Topic;
import ru.dz.mqtt_udp.util.Flags;
import ru.dz.mqtt_udp.util.mqtt_udp_defs;

/**
 * <p>Remote configuration data provider</p>
 * 
 * <p>See also Requester class</p>
 * 
 * <p>
 * Will reply to requests (subscribe packets) for given topics
 * sending back publish packets with data.
 * </p>
 *  
 * @author dz
 *
 */

public final class Provider implements Consumer<IPacket> {

	//private SubServer ss; // no, need one that can serve multiple listeners with thread pool
	//private ArrayList topics = new ArrayList<>();
	private Map<Topic,TopicItem> items = new HashMap<>();

	static Provider withPacketsFrom(IPacketMultiSource source) {
		Provider provider = new Provider();
		source.addPacketSink(provider);
		return provider;
	}

	public void addTopic(Topic topicName, String topicValue) {
		// TODO need class PublishTopicItem?
		items.put(topicName, new TopicItem(mqtt_udp_defs.PTYPE_PUBLISH, topicName, topicValue));
	}

	@Override
	public void accept(IPacket t) {
		debug("Got packet "+t);
		if (t instanceof SubscribePacket) {
			acceptSubcription((SubscribePacket) t);
		}
	}

	void acceptSubcription(SubscribePacket sp) {
		if (items.containsKey(sp.getTopic())) {
			debug("PROVIDER: Got request for "+sp.getTopic());
			publish(items.get(sp.getTopic()));
		}
	}

	private void publish(TopicItem it) {
		try {
			PublishPacket packet = PublishPacket.from(it.getValue(), new Flags(), it.getTopic(), null);
			packet.send();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void debug(String message) {
		System.out.println("Provider:" + message);
	}
}
