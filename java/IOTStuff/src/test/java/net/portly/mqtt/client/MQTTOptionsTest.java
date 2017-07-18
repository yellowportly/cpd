package net.portly.mqtt.client;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class MQTTOptionsTest {

	@Test
	public void checkDebug() {
		MQTTOptions o = new MQTTOptions(getDebugMap());
		assertTrue(o.isDebug());
		assertNull(o.getTopic());
		assertNull(o.getBrokerURL());
		assertNull(o.getClientID());
	}

	@Test
	public void checkOtherOptions() {
		MQTTOptions o = new MQTTOptions(getClientBrokerTopicMap());
		assertFalse(o.isDebug());
		assertEquals("TOPIC", o.getTopic());
		assertEquals("BROKERURL", o.getBrokerURL());
		assertEquals("CLIENTID", o.getClientID());
	}

	
	
	private Map<String, String> getDebugMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put (MQTTClient2.ARG_DEBUG, "true");
		return map;
	}

	private Map<String, String> getClientBrokerTopicMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put (MQTTClient2.ARG_BROKER_URL, "BROKERURL");
		map.put (MQTTClient2.ARG_CLIENT_ID, "CLIENTID");
		map.put (MQTTClient2.ARG_TOPIC, "TOPIC");
		return map;
	}

}
