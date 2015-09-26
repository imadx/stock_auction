import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

class ClientHandler implements Runnable{

	private Socket clientSocket;
	private String userName;
	private String symbol;
	private StockDatabase stockStats;
	private ClientServerTransport messenger;
	public ClientHandler(Socket connectionSocket){
		this.clientSocket = connectionSocket;
		stockStats = new StockDatabase("availableStocks.csv","Symbol", "Price");
		System.out.print("Client connected..");
		messenger = new ClientServerTransport(clientSocket);
	}

	public void run(){
		try{
			System.out.println("Accepting Input.");
			getClientId();

			sendWelcomeMessage();
			getSymbol();
			
			// get bids from client
			String line;
			line = messenger.getFromClient("Enter your bids (\'Q\' to quit)");
			while(!(line.equals("Q")||line.equals("q"))){
				System.out.println(userName + " bids on "+ symbol+": " + line);
				Float t_price;
				try{
					t_price = Float.parseFloat(line);
					if(t_price<0){
						messenger.sayToClient("Enter a price, you entered something else..");
						line = messenger.getFromClient("Enter your bids (\'Q\' to quit)");
						continue;
					}
					stockStats.setPrice(symbol, t_price);
					messenger.sayToClient("bid updated >> " + symbol + ": " + stockStats.getPrice(symbol).toString());

				} catch (Exception e){
					messenger.sayToClient("Enter a price, you entered something else..");
				}

				line = messenger.getFromClient("Enter your bids (\'Q\' to quit)");
			}

			// client disconnected.. Close the socket.
			messenger.out.close(); 
	    	messenger.in.close(); 
			this.clientSocket.close();
			System.out.println("Client disconnected.. [" + userName+ "]");
		} catch (IOException e){
			System.out.println(e);
		}
	}

	public void getClientId() throws IOException{
		System.out.print("Waiting for Client Username.. ");

		userName= messenger.getFromClient("Who are you?");

		System.out.print("\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b");
		System.out.println("Client Username set: " + userName + "              ");
	}
	public void sendWelcomeMessage() throws IOException{
		System.out.print("Sending welcome message to " + userName + "...");
		messenger.sayToClient("Hello, " + userName);
		messenger.sayToClient(stockStats.availableStockList());
		System.out.println("Sent.");
	}
	public void getSymbol() throws IOException{
		Float t_price = 0f;
		boolean error = true;
		while(true){
			symbol = messenger.getFromClient("Symbol to bid on").toUpperCase();
			try{
				t_price = stockStats.getPrice(symbol);
			} catch (Exception e){
				System.out.println(e);
			} finally{
				if(t_price!=null){
					messenger.sayToClient("Current price of " + symbol + " stock is " + t_price);
					break;
				}					
			}
		}
	}
}