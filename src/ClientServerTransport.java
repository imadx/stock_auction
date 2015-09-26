import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.net.Socket;

class ClientServerTransport{
	private Socket clientSocket;
	public PrintWriter out;
	public BufferedReader in;
			
	public ClientServerTransport(Socket clientSocket){
		this.clientSocket = clientSocket;
		try{
			out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		} catch (Exception e){
			System.out.println(e);
		}
	}
	public void sayToClient(String message) throws IOException{
		out.print(message + "\r\n");
		out.flush();
	}
	public String getFromClient(String prompt) throws IOException{
		out.print(prompt + ": ");
		out.flush();
		String input = in.readLine();
		return input;
	}
}