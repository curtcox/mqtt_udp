package ru.dz.mqtt_udp.items;

import ru.dz.mqtt_udp.packets.*;
import ru.dz.mqtt_udp.packets.Flags;

import static ru.dz.mqtt_udp.packets.PacketType.Publish;

// TODO rename to PacketItem, make subclasses per type

/**
 * Container to keep packet data for display and edit. 
 * TODO Actually must be converted to class hierarchy according to packet type.
 * @author dz
 *
 */
public final class TopicItem extends AbstractItem {

	private final Topic topic;
	private final String value;

	public TopicItem(PacketType packetType) {
		super(packetType);
		this.topic = null;
		this.value = "";
	}
	
	public TopicItem(PacketType packetType, Topic topic) {
		super(packetType);
		this.topic = topic;
		this.value = "";
	}

	public TopicItem(PacketType packetType, Topic topic, String value) {
		super(packetType);
		this.topic = topic;
		this.value = value;
	}

	/**
	 * Make from other item.
	 * @param src TopicItem to copy.
	 */
	public TopicItem(TopicItem src) {
		super(src, src.packetType);
		this.topic = src.topic;
		this.value = src.value;
	}

	
	// ---------------------------------------------------
	// Get/set
	

	@Override
	public String toString() {
		String com = (isSigned() ? "Sig! " : "       ")+getTime()+":  ";
		
		if( packetType == Publish)
			return com+topic+"="+value;
		else if (packetType.typeWithTopic())
			return com + packetType +" \ttopic="+topic;
		else
			return com + packetType;
	}

	public Topic getTopic() {		return topic;	}

	public String getValue() {		return value;	}

	public boolean sameTopic( TopicItem t )
	{
		return getTopic().equals(t.getTopic());
	}

	public boolean sameHostAndTopic( TopicItem t ) {
		return getTopic().equals(t.getTopic()) && getFrom().equals(t.getFrom());
	}

	public GenericPacket toPacket() {
		switch(packetType) {
			case Publish: return PublishPacket.from(value, new Flags(), topic, null);
		    default: return super.toPacket();
		}		
	}

	
	
}
