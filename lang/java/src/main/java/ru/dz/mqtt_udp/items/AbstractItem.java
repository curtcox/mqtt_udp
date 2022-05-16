package ru.dz.mqtt_udp.items;

import java.io.IOException;
import java.net.InetAddress;

import ru.dz.mqtt_udp.packets.*;
import ru.dz.mqtt_udp.util.MqttUdpRuntimeException;
import ru.dz.mqtt_udp.util.mqtt_udp_defs;

/**
 * <p>Container to keep packet data for display and edit.</p> 
 *
 * @author dz
 *
 */

public abstract class AbstractItem {
	protected int packetType = -1;

	private String from = "?";
	private String time = getCurrentTime();
	private boolean signed = false; // Is it has digital signature

	/*public AbstractItem() {
		// TODO Auto-generated constructor stub
	}*/

	protected AbstractItem(int packetType) {
		this.packetType = packetType;
	}

	protected AbstractItem(AbstractItem src) {
		this.from  = src.from;
		this.time = src.time;
		this.signed = src.signed;
	}

	// ---------------------------------------------------
	// C'tors help

	protected void assertHasTopic() {		assert typeWithTopic();	}
	protected void assertHasNoTopic() {		assert !typeWithTopic();	}

	public boolean typeWithTopic() {
		return (packetType == mqtt_udp_defs.PTYPE_PUBLISH) ||
				//(packetType == mqtt_udp_defs.PTYPE_PUBACK) ||
				(packetType == mqtt_udp_defs.PTYPE_SUBSCRIBE) ||
				//(packetType == mqtt_udp_defs.PTYPE_SUBACK) ||
				(packetType == mqtt_udp_defs.PTYPE_UNSUBSCRIBE) 
				//|| (packetType == mqtt_udp_defs.PTYPE_UNSUBACK)
				;
	}

	public boolean isPingOrResponce() {
		return
				(packetType == mqtt_udp_defs.PTYPE_PINGREQ) ||
				(packetType == mqtt_udp_defs.PTYPE_PINGRESP)
				;
	}

	public boolean isPublish() {
		return (packetType == mqtt_udp_defs.PTYPE_PUBLISH);
	}

	private static String getCurrentTime() {
		return java.time.LocalTime.now().toString();
	}

	public void setFrom(String from) { this.from = from; }
	public String getFrom() {		return from;	}

	public String getTime() {		return time;	}

	public boolean isSigned() { return signed; } 
	protected void setSigned(boolean signed) { this.signed = signed; }

	// TODO assign value and time only? check for host/topic be same?
	/** 
	 * Assign all data from src
	 * 
	 * @param src object to copy. 
	 **/
	public void assignFrom(AbstractItem src) {
		this.packetType = src.packetType;
		this.from       = src.from;
		this.time       = src.time;
		this.signed     = src.signed; 
	}

	public GenericPacket toPacket() {
		switch(packetType) {
		    case mqtt_udp_defs.PTYPE_PINGREQ:  return new PingReqPacket();
		    case mqtt_udp_defs.PTYPE_PINGRESP: return new PingRespPacket();
		}

		// TODO not runtime exception?
		throw new MqttUdpRuntimeException("Unknown pkt type 0x"+Integer.toHexString(packetType));
	}

	public void sendToAll() throws IOException {
		GenericPacket pkt = toPacket();
		pkt.send();
	}

	public void sendTo(InetAddress addr) throws IOException {
		GenericPacket pkt = toPacket();
		pkt.send( addr );
	}

}
