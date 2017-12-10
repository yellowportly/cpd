SSID    = "peppa"
APPWD   = "walter27"
CMDFILE = "ping.lua"   -- File that is executed after connection
 
wifiTrys     = 1     -- Counter of trys to connect to wifi
NUMWIFITRYS  = 200    -- Maximum number of WIFI Testings while waiting for connection
 
function launch()
  init_i2c_display()
  print("Initialised display !")
  
  print("Connected to WIFI!")
  print("IP Address: " .. wifi.sta.getip())
  showMessage("IP: ", wifi.sta.getip())
  -- Call our command file. Note: if you foul this up you'll brick the device!
  
  -- dofile("security.lua")
  -- makeConn()
  dofile("mqtt_dht.lua")
end
 
function checkWIFI() 
  if ( wifiTrys > NUMWIFITRYS ) then
    print("Sorry. Not able to connect")
  else
    ipAddr = wifi.sta.getip()
    if ( ( ipAddr ~= nil ) and  ( ipAddr ~= "0.0.0.0" ) )then
      tmr.alarm( 1 , 500 , 0 , launch )
    else
      -- Reset alarm again
      tmr.alarm( 0 , 2500 , 0 , checkWIFI)
      print("Checking WIFI..." .. wifiTrys)
      wifiTrys = wifiTrys + 1
    end 
  end 
end

function pre_init() 
    tmr.stop(0)
    if abort == true then
        print('** pre_init - startup aborted')
        return
        end
    print('** pre_init - Now in startup')
    do_init()
end

function do_init() 
    print("** do_init ! ") 
    -- Lets see if we are already connected by getting the IP
    ipAddr = wifi.sta.getip()
    if ( ( ipAddr == nil ) or  ( ipAddr == "0.0.0.0" ) ) then
      -- We aren't connected, so let's connect
      print("** do_oinit - Configuring WIFI....")
      wifi.setmode( wifi.STATION )
      wifi.sta.config( SSID , APPWD)
      print("** do_oinit - Waiting for connection")
      tmr.alarm( 0 , 2500 , 0 , checkWIFI )
    else
     -- We are connected, so just run the launch code.
     launch()
    end
end

-- setup I2c and connect display
function init_i2c_display()
     -- SDA and SCL can be assigned freely to available GPIOs
     local sda = 1 -- GPIO14
     local scl = 2 -- GPIO12
     local sla = 0x3c
     i2c.setup(0, sda, scl, i2c.SLOW)
     disp = u8g.ssd1306_128x32_i2c(sla)
end

function showMessage(msg, msg2)
      disp:firstPage()
      local looper
      looper = 0
      repeat
         disp:setFont(u8g.font_6x10)
         disp:drawStr( 0, 10, msg)
         disp:drawStr( 0, 25, msg2)
        print("Waiting on next page")
        looper = looper + 1
      until disp:nextPage() == false
end

-- and this starts it all
tmr.alarm(0,5000,0,pre_init)
