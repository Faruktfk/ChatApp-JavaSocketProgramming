package chatApp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	ServerSocket serverSocket;

	public Server(ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
	}

	public void startServer() {
		System.out.println("Server is running...");
		try {
			while (!serverSocket.isClosed()) {
				Socket client = serverSocket.accept();
				System.out.println("A new client has connected");
				
				new Thread(new ClientHandler(client)).start();
			}

		} catch (IOException e) {
			closeServerSocket();
		}
	}

	private void closeServerSocket() {
		try {
			if(serverSocket!=null) {
				serverSocket.close();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws IOException {
		
		ServerSocket serverSocket = new ServerSocket(4444);
		Server server = new Server(serverSocket);
		server.startServer();
		
	}

}
