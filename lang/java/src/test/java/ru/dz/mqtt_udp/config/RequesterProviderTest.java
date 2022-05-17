package ru.dz.mqtt_udp.config;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

import ru.dz.mqtt_udp.packets.Topic;
import ru.dz.mqtt_udp.servers.PacketSourceMultiServer;

public class RequesterProviderTest {

	private static final Topic T1  = new Topic("test/java/t1");
	private static final String T1V = "test value 1";

	private static final Topic T2  = new Topic("test/java/t3");
	private static final String T2V = "test value 2";

	private static final Topic T3  = new Topic("test/java/t3");
	private static final String T3V = "test value 3";

	private static final String TO = "timeout/";
	

	private static PacketSourceMultiServer multiServer;
	private static Provider provider;
	private static Requester requester;
	
	@BeforeClass
    public static void setUpClass() {
		multiServer = new PacketSourceMultiServer();
		provider = Provider.withPacketsFrom(multiServer);
		requester = Requester.withPacketsFrom(multiServer);
		multiServer.requestStart();
    }

	@Test(timeout=4000)
	public void exchange_with_1_topic() throws IOException {
		provider.addTopic(T1, T1V);
		requester.addTopic(T1);
		assertTrue( requester.waitForAll(1000) );
	}

	@Test(timeout=4000)
	public void exchange_with_3_topics() throws IOException {
		
		provider.addTopic(T1, T1V);
		provider.addTopic(T2, T2V);
		provider.addTopic(T3, T3V);
		
		requester.addTopic(T1);
		requester.addTopic(T2);
		requester.addTopic(T3);
		
		assertTrue( requester.waitForAll(1000) );
	}


	@Test
	public void timeout_with_1_topic() throws IOException {
		requester.setCheckLoopTime(1000);
		requester.startBackgroundRequests();

		// Let requester ask it for the first time before
		// provider is ready to answer. It will work if
		// requester is repeating it's requests
		requester.addTopic(new Topic(TO+T1));

		provider.addTopic(new Topic(TO+T1), T1V);

		assertTrue( requester.waitForAll(4000) );
	}

	@Test
	public void timeout_with_3_topics() throws IOException {
		requester.setCheckLoopTime(1000);
		requester.startBackgroundRequests();
		
		// Let requester ask it for the first time before 
		// provider is ready to answer. It will work if
		// requester is repeating it's requests
		requester.addTopic(new Topic(TO+T1));
		requester.addTopic(new Topic(TO+T2));
		requester.addTopic(new Topic(TO+T3));

		provider.addTopic(new Topic(TO+T1), T1V);
		provider.addTopic(new Topic(TO+T2), T2V);
		provider.addTopic(new Topic(TO+T3), T3V);
		
		assertTrue( requester.waitForAll(4000) );
	}

	
}
