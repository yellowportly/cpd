# SMADataPump

This process will pull the last entry in a SQLite3 database and send it to a noiminated broker on a nominated topic

It runs exactly once and will terminate and it's really just designed to be run via a scheduled task such as a crontab

## Invoking
An example command line is as follows;-

python SMADataPump 192.168.1.91 cpd/solar /home/pi/sbf/SBFSpot.db

Note: This will publish to a topic '/cpd/solar/values' (leading slash and appended /values come for free)
