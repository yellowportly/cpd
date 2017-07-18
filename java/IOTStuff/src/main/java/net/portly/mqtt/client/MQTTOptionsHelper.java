package net.portly.mqtt.client;

import java.util.HashMap;
import java.util.Map;

public class MQTTOptionsHelper {

	public MQTTOptions processArgs(String[] args) {
		Map<String, String> map = new HashMap<>();

		for (int counter = 0; args != null && counter < args.length; counter++) {
			String arg = args[counter];

			if (arg.startsWith("-") && arg.length() > 1) {
				String nextArg = null;
				if (counter + 1 < args.length && !("-d".equals(nextArg))) {
					nextArg = args[counter + 1];
				}
				
				boolean incr = false;
				
				switch (arg) {
				case "-b":
					if (nextArg != null) {
						map.put(MQTTClient2.ARG_BROKER_URL, nextArg);
						incr = true;
					}
					break;
				case "-c":
					if (nextArg != null) {
						map.put(MQTTClient2.ARG_CLIENT_ID, nextArg);
						incr = true;
					}
					break;
				case "-d":
					map.put(MQTTClient2.ARG_DEBUG, "true");
					break;
				case "-s":
					if (nextArg != null) {
						map.put(MQTTClient2.ARG_DB_URL, nextArg);
						incr = true;
					}
					break;
				case "-t":
					if (nextArg != null) {
						map.put(MQTTClient2.ARG_TOPIC, nextArg);
						incr = true;
					}
					break;
				}
				
				if (incr) {
					counter++;
				}
			}
		}

		return new MQTTOptions(map);
	}
}
