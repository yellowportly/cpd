#!/usr/bin/env python

import time
import fourletterphat

import RPi.GPIO as GPIO


print("""
Press Ctrl+C to exit.
""")

button_pin = 26


def setup_button(button_pin):
  GPIO.setmode(GPIO.BCM)
  GPIO.setup(button_pin, GPIO.IN, pull_up_down=GPIO.PUD_UP)

message = "PUSH"
setup_button(button_pin)

while True:
    input_state = GPIO.input(button_pin)

    if input_state == False:
      # Display the first four letters of the message
      fourletterphat.print_str(message[:4])
      fourletterphat.show()
      time.sleep(3)
      fourletterphat.print_str("    ")
      fourletterphat.show()

