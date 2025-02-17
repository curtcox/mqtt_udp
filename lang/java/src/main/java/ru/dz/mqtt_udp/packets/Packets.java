package ru.dz.mqtt_udp.packets;

import ru.dz.mqtt_udp.*;
import ru.dz.mqtt_udp.hmac.HMAC;
import ru.dz.mqtt_udp.io.DatagramSocketIO;
import ru.dz.mqtt_udp.io.IPacketAddress;
import ru.dz.mqtt_udp.io.PacketInputStreamReader;
import ru.dz.mqtt_udp.io.PacketOutputStreamWriter;
import ru.dz.mqtt_udp.proto.TTR_PacketNumber;
import ru.dz.mqtt_udp.proto.TTR_Signature;
import ru.dz.mqtt_udp.proto.TaggedTailRecord;
import ru.dz.mqtt_udp.util.*;

import java.io.IOException;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Static methods useful for dealing with packets.
 */
public final class Packets {

    public static final IPacket.Reader in = new PacketInputStreamReader(System.in);
    public static final IPacket.Writer out = new PacketOutputStreamWriter(System.out);
    public static final IPacket.IO io = new IPacket.IO() {
        @Override public IPacket read() throws IOException { return in.read(); }
        @Override public void write(IPacket packet) throws IOException { out.write(packet); }
    };
    public static final IPacket.IO net = DatagramSocketIO.newInstance();

    /**
     * Construct packet object from binary data (recvd from net).
     *
     * @param raw binary data from UDP packet
     * @param from source address
     * @return Packet object
     * @throws MqttProtocolException on incorrect binary packet data
     */
    public static IPacket fromBytes(Bytes raw, IPacketAddress from ) throws MqttProtocolException {
        int total_len = 0;
        int headerEnd = 1;

        while(true) {
            byte b = raw.at(headerEnd++);
            total_len |= b & ~0x80;

            if( (b & 0x80) == 0 )
                break;

            total_len <<= 7;
        }

        // total_len is length of classic MQTT packet's payload,
        // not including type byte & length field

        int recvLen = raw.length - headerEnd;

        // recvLen is all the packet size minus header

        Collection<TaggedTailRecord> ttrs = null;

        if( recvLen < total_len)
            throw new MqttProtocolException("packet decoded size ("+total_len+") > packet length ("+recvLen+")");

        // We have bytes after classic MQTT packet, must be TTRs, decode 'em
        if (recvLen > total_len) {
            ttrs = decodeTTRs(raw, total_len, headerEnd, recvLen);
        }

        byte[] sub = new byte[total_len];
        System.arraycopy(raw, headerEnd, sub, 0, total_len);

        int ptype = 0xF0 & (int)(raw.at(0));
        Flags flags = new Flags(0x0F & (int)(raw.at(0)));

        return packet(ptype,new Bytes(sub),flags,from,ttrs);
    }

    private static GenericPacket packet(int ptype,Bytes sub,Flags flags,IPacketAddress from,Collection<TaggedTailRecord> ttrs) throws MqttProtocolException {
        switch (ptype) {
            case mqtt_udp_defs.PTYPE_PUBLISH:   return new PublishPacket(flags, Topic.from(sub), from, sub);
            case mqtt_udp_defs.PTYPE_PUBACK:    return PubAckPacket.from(sub, flags, from);
            case mqtt_udp_defs.PTYPE_PINGREQ:   return new PingReqPacket(flags, from);
            case mqtt_udp_defs.PTYPE_PINGRESP:  return new PingRespPacket(flags, from);
            case mqtt_udp_defs.PTYPE_SUBSCRIBE: return new SubscribePacket(Topic.from(sub), flags, from);
        }
        throw new MqttProtocolException("Unknown pkt type " + ptype);
    }

    static Collection<TaggedTailRecord> decodeTTRs(Bytes raw, int total_len, int headerEnd, int recvLen)
            throws MqttProtocolException
    {
        Collection<TaggedTailRecord> ttrs;
        int tail_len = recvLen - total_len; // TTRs size
        byte [] ttrs_bytes = new byte[tail_len];
        //recvLen = total_len; // TODO why not just use total_len below?

        // Get a copy of all TTRs
        System.arraycopy(raw, total_len+headerEnd, ttrs_bytes, 0, tail_len);

        // Decode them all, noting where signature TTR starts
        AtomicReference<Integer> signaturePos = new AtomicReference<Integer>(-1);
        ttrs = TaggedTailRecord.fromBytesAll(ttrs_bytes, signaturePos);

        // If we have signature TTR in packet, check it
        int sigPos = signaturePos.get();

        // We are in strict sig cjeck mode?
        if( (sigPos < 0) && Engine.isSignatureRequired() )
            throw new MqttProtocolException("Unsigned packet");

        // Have signature - check it
        if(sigPos >= 0) {
            // fromBytesAll() calcs signature position in ttrs_bytes, add preceding parts lengths
            sigPos += total_len;
            sigPos += headerEnd;

            // ------------------------------------------------------------
            // We have signature in packet we got, and we know its position
            // in incoming packet. Calculate ours and check.
            // ------------------------------------------------------------

            // Copy out part of packet that is signed and must be checked
            byte [] sig_check_bytes = new byte[sigPos];
            System.arraycopy(raw, 0, sig_check_bytes, 0, sigPos);

            // Look for the signature TTR
            for( TaggedTailRecord ttr : ttrs ) {
                if (ttr instanceof TTR_Signature) {
                    TTR_Signature ts = (TTR_Signature) ttr;

                    //ByteArray.dumpBytes("his", ts.getSignature());
                    boolean sigCorrect = ts.check(sig_check_bytes, Engine.getSignatureKey());
                    if(!sigCorrect)
                        throw new MqttProtocolException("Incorrect packet signature");
                    break;
                }
            }
        }
        return ttrs;
    }

    /**
     * Decode 2-byte string length.
     * @param pkt Binary packet data.
     * @return Decoded length.
     */
    static int decodeTopicLen(Bytes pkt ) {
        int ret = (pkt.at(0) << 8) | pkt.at(1);
        ret &= 0xFFFF;
        return ret;
    }

    /**
     * Rename to encodePacketHeader?
     *
     * Encode total packet length. Encoded as variable length byte sequence, 7 bits per byte.
     *
     * @param pkt packet payload bytes
     * @param packetType type ( &amp; 0xF0 )
     * @param flags flags
     * @param ttr TTRs to encode to packet
     * @return encoded packet to send to UDP
     */
    static Bytes encodeTotalLength(Bytes pkt, PacketType packetType, Flags flags, AbstractCollection<TaggedTailRecord> ttr, PacketNumber p) {
        byte[] buf = headerBytes(pkt,packetType,flags);
        // Encode in Tagged Tail Records - packet extensions
        return encodeTTR( ttr, Bytes.from(buf,pkt.bytes), p );
    }

    private static byte[] headerBytes(Bytes pkt, PacketType packetType, Flags flags) {

        int data_len = pkt.length;

        byte[] buf = new byte[4]; // can't sent very long packets over UDP, 16 bytes are surely ok
        int bp = 1;

        buf[0] = (byte) ((packetType.value & 0xF0) | (flags.toByte() & 0x0F));

        do {
            byte b = (byte) (data_len % 128);
            data_len /= 128;

            if( data_len > 0 )
                b |= 0x80;

            buf[bp++] = b;
        } while( data_len > 0 );

        return buf;
    }

    /**
     * <p>Have 'classic' MQTT packet at input, extend it with Tagged Tail Records.</p>
     *
     * <p>Add packet number if one is missing. Add signature.</p>
     *
     * @param ttrs Collection of TTRs to add.
     * @param packetBeginning Classic packet.
     * @return Extended packet.
     */
    static Bytes encodeTTR( AbstractCollection<TaggedTailRecord> ttrs, Bytes packetBeginning, PacketNumber packetNumber) {
        ArrayList<byte[]> outs = new ArrayList<>();

        boolean haveNumber = false;

        if( ttrs != null ) {
            for (TaggedTailRecord r : ttrs) {
                if (r instanceof TTR_Signature) {
                    GlobalErrorHandler.handleError(ErrorType.Protocol, "Signature must be generated here");
                    continue;
                }

                if (r instanceof TTR_PacketNumber)
                    haveNumber = true;

                outs.add(r.toBytes());
            }
        }

        // Add packet number to list, if none
        if( !haveNumber ) {
            outs.add(new TTR_PacketNumber(packetNumber.value ).toBytes());
        }

        int totalLen = packetBeginning.length;
        for( byte[] bb : outs ) {
            totalLen += bb.length;
        }

        byte [] presig = new byte[totalLen+TTR_Signature.SIGLEN];

        System.arraycopy(packetBeginning, 0, presig, 0, packetBeginning.length);

        int pos = packetBeginning.length;
        for( byte[] bb : outs ) {
            System.arraycopy(bb, 0, presig, pos, bb.length);
            pos += bb.length;
        }

        byte [] toSign = new byte[totalLen];
        System.arraycopy(presig, 0, toSign, 0, totalLen);

        byte[] signature = HMAC.hmacDigestMD5(toSign, Engine.getSignatureKey());

        TTR_Signature sig = new TTR_Signature(signature);

        byte[] sigBytes = sig.toBytes();
        System.arraycopy( sigBytes, 0, presig, pos, TTR_Signature.SIGLEN);

        return new Bytes(presig);
    }

}
