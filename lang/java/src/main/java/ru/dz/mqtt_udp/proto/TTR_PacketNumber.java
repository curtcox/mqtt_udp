package ru.dz.mqtt_udp.proto;

import java.util.concurrent.atomic.AtomicInteger;

public final class TTR_PacketNumber extends TTR_AbstractInteger32 {

	private final static byte myTag = (byte)'n'; 
	
	//private final int packetNumber;
	private static AtomicInteger next = new AtomicInteger( (int)(System.currentTimeMillis() & 0xFFFFFFFF) );
	
	public TTR_PacketNumber(byte tag, byte[] rec, int rawLength) 
	{
		super(tag, rec, rawLength);
	}

	public TTR_PacketNumber( int number )
	{
		super( myTag, number);
	}

	/** use next sequential number */
	public TTR_PacketNumber()
	{
		super( myTag, next.incrementAndGet() );
	}
	

	
	@Override
	public String toString() {
		return String.format("TTR PacketNumber %d", getValue());
	}


}
