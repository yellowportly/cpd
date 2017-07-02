#!/usr/bin/python


__author__ = 'Ian Pearce'

import time
import argparse
import sys
import traceback
import paho.mqtt.client as mqtt
import sqlite3
import datetime
from datetime import datetime

# The callback for when the client receives a CONNACK response from the server.
def on_connect(client, userdata, flags, rc):
    print("DataUpload connected to MQTT with result code "+str(rc))
    pass

# The callback for when the client receives a CONNACK response from the server.
def on_disconnect(client, userdata, rc):
    print("DataUpload disconnected to MQTT with result code "+str(rc))
    pass

# The callback for when a PUBLISH message is received from the server.
def on_message(client, userdata, msg):
    print(msg.topic+" "+str(msg.payload))
    pass

def get_db_data(db):
    sql = "SELECT DATE(Timestamp, 'unixepoch') AS Date, TIME(Timestamp, 'unixepoch') AS Time, TimeStamp, EToday, ETotal, Uac1, Temperature FROM SpotData ORDER BY TimeStamp DESC LIMIT 1"
    cursor = db.cursor()
    cursor.execute(sql)
    row = cursor.fetchone()
    return row

def main(mqtt_broker, mqtt_topic, sqlite_db):
    db = sqlite3.connect(sqlite_db)

    # while True:
    try:
        # Connect to MQTT
        client = mqtt.Client()
        client.on_connect = on_connect
        client.on_message = on_message
        client.connect(mqtt_broker, 1883, 60)

        # Format the MQTT topics to publish values under
        mqtt_prefix = mqtt_topic
        topic_solar = "/{0}/values".format(mqtt_prefix)
        row = get_db_data(db)

#        while True:
        # MQTT Blocking call that processes network traffic, dispatches callbacks and handles reconnecting.
        client.loop()

        payload = "{0},{1},{2},{3},{4},{5},{6}".format(row[0],row[1],row[2],row[3],row[4],row[5],row[6])
        print("Publishing")
        print(payload)
        client.publish(topic_solar, payload=payload, qos=0, retain=False)
        time.sleep(1)

    except Exception as inst:
        # print >>sys.stderr, type(inst)     # the exception instance
        # print >>sys.stderr, inst.args      # arguments stored in .args
        # print >>sys.stderr, inst           # __str__ allows args to printed directly
        print(datetime.now().isoformat())
        traceback.print_exc(file=sys.stderr)
        btSocket.close()
        error_count+=1
        payload = "{0}".format(error_count)
        client.publish(topic_errors, payload=payload, qos=0, retain=False)

    client.disconnect()
    db.close()
    time.sleep(1)

# Gets the args from the command line
parser = argparse.ArgumentParser(
    description='SMA Data upload from SQlite',
    epilog='Copyright 2017 neverperplexed.com')

parser.add_argument('broker', metavar='broker', type=str,
                    help='hostname or address of mqtt broker')

parser.add_argument('topic', metavar='topic', type=str,
                    help='Topic on which to publish')

parser.add_argument('sqlite_db', metavar='sqlite_db', type=str,
                    help='Location of sqlite3 db file')

args = parser.parse_args()


main(args.broker, args.topic, args.sqlite_db)

exit()
