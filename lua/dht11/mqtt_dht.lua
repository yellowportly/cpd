conns=1
PIN = 4 --  data pin, GPIO2
msgs=0
failed = 0

--[[
wifi.setmode(wifi.STATION)
wifi.sta.config("peppa","walter27")
wifi.sta.connect();
--]]
tmr.alarm(3,5000,1,function ()
                        tmr.stop(3)
                        print("connected with "..wifi.sta.getip())
                        tmr.alarm(1,60000, 1, do_dht11_check)
                   end)

function do_dht11_check() 
    ReadDHT11() 
    conns=conns+1 
    if conns==5 then 
        conns=0 
        wifi.sta.connect() 
        print("** Reconnecting WIFI")
    end 
end

function sendMQTTMessage ()                        
            print("1. MQTT connected")
            m:publish("/cpd/dht/temp",temp,0,0, function(client) print("2b. Sent temp: "..temp) end)
            m:publish("/cpd/dht/humi",humi,0,0, function(client) print("2c. Sent humi: "..humi) end)
            msgs = msgs + 1
            showMessage("Temp:"..temp.." Humidity:"..humi, "MQTT: "..msgs.." ("..failed..")")

            tmr.alarm(2,3000,1,function() 
                                    tmr.stop(2)
                                    print("3. Closing MQTT")
                                    m:close() 
                                    print("4. Closed MQTT")
                                   end)
end

--ReadDHT();

function ReadDHT11()
    status, temp, humi, temp_dec, humi_dec = dht.read(PIN)
    print("** Read DHT11")
    if status == dht.OK then
        -- Float firmware using this example
        print("** DHT Temperature:"..temp..";".."Humidity:"..humi)
    elseif status == dht.ERROR_CHECKSUM then
        print( "** DHT Checksum error" )
    elseif status == dht.ERROR_TIMEOUT then
        print( "** DHT timed out" )
    end

    print("** Connecting to MQTT")
    m = mqtt.Client("SensorDHT11", 10);
    
    m:connect("192.168.1.91", 1883, 0, 1, 
                    function(client)
                        sendMQTTMessage()
                    end,
                    function(client, reason) 
                            showMessage("Temp:"..temp.." Humidity:"..humi, "MQTT failed: ("..reason..")")
                            print("MQTT failed for reason: "..reason)
                            failed = failed + 1 
                    end)
end




-- ReadDHT11()
srv=net.createServer(net.TCP) srv:listen(80,function(conn)
    conn:on("receive",function(conn,payload)
        --print(payload) -- for debugging only
        --generates HTML web site
        conn:send('HTTP/1.1 200 OK\r\nConnection: keep-alive\r\nCache-Control: private, no-store\r\n\r\n\
        <!DOCTYPE HTML>\
        <html><head><meta content="text/html;charset=utf-8"><title>ESP8266</title></head>\
        <body bgcolor="#ffe4c4"><h2>Hygrometer with<br>DHT11 sensor</h2>\
        <h3><font color="green">\
        <IMG SRC="http://esp8266.fancon.cz/common/hyg.gif"WIDTH="64"HEIGHT="64"><br>\
        <input style="text-align: center"type="text"size=4 name="j"value="'..humi..'"> % of relative humidity<br><br>\
        <IMG SRC="http://esp8266.fancon.cz/common/tmp.gif"WIDTH="64"HEIGHT="64"><br>\
        <input style="text-align: center"type="text"size=4 name="p"value="'..temp..'"> Temperature grade C<br>\
        <IMG SRC="http://esp8266.fancon.cz/common/dht11.gif"WIDTH="200"HEIGHT="230"BORDER="2"></body></html>')
        conn:on("sent",function(conn) conn:close() end)
        end)
end)


oled_test.lua (2036 bytes)

------------------------------------------------------------------------------
-- u8glib example which shows how to implement the draw loop without causing
-- timeout issues with the WiFi stack. This is done by drawing one page at
-- a time, allowing the ESP SDK to do its house keeping between the page
-- draws.
--
-- This example assumes you have an SSD1306 display connected to pins 4 and 5
-- using I2C and that the profont22r is compiled into the firmware.
-- Please edit the init_display function to match your setup.
--  
-- Example:
-- dofile("u8g_drawloop.lua")
------------------------------------------------------------------------------

local disp
local font

function init_display()
  local sda = 4
  local sdl = 5
  local sla = 0x3c
  i2c.setup(0,sda,sdl, i2c.SLOW)
  disp = u8g.ssd1306_128x32_i2c(sla)
  font = u8g.font_profont22r
end

local function setLargeFont()
  disp:setFont(font)
  disp:setFontRefHeightExtendedText()
  disp:setDefaultForegroundColor()
  disp:setFontPosTop()
end

-- Start the draw loop with the draw implementation in the provided function callback
function updateDisplay(func)
  -- Draws one page and schedules the next page, if there is one
  local function drawPages()
    func()
    if (disp:nextPage() == true) then
      node.task.post(drawPages)
    end
  end
  -- Restart the draw loop and start drawing pages
  disp:firstPage()
  node.task.post(drawPages)
end

function drawHello()
  setLargeFont()
  disp:drawStr(30,10, "Hello")
end

function drawWorld()
  setLargeFont()
  disp:drawStr(30,10, "World")
end

local drawDemo = { drawHello, drawWorld }

function demoLoop()
  -- Start the draw loop with one of the demo functions
  local f = table.remove(drawDemo,1)  
  updateDisplay(f)
  table.insert(drawDemo,f)
end

-- Initialise the display
init_display()

-- Draw demo page immediately and then schedule an update every 5 seconds.
-- To test your own drawXYZ function, disable the next two lines and call updateDisplay(drawXYZ) instead.
demoLoop()
tmr.alarm(4, 5000, 1, demoLoop)

