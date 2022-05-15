package ru.dz.mqtt_udp.items;

import ru.dz.mqtt_udp.packets.Packets;
import ru.dz.mqtt_udp.packets.PublishPacket;
import ru.dz.mqtt_udp.util.Flags;
import ru.dz.mqtt_udp.util.GenericPacket;
import ru.dz.mqtt_udp.util.mqtt_udp_defs;

// TODO rename to PacketItem, make subclasses per type

/**
 * Container to keep packet data for display and edit. 
 * TODO Actually must be converted to class hierarchy according to packet type.
 * @author dz
 *
 */
public final class TopicItem extends AbstractItem {

	
	private String topic;
	private String value;

	public TopicItem(int packetType) {
		super(packetType);
		assertHasNoTopic();
	}
	
	public TopicItem(int packetType, String topic) {
		super(packetType);
		this.topic = topic;
		this.value = "";
		
		assertHasTopic();
	}


	public TopicItem(int packetType, String topic, String value) {
		super(packetType);
		this.topic = topic;
		this.value = value;

		assertHasTopic();
	}

	/**
	 * Make from other item.
	 * @param src TopicItem to copy.
	 */
	public TopicItem(TopicItem src) {
		super(src);
		this.packetType = src.packetType;
		this.topic = src.topic;
		this.value = src.value;
	}

	
	
	
	
	
	
	// ---------------------------------------------------
	// Get/set
	

	@Override
	public String toString() {
		String com = (isSigned() ? "Sig! " : "       ")+getTime()+":  ";
		
		if( packetType == mqtt_udp_defs.PTYPE_PUBLISH)
			return com+topic+"="+value;
		else if(typeWithTopic())
			return com + Packets.getPacketTypeName(packetType)+" \ttopic="+topic;
		else
			return com + Packets.getPacketTypeName(packetType);
	}

	public String getTopic() {		return topic;	}

	public void setValue(String value) { this.value = value; }
	public String getValue() {		return value;	}


	
	// ---------------------------------------------------
	
	
	
	// TODO assign value and time only? check for host/topic be same?
	/** 
	 * Assign all data from src
	 * 
	 * @param src object to copy. 
	 **/
	public void assignFrom(TopicItem src) {
		this.topic	= src.topic;
		this.value	= src.value;
		super.assignFrom(src);
	}

	public boolean sameTopic( TopicItem t )
	{
		return getTopic().equals(t.getTopic());
	}

	public boolean sameHostAndTopic( TopicItem t ) {
		return getTopic().equals(t.getTopic()) && getFrom().equals(t.getFrom());
	}

	//public boolean hasTopic() {		return typeWithTopic();	}

	// ---------------------------------------------------
	
	
	public GenericPacket toPacket() {
		switch(packetType) {
		case mqtt_udp_defs.PTYPE_PUBLISH: return new PublishPacket(topic,new Flags(), value);
		//case mqtt_udp_defs.PTYPE_SUBSCRIBE: return new SubscribePacket(topic);
		
		default: return super.toPacket(); 
		}		
	}

	
	
}
