import onionGpio
import time
import paho.mqtt.client as mqtt #import the client1


def setUpMQTT():
    broker_address="192.168.1.91"
    client = mqtt.Client("P1")
    client.connect(broker_address)
    return client

## define constants
ledOnDuration = 0.10
pollingInterval = 0.1

## initialize GPIOs
switchPin     = onionGpio.OnionGpio(2)      # use GPIO0
ledPin        = onionGpio.OnionGpio(1)      # use GPIO1

## set the GPIO directions
switchPin.setInputDirection()               # switch pin is an input
ledPin.setOutputDirection(0)                # led pin is an output

client = setUpMQTT()

## trigger the switch via edge detection
while 1:
     val = 1 * switchPin.getValue()
     # print ("XX%sXX" % val[:1])
     if val[:1] == "1":
        last = ledPin.getValue()
        if last[:1] == "0":
          ledPin.setValue(1)
          print "Changed On"
          client.publish("/cpd/dish","ON")
     else:
        last = ledPin.getValue()
        if last[:1] == "1":
          ledPin.setValue(0)
          print "Changed Off"
          client.publish("/cpd/dish","OFF")

     time.sleep(pollingInterval)
