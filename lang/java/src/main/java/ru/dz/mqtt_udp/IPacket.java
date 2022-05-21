package ru.dz.mqtt_udp;

import ru.dz.mqtt_udp.io.IPacketAddress;
import ru.dz.mqtt_udp.packets.PacketType;

import java.io.IOException;

/**
 * Interface of general MQTT/UDP packet.
 * @author dz
 *
 */
public interface IPacket {

	interface Writer {
		void write(IPacket packet) throws IOException;
	}

	interface Reader {
		IPacket read() throws IOException;
	}

	interface IO extends Reader, Writer {}

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
	PacketType getType();

	/**
	 * Packet with valid signature?
	 * @return true if it has valid digital signature.
	 */
	boolean isSigned();
	
	
}
