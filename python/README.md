# python directory

Miscellaneous scripts for python code

- pushme.py is the script to mamange the pushbutton activity that wil then display a value on the FourLetterPHAT

To run this at astartup, add an entry to /etc/rc.local as follows;-
```
python /home/pi/dev/pushme.py /home/pi/smadata/SBFspot.db
```

for reference, the FourletterPhat uses these pins;- https://pinout.xyz/pinout/four_letter_phat

