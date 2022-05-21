package ru.dz.mqtt_udp.packets;

import ru.dz.mqtt_udp.IPacket;

import java.io.UnsupportedEncodingException;

import static ru.dz.mqtt_udp.util.Check.notNull;

/**
 * An immutable byte array.
 */
public final class Bytes {

	public final byte[] bytes;

	public final String name;

	public final int length;

	public Bytes(byte[] bytes, String name) {
		this.bytes = notNull(bytes);
		this.name = notNull(name);
		this.length = bytes.length;
	}

	public Bytes() {
		this(new byte[0],Thread.currentThread().getStackTrace()[0].toString());
	}

	public Bytes(byte[] bytes) {
		this(bytes,Thread.currentThread().getStackTrace()[0].toString());
	}

	public static Bytes from(String value) {
		try {
			return new Bytes(value.getBytes(IPacket.MQTT_CHARSET));
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	public void dump() {
		System.err.println(this);
	}

	@Override
	public String toString() {
		StringBuilder out = new StringBuilder();

		out.append(name + ", len = " + bytes.length);

		int p = 0;

		while( p < bytes.length ) {
			if( (p % 16) == 0 )
				out.append(" ");

			byte cb = bytes[p++];

			out.append( String.format("%02X ", cb) );
		}


		out.append(" ");
		out.append("--");
		return out.toString();
	}

	public byte at(int i) {
		return bytes[i];
	}
}
