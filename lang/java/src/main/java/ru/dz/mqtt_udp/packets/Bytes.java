package ru.dz.mqtt_udp.packets;

import static ru.dz.mqtt_udp.util.Check.notNull;

public final class Bytes {

	final byte[] bytes;
	final String name;

	public Bytes(byte[] bytes, String name) {
		this.bytes = notNull(bytes);
		this.name = notNull(name);
	}

	public Bytes(byte[] bytes) {
		this(bytes,Thread.currentThread().getStackTrace()[0].toString());
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

}
