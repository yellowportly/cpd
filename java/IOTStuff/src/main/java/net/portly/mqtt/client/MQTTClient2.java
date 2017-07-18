package net.portly.mqtt.client;

import java.util.Map;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import net.portly.sqlite.client.DatabaseActions;

public class MQTTClient2 implements MqttCallback {

	MqttClient myClient;
	DatabaseActions dbActions = null;

	boolean connected = false;

	MqttConnectOptions connOpt;
	MQTTOptions options = null;

	// static final String BROKER_URL = "tcp://192.168.1.91:1883";
	// static final String M2MIO_THING = "chipshopjava";
	static final String ARG_BROKER_URL = "brokerURL";
	static final String ARG_CLIENT_ID = "clientID";
	static final String ARG_TOPIC = "topic";
	static final String ARG_DEBUG = "debug";

	static final Boolean subscriber = true;
	static final String ARG_DB_URL = "dbURL";

	public void connectionLost(Throwable t) {
		System.out.println("Connection lost!");
		connected = false;
	}

	public void deliveryComplete(IMqttDeliveryToken token) {
		// System.out.println("Pub complete" + new
		// String(token.getMessage().getPayload()));
	}

	public void messageArrived(String paramString, MqttMessage message) throws Exception {
		if (options.isDebug()) {
			System.out.println("-------------------------------------------------");
			System.out.println("| Topic:" + paramString);
			System.out.println("| Message: " + new String(message.getPayload()));
			System.out.println("-------------------------------------------------");
		}

		if (dbActions != null) {
			if (options.isDebug() && options.getDbURL() != null) {
				System.out.println("Persisting message to DB");
			}
			dbActions.doWork(paramString, new String(message.getPayload()));
		}
	}

	public static void main(String[] args) {
		MQTTClient2 smc = new MQTTClient2();
		smc.runClient(args);
	}

	public void runClient(String[] args) {
		MqttClient client = null;
		MQTTOptionsHelper helper = new MQTTOptionsHelper();
		options = helper.processArgs(args);

		if (options.getDbURL() != null) {
			dbActions = new DatabaseActions(options.getDbURL());
			System.out.println("Note: Persisting to: " + options.getDbURL());
		} else {
			System.out.println("Note: running without DB persistence");
		}

		while (true) {
			try {
				if (options.isDebug()) {
					System.out.println("Begin connection to: " + options.getBrokerURL() + " as: "
							+ options.getClientID() + " listening to: " + options.getTopic());
				}
				client = new MqttClient(options.getBrokerURL(), options.getClientID());
				MqttConnectOptions connOpts = new MqttConnectOptions();
				connOpts.setKeepAliveInterval(60);
				connOpts.setCleanSession(true);
				client.connect(connOpts);
				if (options.isDebug()) {
					System.out.println(
							"Completed connection to: " + options.getBrokerURL() + " as: " + options.getClientID());
				}

				client.setCallback(this);
				client.subscribe(options.getTopic(), 1); // qos=1

				connected = true;
				while (connected) { // check connection status
					try {
						Thread.sleep(5000);
					} catch (Exception e) {
					}
				}
			} catch (MqttException me) {
				// reconnect on exception
				System.out.printf("Exception handled, reconnecting...\nDetail:\n%s\n", me);
				try {
					Thread.sleep(5000);
					connected = false;
				} catch (Exception e) {
				}
			}
		}

	}
}
