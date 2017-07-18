package net.portly.mqtt.client;

import java.util.Map;

public class MQTTOptions {

	private Map<String, String> map;

	public MQTTOptions(Map<String, String> map) {
		this.map = map;
	}

	public boolean isDebug() {
		return "true".equalsIgnoreCase(map.get(MQTTClient2.ARG_DEBUG));
	}

	public String getTopic() {
		return map.get(MQTTClient2.ARG_TOPIC);
	}

	public String getBrokerURL() {
		return map.get(MQTTClient2.ARG_BROKER_URL);
	}

	public String getClientID() {
		return map.get(MQTTClient2.ARG_CLIENT_ID);
	}
	
	public String getDbURL() {
		return map.get(MQTTClient2.ARG_DB_URL);
	}
}
