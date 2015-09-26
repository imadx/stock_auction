import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class AuctionServer{
	// Take incoming connections and pass them to worker threads

	private static int BASE_PORT = 2000;
	

	public AuctionServer(int BASE_PORT) throws IOException{
		System.out.print("Auction Server starting..");
		ServerSocket serverSocket = new ServerSocket(BASE_PORT);
		System.out.println("\b\b\b\b\bed.   ");
		while(true){
			Socket socket = serverSocket.accept();
			Thread thread = new Thread(new ClientHandler(socket));
			thread.start();
		}
	}
}
	    
	