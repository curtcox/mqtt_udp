package ru.dz.mqtt_udp;

import static org.junit.Assert.*;

import org.junit.Test;

import ru.dz.mqtt_udp.TopicFilter;
import ru.dz.mqtt_udp.packets.Topic;

/**
 * Test for topic filtering.
 * @author dz
 *
 */
public class TopicFilterTest {

	@Test
	public void testPlain() {
		TopicFilter tf = new TopicFilter("aaa/ccc/bbb");
		assertTrue( tf.test(new Topic("aaa/ccc/bbb") ));
		assertFalse( tf.test(new Topic("aaa/c/bbb")) );
		assertFalse( tf.test(new Topic("aaa/ccccc/bbb")) );
		assertFalse( tf.test(new Topic("aaa/ccccc/ccc") ));
	}

	@Test
	public void testPlus() {
		TopicFilter tf = new TopicFilter("aaa/+/bbb");
		assertTrue( tf.test(new Topic("aaa/ccc/bbb")) );
		assertTrue( tf.test(new Topic("aaa/c/bbb") ));
		assertTrue( tf.test(new Topic("aaa/ccccc/bbb")) );
		assertFalse( tf.test(new Topic("aaa/ccccc/ccc")) );
	}

	@Test
	public void testSharp() {
		TopicFilter tf = new TopicFilter("aaa/#");
		assertTrue( tf.test(new Topic("aaa/ccc/bbb")) );
		assertTrue( tf.test(new Topic("aaa/c/bbb") ));
		assertTrue( tf.test(new Topic("aaa/ccccc/bbb")) );
		assertFalse( tf.test(new Topic("aba/ccccc/ccc")) );
	}
	
	
}
