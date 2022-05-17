package ru.dz.mqtt_udp.packets;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.Collection;
import java.util.Optional;

import ru.dz.mqtt_udp.Engine;
import ru.dz.mqtt_udp.IPacket;
import ru.dz.mqtt_udp.MqttProtocolException;
import ru.dz.mqtt_udp.io.IPacketAddress;
import ru.dz.mqtt_udp.io.IpAddress;
import ru.dz.mqtt_udp.io.SingleSendSocket;
import ru.dz.mqtt_udp.proto.TTR_PacketNumber;
import ru.dz.mqtt_udp.proto.TTR_ReplyTo;
import ru.dz.mqtt_udp.proto.TTR_Signature;
import ru.dz.mqtt_udp.proto.TaggedTailRecord;
import ru.dz.mqtt_udp.util.ErrorType;
import ru.dz.mqtt_udp.util.Flags;
import ru.dz.mqtt_udp.util.GlobalErrorHandler;
import ru.dz.mqtt_udp.util.mqtt_udp_defs;

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
	private Flags flags = new Flags();

	/**
	 * Packet source address, if packet is received from net.
	 * Locally created ones have LOCAL here.
	 */
	private IPacketAddress from;

	private InetAddress resendAddress;

	private int sentCounter = 0;

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

	/**
	 * Create new socket to listen to MQTT/UDP packets.
	 * @return Created socket.
	 * @throws SocketException If unable.
	 */
	public static DatagramSocket recvSocket() throws SocketException {
		return recvSocket(new DatagramSocket(null));
	}

	static DatagramSocket recvSocket(DatagramSocket s) throws SocketException {
		//s.setBroadcast(true);

		s.setReuseAddress(true);
		// TODO reuseport

		InetSocketAddress address = new InetSocketAddress(mqtt_udp_defs.MQTT_PORT);

		s.bind(address);
		return s;
	}

	/**
	 * Broadcast me using default send socket.
	 * @throws IOException If unable.
	 */
	final public void send() throws IOException {
		send(SingleSendSocket.get());
	}
	
	/**
	 * Send me using default send socket.
	 * @param addr Where to send to.
	 * @throws IOException If unable.
	 */
	final public void send(InetAddress addr) throws IOException {
		send(SingleSendSocket.get(),addr);
	}
	
	/**
	 * Broadcast me using given socket. 
	 * @param sock Socket must be made with sendSocket() method.
	 * @throws IOException If unable.
	 */
	final public void send(DatagramSocket sock) throws IOException {
		send( sock, InetAddress.getByAddress(broadcast) );
	}

	/**
	 * Send me to given address. 
	 * @param sock Socket must be made with sendSocket() method.
	 * @param address Host to send to
	 * @throws IOException If unable.
	 */
	final public void send(DatagramSocket sock, InetAddress address) throws IOException {
		byte[] pkt = toBytes();
		
		DatagramPacket packet = new DatagramPacket(pkt, pkt.length, address, mqtt_udp_defs.MQTT_PORT);
		Engine.throttle();
		sock.send(packet);
		sentCounter++;
		//System.out.println("UDP sent "+pkt.length);
		resendAddress = address;
		if( flags.getQoS() != 0 ) Engine.queueForResend(this);
	}


	/**
	 * Resend me to last used address. 
	 * @param sock Socket must be made with sendSocket() method.
	 * @throws IOException If unable.
	 */
	final public void resend(DatagramSocket sock) throws IOException {
		byte[] pkt = toBytes();
		
		DatagramPacket p = new DatagramPacket(pkt, pkt.length, resendAddress, mqtt_udp_defs.MQTT_PORT);
		sock.send(p);
		sentCounter++;
		//System.out.println("UDP resent "+pkt.length);
	}

	/**
	 * Wait for packet to come in.
	 * @param s Socket to use.
	 * @return Packet received.
	 * @throws SocketException As is.
	 * @throws IOException As is. 
	 * @throws MqttProtocolException What we got is not a valid MQTT/UDP packet.
	 */
	public static IPacket recv(DatagramSocket s) throws IOException, MqttProtocolException {
		// some embedded systems can't fragment UDP and
		// fragmented UDP is highly unreliable anyway, so it is 
		// better to stick to MAC layer max packet size 
		
		byte[] buf = new byte[2*1024];  
		DatagramPacket packet = new DatagramPacket(buf, buf.length);
		
		s.receive(packet);

		int l = packet.getLength();
		
		byte[] got = new byte[l];  
		
		System.arraycopy(packet.getData(), packet.getOffset(), got, 0, l);
		
		return Packets.fromBytes(got, IpAddress.from(packet));
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

	final public int getSentCounter() {		return sentCounter;	}

	private int ackCount = 0; 
	/**
	 * Increment counter of ACKs we got for this packet
	 */
	final public void incrementAckCount() {
		ackCount++;
	}

	final public int getAckCount() {	return ackCount;	}

}
