package ru.dz.mqtt_udp.items;

import ru.dz.mqtt_udp.IPacket;
import ru.dz.mqtt_udp.packets.*;

import static ru.dz.mqtt_udp.packets.PacketType.*;

public final class Items {

    public static TopicItem fromPacket( IPacket p ) {
        if (p instanceof PublishPacket)   return publish((PublishPacket) p);
        if( p instanceof SubscribePacket) return subscribe((SubscribePacket) p);
        if( p instanceof PingReqPacket)   return pingRequest((PingReqPacket) p);
        if( p instanceof PingRespPacket)  return pingResponse((PingRespPacket) p);
        return unknown(p);
    }

    static TopicItem from(TopicItem item,IPacket p) {
        item.setFrom(p.getFrom().toString());
        item.setSigned( p.isSigned() );
        return item;
    }

    static TopicItem publish(PublishPacket p) {
        return from(new TopicItem( p.getType(), p.getTopic(), p.getValueString() ),p);
    }

    static TopicItem subscribe( SubscribePacket p ) {
        return from(new TopicItem(Subscribe, p.getTopic() ),p);
    }

    static TopicItem pingRequest(PingReqPacket p) {
        return from(new TopicItem(PingRequest),p);
    }

    static TopicItem pingResponse(PingRespPacket p ) {
        return from(new TopicItem(PingResponse),p);
    }

    static TopicItem unknown( IPacket p ) {
        System.out.println(p);
        // TODO hack
        return from(new TopicItem( Unknown, Topic.UnknownPacket, p.toString()),p);
    }

}
