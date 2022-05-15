package ru.dz.mqtt_udp.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.function.Consumer;

import ru.dz.mqtt_udp.Engine;
import ru.dz.mqtt_udp.IPacket;
import ru.dz.mqtt_udp.IPacketMultiSource;
import ru.dz.mqtt_udp.servers.PacketSourceMultiServer;
import ru.dz.mqtt_udp.packets.PublishPacket;
import ru.dz.mqtt_udp.packets.SubscribePacket;
import ru.dz.mqtt_udp.TopicFilter;
import ru.dz.mqtt_udp.util.ErrorType;
import ru.dz.mqtt_udp.util.GlobalErrorHandler;
import ru.dz.mqtt_udp.util.LoopRunner;
import ru.dz.mqtt_udp.util.mqtt_udp_defs;

/**
 * 
 * <p>Passive remote configuration controllable node.</p>
 * 
 * <p>
 * This class serves configuration items to be remotely configurable.
 * </p>
 * <p>See also lang/c/mqtt_udp_rconfig.c - C implementation of same functionality.</p>
 * <p>See also <a href="https://github.com/dzavalishin/mqtt_udp/wiki/MQTT-UDP-message-content-specification">Wiki</a></p>
 * 
 * @see Controller
 * 
 * @author dz
 *
 */
public final class RemoteConfig implements Consumer<IPacket> {
	private LoopRunner lr = new RemoteConfigLoopRunner() {
		@Override
		protected void step() {
			//new SubscribePacket(SYS_CONF_WILD).send();
			sleep(30L*1000L);
			//sleep(2L*1000L);
		}
	};
	//private String macAddress;
	private Collection<ConfigurableParameter> items; 


	//public RemoteConfig( IPacketMultiSource ms, String macAddress, Collection<ConfigurableParameter> items ) {
	public RemoteConfig( IPacketMultiSource ms, Collection<ConfigurableParameter> items ) {
		//this.macAddress = macAddress;
		this.items = items;
		ms.addPacketSink(this);	
	}

	public void requestStart() {
		lr.requestStart();
	}



	private final static String SYS_CONF_WILD = mqtt_udp_defs.SYS_CONF_PREFIX+"/#";
	private TopicFilter rf = new TopicFilter(SYS_CONF_WILD);
	private String fName;

	/**
	 *  Incoming packet
	 * 
	 */
	@Override
	public void accept(IPacket p) {
		if (p instanceof SubscribePacket) {
			SubscribePacket sp = (SubscribePacket) p;

			if( rf.test(sp.getTopic()) ) {
				sendAllConfigurableTopics();
				return;
			}

			// possible request for some specific one
			sendConfigurableTopic(sp.getTopic());

		}

		if (p instanceof PublishPacket) {
			PublishPacket pp = (PublishPacket) p;

			setLocalValue( pp );
		}		
	}

	private void setLocalValue(PublishPacket pp) {
		items.forEach( item -> {
			if( item.topicIs(pp.getTopic()) )
				item.setValue(pp.getValueString());
		} );
	}

	private void sendConfigurableTopic(String topic) {
		items.forEach( item -> {
			if( item.topicIs(topic))
				item.sendCurrentValue();
		} );
	}

	private void sendAllConfigurableTopics() 
	{
		items.forEach( item -> item.sendCurrentValue() );		
	}



	private Properties props = new Properties();
	public void setPropertiesFileName( String fName )
	{
		this.fName = fName;		
	}


	public void loadFromProperties()
	{
		FileInputStream inStream;
		try {
			inStream = new FileInputStream(new File(fName));
		} catch (FileNotFoundException e) {
			GlobalErrorHandler.handleError(ErrorType.IO, e);
			return;
		}
		
		try {
			props.load(inStream);
			props.forEach( (name,val) ->{
				String[] names = name.toString().split("/");

				items.forEach( item -> {
					if( 
							item.getKind().equals(names[0]) &&
							item.getName().equals(names[1])
							)
						item.setValue(val.toString());
				});
			});
		} catch (IOException e) {
			GlobalErrorHandler.handleError(ErrorType.IO, e);
		}		
		finally
		{
			try {
				inStream.close();
			} catch (IOException e) {
				GlobalErrorHandler.handleError(ErrorType.IO, e);
			}
		}
	}


	public void saveToProperties()
	{
		items.forEach( item -> {
			if(!(item instanceof LocalReadOnlyParameter)) 
			{				
				String key = item.getKind()+"/"+item.getName();
				props.setProperty(key, item.getValue());
			}
		} );

		FileOutputStream out;
		try {
			out = new FileOutputStream(new File(fName));
		} catch (FileNotFoundException e) {
			GlobalErrorHandler.handleError(ErrorType.IO, e);
			return;
		}

		
		try {
			props.store(out, "MQTT/UDP remote config storage");
		} catch (IOException e) {
			GlobalErrorHandler.handleError(ErrorType.IO, e);
		} finally 
		{
			try {
				out.close();
			} catch (IOException e) {
				GlobalErrorHandler.handleError(ErrorType.IO, e);
			}
		}
	}


	static Set<ConfigurableParameter> itemList() {
		Set<ConfigurableParameter> itemList = new HashSet<>();

		//ConfigurableHost ch = new ConfigurableHost(mac, null );
		//itemList.add(new ConfigurableParameter(ch, "topic", "test1", "Trigger"));

		itemList.add(new LocalReadOnlyParameter( "info", "soft", "Tray Informer") );
		itemList.add(new LocalReadOnlyParameter( "info", "ver", Engine.getVersionString()) );
		itemList.add(new LocalReadOnlyParameter( "info", "uptime", "?") );

		itemList.add(new LocalConfigurableParameter( "node", "name", "Tray Informer") );
		itemList.add(new LocalConfigurableParameter( "node", "location", "Desk PC") );

		itemList.add(new LocalConfigurableParameter( "topic", "test1", "Trigger") );

		return itemList;
	}

	static void start() {
		PacketSourceMultiServer server = new PacketSourceMultiServer();
		RemoteConfig config = new RemoteConfig(server, itemList());

		server.requestStart();
		config.requestStart();

		config.setPropertiesFileName("remoteconf.prop");
		config.saveToProperties();
	}


	public static void main(String[] args) {
		start();
	}




}
