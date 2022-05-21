package ru.dz.mqtt_udp.io;

import ru.dz.mqtt_udp.IPacket;
import ru.dz.mqtt_udp.MqttProtocolException;
import ru.dz.mqtt_udp.packets.Packets;
import ru.dz.mqtt_udp.util.mqtt_udp_defs;

import java.io.IOException;
import java.net.*;

import static ru.dz.mqtt_udp.util.Check.notNull;

public final class DatagramSocketIO implements IPacket.IO {

    final DatagramSocket inputSocket;
    final DatagramSocket outputSocket;

    private int sentCounter = 0;

    /**
     * Broadcast IP address.
     */
    private static final byte[] broadcast =  { (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF } ;

    private static final int PORT = mqtt_udp_defs.MQTT_PORT;

//    private InetAddress resendAddress;

    //    final public int getSentCounter() {		return sentCounter;	}
//
    DatagramSocketIO(DatagramSocket inputSocket, DatagramSocket outputSocket) {
        this.inputSocket = notNull(inputSocket);
        this.outputSocket = notNull(outputSocket);
    }

    /**
     * Create new socket to listen to MQTT/UDP packets.
     * @return Created socket.
     * @throws SocketException If unable.
     */
    public static DatagramSocketIO newInstance() {
        try {
            DatagramSocketIO io = new DatagramSocketIO(new DatagramSocket(null),new DatagramSocket(null));
            io.recvSocket();
            return io;
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }

    void recvSocket() throws SocketException {
        //s.setBroadcast(true);

        inputSocket.setReuseAddress(true);
        // TODO reuseport

        InetSocketAddress address = new InetSocketAddress(mqtt_udp_defs.MQTT_PORT);

        inputSocket.bind(address);
    }

//    /**
//     * Broadcast me using default send socket.
//     * @throws IOException If unable.
//     */
//    final public void send() throws IOException {
//        send(SingleSendSocket.get());
//    }
//
//    /**
//     * Send me using default send socket.
//     * @param addr Where to send to.
//     * @throws IOException If unable.
//     */
//    final public void send(InetAddress addr) throws IOException {
//        send(SingleSendSocket.get(),addr);
//    }

//    /**
//     * Broadcast me using given socket.
//     * @param sock Socket must be made with sendSocket() method.
//     * @throws IOException If unable.
//     */
//    final public void send(DatagramSocket sock) throws IOException {
//        send( sock, InetAddress.getByAddress(broadcast) );
//    }

//    /**
//     * Send me to given address.
//     * @param sock Socket must be made with sendSocket() method.
//     * @param address Host to send to
//     * @throws IOException If unable.
//     */
//    final public void send(DatagramSocket sock, InetAddress address) throws IOException {
//        byte[] pkt = toBytes();
//
//        DatagramPacket packet = new DatagramPacket(pkt, pkt.length, address, mqtt_udp_defs.MQTT_PORT);
//        Engine.throttle();
//        sock.send(packet);
//        sentCounter++;
//        //System.out.println("UDP sent "+pkt.length);
//        resendAddress = address;
//        if( flags.getQoS() != 0 ) Engine.queueForResend(this);
//    }

//    /**
//     * Resend me to last used address.
//     * @param sock Socket must be made with sendSocket() method.
//     * @throws IOException If unable.
//     */
//    final public void resend(DatagramSocket sock) throws IOException {
//        byte[] pkt = toBytes();
//
//        DatagramPacket p = new DatagramPacket(pkt, pkt.length, resendAddress, mqtt_udp_defs.MQTT_PORT);
//        sock.send(p);
//        sentCounter++;
//        //System.out.println("UDP resent "+pkt.length);
//    }

    /**
     * Wait for packet to come in.
     * @param s Socket to use.
     * @return Packet received.
     * @throws SocketException As is.
     * @throws IOException As is.
     * @throws MqttProtocolException What we got is not a valid MQTT/UDP packet.
     */
    private static IPacket recv(DatagramSocket s) throws IOException {
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
    public void write(IPacket packet) throws IOException {
        outputSocket.send(datagram(packet));
    }

    private DatagramPacket datagram(IPacket packet) {
        byte[] bytes = packet.toBytes();
        return new DatagramPacket(bytes, bytes.length, packet.getFrom().getInetAddress(), PORT);
    }

    @Override
    public IPacket read() throws IOException {
        // some embedded systems can't fragment UDP and
        // fragmented UDP is highly unreliable anyway, so it is
        // better to stick to MAC layer max packet size

        byte[] buf = new byte[2*1024];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);

        inputSocket.receive(packet);

        int l = packet.getLength();

        byte[] got = new byte[l];

        System.arraycopy(packet.getData(), packet.getOffset(), got, 0, l);

        return Packets.fromBytes(got, IpAddress.from(packet));
    }
}
