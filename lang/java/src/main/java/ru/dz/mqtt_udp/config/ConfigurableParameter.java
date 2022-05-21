package ru.dz.mqtt_udp.config;

import ru.dz.mqtt_udp.io.IPacketAddress;
import ru.dz.mqtt_udp.packets.Bytes;
import ru.dz.mqtt_udp.packets.PublishPacket;
import ru.dz.mqtt_udp.packets.Topic;
import ru.dz.mqtt_udp.packets.Flags;
import ru.dz.mqtt_udp.util.mqtt_udp_defs;

/**
 * 
 * Parameter of some device that can be configured remotely.
 * 
 * 
 * @author dz
 *
 */
public class ConfigurableParameter implements Comparable<ConfigurableParameter> {
	
	private final ConfigurableHost host;
	private final String kind;
	private final String name;
	private String value;

	public ConfigurableParameter( ConfigurableHost host, String kind, String name, String value) {
		this.host = host;
		this.kind = kind;
		this.name = name;
		this.value = value;
	}
	
	
	@Override
	public String toString() {
		return String.format("%s: %s '%s'='%s'", host, kind, name, value );
	}
	
	@Override
	public boolean equals(Object obj) {
		//System.out.println("equals "+toString());
		if( obj == null ) return false;
		
		//System.out.println("equals "+toString()+" == "+obj.toString());
		
		if(!(obj instanceof ConfigurableParameter))
			return false;
		
		ConfigurableParameter cp = (ConfigurableParameter) obj;
			
		return 
				cp.host.equals(host) 
				&& cp.kind.equals(kind) 
				&& cp.name.equals(name) 
				//&& cp.value.equals(value)
				;
	}

	@Override
	public int hashCode() {
		return host.hashCode() + kind.hashCode() + name.hashCode();
	}

	@Override
	public int compareTo(ConfigurableParameter cp) {
		int cmp;
		
		if( cp == null ) return 1;
		
		cmp = host.compareTo(cp.host);
		if( cmp != 0 ) return cmp;
		
		cmp = kind.compareTo(cp.kind);
		if( cmp != 0 ) return cmp;

		cmp = name.compareTo(cp.name);
		//if( cmp != 0 ) return cmp;

		//cmp = value.compareTo(cp.value);
		
		return cmp;
	}


	public ConfigurableHost getConfigurableHost() { return host; }
	public String getKind() { return kind; }
	public String getName() { return name; }
	public String getValue() { return value; }

	private Topic makeTopicName() {
		return new Topic(String.format(
				"%s/%s/%s/%s", 
				mqtt_udp_defs.SYS_CONF_PREFIX,
				host.getMacAddressString(),
				kind, name
				));
	}
	
	private Topic fullTopcNameCache = null;
	public Topic getTopicName() {
		if(null == fullTopcNameCache) {
			synchronized (this) {
				if(null == fullTopcNameCache)
					fullTopcNameCache = makeTopicName();
			}
		}
		return fullTopcNameCache;
	}


	public PublishPacket sendCurrentValue() {
		Topic topic = getTopicName();

		return new PublishPacket(new Flags(), topic, IPacketAddress.LOCAL, Bytes.from(value));
	}


	public boolean topicIs(Topic topic) {
		return getTopicName().equals(topic);
	}


	public void setValue(String v) {
		value = v;
	}
}
