package ru.dz.mqtt_udp.config;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;


import ru.dz.mqtt_udp.IPacket;
import ru.dz.mqtt_udp.IPacketMultiSource;
import ru.dz.mqtt_udp.items.Items;
import ru.dz.mqtt_udp.packets.PublishPacket;
import ru.dz.mqtt_udp.packets.SubscribePacket;
import ru.dz.mqtt_udp.items.TopicItem;
import ru.dz.mqtt_udp.packets.Topic;
import ru.dz.mqtt_udp.util.LoopRunner;

/**
 * 
 * <p>
 * Active mode remote configuration. Application has no local config 
 * storage and requests config items on each start. Alternatively it
 * has local config storage but updates it from time to time from
 * central node.
 * </p>
 * 
 * <p>Remote configuration requester.</p>
 *
 * <p>See also Provider class.</p>
 * 
 * <p>Keeps set of topics, requests them from network, keeps replies. Usage:</p>
 * 
 * <pre>
 * 
 * IPacketMultiSource ms = new PacketSourceMultiServer();
 * ms.start();
 * 
 * Requester r = Requester(ms);
 * 
 * r.addTopic("$SYS/myinstancename/param"); // will request it from net
 *
 * r.startBackgroundRequests(); // start asking for topic values in loop
 * 
 * if( !waitForAll(10*1000*1000) )
 * {
 * 		print("Can't get config from net"); System.Exit(1);
 * }
 * 
 * </pre>
 * @author dz
 *
 */

public final class Requester implements Consumer<IPacket> {

	private long checkLoopTime = CHECK_LOOP_TIME;

	private final Map<Topic,TopicItem> items = new HashMap<>();

	private static final int CHECK_LOOP_TIME = 1000*60;
	private static final int REQUEST_STEP_TIME = 1000;

	private final LoopRunner runner = new  LoopRunner("MQTT UDP config.Requester") {

		@Override
		protected void onStart() { /** empty */ }

		@Override
		protected void step() throws IOException {
			sleep( checkLoopTime );
			loop();
		}

		@Override
		protected void onStop() { /** empty */ }
		
	};
	
	/**
	 * Construct.
	 * @param source MQTT/UDP network listener which is able to serve multiple consumers.
	 */
	static Requester withPacketsFrom(IPacketMultiSource source) {
		Requester requester = new Requester();
		source.addPacketSink(requester);
		return requester;
	}

	/**
	 * Add topic, which value is to be requested from MQTT/UDP network,
	 * @param topic name of topic to request
	 * @throws IOException if network send is failed
	 */
	public void addTopic(Topic topic) throws IOException {
		synchronized (items) {
			// TODO need class PublishTopicItem?
			//items.put(topicName, new TopicItem(mqtt_udp_defs.PTYPE_PUBLISH, topicName, topicValue));
			put(topic, null);

			SubscribePacket subscribePacket = new SubscribePacket(topic);
			subscribePacket.send();
		}
	}

	/**
	 * Get value for topic
	 * @param topic to get value for
	 * @return value or null if not yet received
	 */
	public String getValue(Topic topic) {
		String v = null;
		synchronized (items) {
			TopicItem ti = items.get(topic);
			if( ti != null )
				v = ti.getValue();
		}		
		return v;
	}

	/**
	 * Implementation of Consumer&lt;IPacket&gt; interface.
	 * Sink to put received packets to.
	 */
	@Override
	public void accept(IPacket t) {
		debug("Accepted " + t);
		if (t instanceof PublishPacket) {
			acceptPublishPacket((PublishPacket) t);
		}
	}

	void acceptPublishPacket(PublishPacket pp) {
		synchronized (items) {
			Topic topic = pp.getTopic();
			if (items.containsKey(topic)) {
				debug("REQUESTER: Got reply for " + topic);
				put(topic, Items.fromPacket(pp));
			}
		}
	}

	private void put(Topic topic, TopicItem topicItem) {
		items.put(topic, topicItem);
	}

	/**
	 * Start background process to poll net for topics we need.
	 */
	public void startBackgroundRequests() {
		runner.requestStart();
	}

	/**
	 * Set time between repeated requests for items.
	 * @param checkLoopTime Time in milliseconds.
	 */
	public void setCheckLoopTime(long checkLoopTime) {		this.checkLoopTime = checkLoopTime;	}
	public long getCheckLoopTime() {		return checkLoopTime;	}

	protected void loop() throws IOException {

		// find topics for which we have no data
		Set<Topic> empty = getAllEmpty();

		// request them one per second		
		for( Topic topic : empty ) {
			new SubscribePacket(topic).send();
			LoopRunner.sleep(REQUEST_STEP_TIME);
		}

	}

	/**
	 * Get list of topics for which we do not have data yet.
	 * @return Set of topic strings
	 */
	public Set<Topic> getAllEmpty() {
		Set<Topic> empty = new HashSet<>();

		synchronized (items) {
			items.forEach( (topic, item) -> {
				if( item == null )
					empty.add(topic);
			});
		}
		return empty;
	}

	/**
	 * Check if all items got values.
	 * @return true if we got all data.
	 */
	public boolean isDone() {
		synchronized (items) {
			for( TopicItem item : items.values() ) {
				if (item == null) {
					debug("items : " + items);
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * Wait for all topics to get content.
	 * Returns when we have data for all topics we know about.
	 * 
	 * @param timeoutMsec Max time to wait.
	 * 
	 * @return true if success, false if timed out.
	 */
	public boolean waitForAll(long timeoutMsec) {
		if ( timeoutMsec < 0 )
			throw new IllegalArgumentException("timeoutMsec < 0");

		long start = System.currentTimeMillis();

		while (true) {
			if ( isDone() ) return true;
			
			long now = System.currentTimeMillis();

			if( (now - start) > timeoutMsec )
				return false;

			sleep(timeoutMsec/5);
		}
	}

	private void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e1) {
			// Ignore
		}
	}
	// TODO set sink to be informed on arrive of some item or any item

	private static void debug(String message) {
		System.out.println("Requester:" + message);
	}

}
