package net.portly.mqtt.client;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JSONHelper {

	private String json;

	public JSONHelper(String string) {
		this.json = string;
	}

	public <T> T deserialize(Class<T> clazz) {
		ObjectMapper objectMapperDeserializer = new ObjectMapper();
		T object = null;
		if (json != null) {
			try {
				object = objectMapperDeserializer.readValue(json, clazz);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return object;
	}

}
