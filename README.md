# stock_auction
A client server model for an auction scenario (in this case a bidding on stocks) created in Java/ JavaFX

### Running the server
Use java to start the server.  
  
compile the source with `javac StartServer.java`  
and run it with `java StartServer`  

### Connecting to the server  
Use telnet(in windows) or nc(in linux) to connect with the server  
the server is listening on port 2000  

##### On Windows
`telnet localhost 2000` or  
`telnet <ip address> 2000` *(yes, you can connect remotely with several people and have fun)*

##### On Linux
`nc localhost 2000` or `nc <ip address> 2000`  

### Preview of the server-side GUI
This is a snapshot taken when a client is connected and bids on a symbol available.  
![Screenshot of the server-side GUI](https://github.com/imadx/stock_auction/blob/master/img/snapshot.jpg)
