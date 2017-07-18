package net.portly.mqtt.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class MQTTOptionsHelperTest {

	@Test
	public void checkForNullArgs() {
		MQTTOptionsHelper h = new MQTTOptionsHelper();
		h.processArgs(null);
	}

	@Test
	public void checkForArgsWithNoHypens() {
		MQTTOptionsHelper h = new MQTTOptionsHelper();
		MQTTOptions res = h.processArgs(new String[] { "a", "b", "c" });

		assertNull(res.getBrokerURL());
		assertNull(res.getClientID());
		assertFalse(res.isDebug());
		assertNull(res.getTopic());
	}

	@Test
	public void checkForArgsWithJustDBURL() {
		MQTTOptionsHelper h = new MQTTOptionsHelper();
		MQTTOptions res = h.processArgs(new String[] { "-s", "/home/ian/cpd/test.db" });

		assertNull(res.getBrokerURL());
		assertNull(res.getClientID());
		assertFalse(res.isDebug());
		assertNull(res.getTopic());
		assertEquals("/home/ian/cpd/test.db", res.getDbURL());
	}

	@Test
	public void checkForOneArgWithHypensButNoCorrectValueAndOneWithout() {
		MQTTOptionsHelper h = new MQTTOptionsHelper();
		MQTTOptions res = h.processArgs(new String[] { "a", "-b" });

		assertNull(res.getBrokerURL());
		assertNull(res.getClientID());
		assertFalse(res.isDebug());
		assertNull(res.getTopic());
	}

	@Test
	public void checkForOneArgWithHypensAndAValueAndOneWithout() {
		MQTTOptionsHelper h = new MQTTOptionsHelper();
		MQTTOptions res = h.processArgs(new String[] { "a", "-b", "XXX" });

		assertEquals("XXX", res.getBrokerURL());
		assertNull(res.getClientID());
		assertFalse(res.isDebug());
		assertNull(res.getTopic());
	}

	@Test
	public void checkForBrokerDebugAndTopic() {
		MQTTOptionsHelper h = new MQTTOptionsHelper();
		MQTTOptions res = h.processArgs(new String[] { "-b", "XXX", "-d", "-t", "ZZZ" });

		assertEquals("XXX", res.getBrokerURL());
		assertNull(res.getClientID());
		assertTrue(res.isDebug());
		assertEquals("ZZZ", res.getTopic());
	}

	@Test
	public void checkForBrokerWithPortSpecified() {
		MQTTOptionsHelper h = new MQTTOptionsHelper();
		MQTTOptions res = h.processArgs(new String[] { "-b", "XXX:1883" });

		assertEquals("XXX:1883", res.getBrokerURL());
		assertNull(res.getClientID());
		assertFalse(res.isDebug());
		assertNull(res.getTopic());
	}

	@Test
	public void checkForBrokerDebugAndTopicAndClientID() {
		MQTTOptionsHelper h = new MQTTOptionsHelper();
		MQTTOptions res = h.processArgs(new String[] { "-b", "XXX", "-d", "-t", "ZZZ", "-c", "AAA" });

		assertEquals("XXX", res.getBrokerURL());
		assertEquals("AAA", res.getClientID());
		assertTrue(res.isDebug());
		assertEquals("ZZZ", res.getTopic());
	}


	@Test
	public void checkForDebugLast() {
		MQTTOptionsHelper h = new MQTTOptionsHelper();
		MQTTOptions res = h.processArgs(new String[] { "-b", "XXX", "-t", "ZZZ", "-c", "AAA", "-d" });

		assertEquals("XXX", res.getBrokerURL());
		assertEquals("AAA", res.getClientID());
		assertTrue(res.isDebug());
		assertEquals("ZZZ", res.getTopic());
	}

	@Test
	public void checkForDebugFirst() {
		MQTTOptionsHelper h = new MQTTOptionsHelper();
		MQTTOptions res = h.processArgs(new String[] { "-d", "-b", "XXX", "-t", "ZZZ", "-c", "AAA"});

		assertEquals("XXX", res.getBrokerURL());
		assertEquals("AAA", res.getClientID());
		assertTrue(res.isDebug());
		assertEquals("ZZZ", res.getTopic());
	}

}
