package ru.dz.mqtt_udp.packets;

import java.util.Collection;
import java.util.Optional;

import ru.dz.mqtt_udp.IPacket;
import ru.dz.mqtt_udp.io.IPacketAddress;
import ru.dz.mqtt_udp.proto.TTR_PacketNumber;
import ru.dz.mqtt_udp.proto.TTR_ReplyTo;
import ru.dz.mqtt_udp.proto.TTR_Signature;
import ru.dz.mqtt_udp.proto.TaggedTailRecord;
import ru.dz.mqtt_udp.util.ErrorType;
import ru.dz.mqtt_udp.util.Flags;
import ru.dz.mqtt_udp.util.GlobalErrorHandler;

import static ru.dz.mqtt_udp.util.Check.notNull;

/**
 * Network IO work horse for MQTT/UDP packets.
 * @author dz
 *
 */

public abstract class GenericPacket implements IPacket {

	/**
	 * Packet header flags.
	 */
	private final Flags flags;

	/**
	 * Packet source address, if packet is received from net.
	 * Locally created ones have LOCAL here.
	 */
	private final IPacketAddress from;

	/** 
	 * Broadcast IP address.
	 */
	private static final byte[] broadcast =  { (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF } ;

	/**
	 * Construct packet from network.
	 * @param from Sender's address.
	 */
	protected GenericPacket(Flags flags,IPacketAddress from) {
		this.flags = notNull(flags);
		this.from = notNull(from);
	}

	@Override
	final public IPacketAddress getFrom() { return from; }
	
	final public Flags getFlags() {		return flags;	}

	
	@Override
	public String toString() {
		return String.format("MQTT/UDP packet of unknown type from '%s', please redefine toString in %s", from, getClass().getName());
	}


	/**
	 * <p>
	 * <b>Internal use only.</b>
	 * </p>
	 * <p>
	 * Apply data from TTRs to constructed packet.
	 * </p>
	 * @param ttrs Tagged Tail Records to apply
	 * @return Self
	 */
	final public IPacket applyTTRs(Collection<TaggedTailRecord> ttrs) {
		if( ttrs == null )
			return this;
		
		for( TaggedTailRecord ttr : ttrs )
			applyTTR(ttr);
		
		return this;
	}

	private void applyTTR(TaggedTailRecord ttr) {
		if (ttr instanceof TTR_Signature) {
			; // just ignore, checked outside
			setSigned( true );
		}
		
		else if (ttr instanceof TTR_PacketNumber) {
			TTR_PacketNumber t = (TTR_PacketNumber) ttr;			
			setPacketNumber( t.getValue() );
		}
		
		else if (ttr instanceof TTR_ReplyTo) {
			TTR_ReplyTo r = (TTR_ReplyTo) ttr;			
			setReplyToPacketNumber( r.getValue() );
		}
		
		else 
		{
			GlobalErrorHandler.handleError(ErrorType.Protocol, "Unknown TTR: "+ttr);
		}
	}
	

	private Optional<Integer> replyToPacketNumber = Optional.empty();

	final public Optional<Integer> getReplyToPacketNumber() {
		return replyToPacketNumber;
	}

	final public void setReplyToPacketNumber( int replyToPacketNumber ) {
		this.replyToPacketNumber = Optional.ofNullable( replyToPacketNumber );
	}
	
	private Optional<Integer> packetNumber = Optional.empty();

	final public Optional<Integer> getPacketNumber() {
		return packetNumber;
	}

	final public void setPacketNumber(int packetNumber) {
		this.packetNumber = Optional.ofNullable(packetNumber);
	}

	private boolean signed = false;

	@Override
	final public boolean isSigned() {		return signed;	}

	private void setSigned(boolean signed) {		this.signed = signed;	}

	private int ackCount = 0; 
	/**
	 * Increment counter of ACKs we got for this packet
	 */
	final public void incrementAckCount() {
		ackCount++;
	}

	final public int getAckCount() {	return ackCount;	}

}
