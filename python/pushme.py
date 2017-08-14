#!/usr/bin/env python

import time
import argparse
import fourletterphat
import sqlite3
import RPi.GPIO as GPIO

button_pin = 26

def setup_button(button_pin):
  GPIO.setmode(GPIO.BCM)
  GPIO.setup(button_pin, GPIO.IN, pull_up_down=GPIO.PUD_UP)

def get_db_data(db):
    sql = "SELECT EToday FROM SpotData ORDER BY TimeStamp DESC LIMIT 1"
    cursor = db.cursor()
    cursor.execute(sql)
    row = cursor.fetchone()
    return row


# # # Main function
def main(sqlite_db):
  setup_button(button_pin)
  db = sqlite3.connect(sqlite_db)

  while True:
    input_state = GPIO.input(button_pin)

    if input_state == False:
      row = get_db_data(db)
      data = "%4d" % row[0]

      # Display the first four letters of the message
      fourletterphat.print_str(data[:4])
      fourletterphat.show()
      time.sleep(3)
      fourletterphat.print_str("    ")
      fourletterphat.show()


# Gets the args from the command line
parser = argparse.ArgumentParser(
    description='Push button SMA data',
    epilog='Copyright 2017 neverperplexed.com')

parser.add_argument('sqlite_db', metavar='sqlite_db', type=str,
                    help='Location of sqlite3 db file')

args = parser.parse_args()

main(args.sqlite_db)

exit()

