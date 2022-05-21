package ru.dz.mqtt_udp.servers;

import java.io.IOException;

import ru.dz.mqtt_udp.Engine;
import ru.dz.mqtt_udp.IPacket;
import ru.dz.mqtt_udp.io.IPacketAddress;
import ru.dz.mqtt_udp.packets.PingReqPacket;
import ru.dz.mqtt_udp.packets.PingRespPacket;
import ru.dz.mqtt_udp.packets.PubAckPacket;
import ru.dz.mqtt_udp.packets.PublishPacket;
import ru.dz.mqtt_udp.util.Flags;
import ru.dz.mqtt_udp.util.LoopRunner;


/** 
 * Server loop to listen to network traffic and process incoming packets.
 * @author dz
 *
 */
@Deprecated
public abstract class SubServer extends LoopRunner {

//	private final DatagramSocket ss = SingleSendSocket.get();
//	private DatagramSocket s;
	private final IPacket.IO io;

	public SubServer(IPacket.IO io) {
		super("MQTT UDP Recv");
		this.io = io;
	}

	@Override
	protected void onStart() throws IOException {
//		s = GenericPacket.recvSocket();
	}

	@Override
	protected void step() throws IOException {
		IPacket p = io.read();
		if(!muted) preprocessPacket(p);			
		processPacket(p);			
	}

	@Override
	protected void onStop() {
		//s.close();
	}

	// ------------------------------------------------------------
	// Replies on/off
	// ------------------------------------------------------------

	private boolean muted = false;
	public boolean isMuted() {		return muted;	}
	/** 
	 * Set muted mode. In muted mode server loop won't respond to any incoming packets
	 * (such as PINGREQ) automatically.
	 * 
	 * @param muted If true - mute replies.
	 */
	public void setMuted(boolean muted) {		this.muted = muted;	}


	// ------------------------------------------------------------
	// Incoming data process thread
	// ------------------------------------------------------------

	//volatile private boolean run = false;

	//public boolean isRunning() { return run; }
	//public boolean isRunning() { return lr.isRunning(); }
	//public void requestStart() { lr.requestStart(); }
	//public void requestStop() { lr.requestStop(); }

	/**
	 * Request to start reception loop thread.
	 * /
	public void requestStart()
	{
		if(isRunning()) return;
		start();
	}

	/**
	 * Request to stop reception loop thread.
	 * /
	public void requestStop() { run = false; }

	/**
	 * Worker: start reception loop thread.
	 * /
	protected void start() {
		Runnable target = makeLoopRunnable();
		Thread t = new Thread(target, "MQTT UDP Recv");
		t.start();
	}* /


	private void loop() throws IOException, MqttProtocolException {
		DatagramSocket s = GenericPacket.recvSocket();

		run = true;

		while(run)
		{
			//System.out.print("Listen loop run");
			IPacket p = GenericPacket.recv(s);
			if(!muted) preprocessPacket(p);			
			processPacket(p);			
		}

		s.close();
	} * /


	private Runnable makeLoopRunnable() {
		return new Runnable() {
			@Override
			public void run() {
				try {
					loop();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (MqttProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
			}
		};
	} */



	// ------------------------------------------------------------
	// Packet processing
	// ------------------------------------------------------------


	/**
	 * Must be overridden in children to process packet 
	 * @param p packet to process
	 * @throws IOException if IO error
	 */
	protected abstract void processPacket(IPacket p) throws IOException;


	/** 
	 * Does internal protocol defined packet processing.
	 * @throws IOException 
	 */
	private void preprocessPacket(IPacket p) throws IOException {

		if( p instanceof PingReqPacket) {
			// Reply to ping
			PingRespPacket presp = new PingRespPacket(new byte[0],new Flags(), IPacketAddress.LOCAL);
			//presp.send(ss, ((PingReqPacket) p).getFrom().getInetAddress());
			// decided to broadcast ping replies
			io.write(presp);
		}
		else if( p instanceof PublishPacket) {
			PublishPacket pp = (PublishPacket) p;
			
			int qos = pp.flags.getQoS();
			if( qos != 0 ) {
				System.out.println("QoS, Publish id="+pp.getPacketNumber());
				int maxQos = Engine.getMaxReplyQoS();
				Flags flags = new Flags();
				flags.setQoS(Integer.min(qos, maxQos));
				io.write(new PubAckPacket(new byte[0], flags, IPacketAddress.LOCAL));
			}
		}

	}

}
