package ru.dz.mqtt_udp.items;

import java.io.IOException;
import java.net.InetAddress;

import ru.dz.mqtt_udp.packets.*;
import ru.dz.mqtt_udp.util.MqttUdpRuntimeException;

import static ru.dz.mqtt_udp.util.Check.notNull;

/**
 * <p>Container to keep packet data for display and edit.</p> 
 *
 * @author dz
 *
 */
public abstract class AbstractItem {
	final PacketType packetType;

	private String from = "?";
	private long time = getCurrentTime();
	private boolean signed = false; // Is it has digital signature

	protected AbstractItem(PacketType packetType) {
		this.packetType = notNull(packetType);
	}

	protected AbstractItem(AbstractItem src,PacketType packetType) {
		this.from  = src.from;
		this.time = src.time;
		this.signed = src.signed;
		this.packetType = packetType;
	}

	public boolean isPublish() {
		return (packetType == PacketType.Publish);
	}

	private static long getCurrentTime() {
		return System.currentTimeMillis();
	}

	public void setFrom(String from) { this.from = from; }
	public String getFrom() {		return from;	}

	public long getTime() {		return time;	}

	public boolean isSigned() { return signed; } 
	protected void setSigned(boolean signed) { this.signed = signed; }

	public GenericPacket toPacket() {
		switch(packetType) {
			case PingRequest:  return new PingReqPacket();
			case PingResponse: return new PingRespPacket();
		}

		// TODO not runtime exception?
		throw new MqttUdpRuntimeException("Unknown pkt type 0x"+Integer.toHexString(packetType.value));
	}

//	public void sendToAll() throws IOException {
//		GenericPacket pkt = toPacket();
//		pkt.send();
//	}
//
//	public void sendTo(InetAddress addr) throws IOException {
//		GenericPacket pkt = toPacket();
//		pkt.send( addr );
//	}

}
