package net.portly.mqtt.client;

import static org.junit.Assert.*;

import org.junit.Test;

public class JSONHelperTest {

	@Test
	public void testTheDeserializationOfAnObject() {
		JSONHelper jh = new JSONHelper("{ \"action\":\"two\", \"value\":\"2\" }");
		ActionValuePair avp = jh.deserialize(ActionValuePair.class);
		assertEquals("two", avp.getAction());
		assertEquals("2", avp.getValue());
	}

}
