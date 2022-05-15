package ru.dz.mqtt_udp;

import ru.dz.mqtt_udp.io.IPacketAddress;

/**
 * Interface of general MQTT/UDP packet.
 * @author dz
 *
 */
public interface IPacket {

	/** MQTT/UDP character set */
	String MQTT_CHARSET = "UTF-8";

	/**
	 * Generate network representation of packet to be sent.
	 * @return UDP packet contents.
	 */
	byte[] toBytes();

	/**
	 * Get packet sender address.
	 * @return IP address.
	 */
	IPacketAddress getFrom();

	/**
	 * Get packet type byte, as sent over the net (&amp; 0xF0).
	 * @return Packet type byte.
	 */
	int getType();

	String[] pTYpeNames = {
			"? NULL",
			"Connect",
			"ConnAck",
			"Publish",
			"PubAck",
			"PubRec",
			"PubRel",
			"PubComp",
			"Subscribe",
			"SubAck",
			"UnSubscribe",
			"UnSubAck",
			"PingReq",
			"PingResp",
			"Disconnect",
			"? 0xFF",
	};

	/**
	 * Packet with valid signature?
	 * @return true if it has valid digital signature.
	 */
	boolean isSigned();
	
	
}
