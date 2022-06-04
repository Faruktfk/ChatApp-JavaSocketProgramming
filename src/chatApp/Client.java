package chatApp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class Client {

	private Socket socket;
	private BufferedReader bufferedReader;
	private BufferedWriter bufferedWriter;
	private String username;
	private String history = "";

	public Client(Socket socket, String username) {
		try {
			this.socket = socket;
			this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			this.username = username;

			bufferedWriter.write(username);
			bufferedWriter.newLine();
			bufferedWriter.flush();
		} catch (IOException e) {
			closeEverything();
		}
	}
	
	public void sendMessage(String messageToSend) {
		try {
			
			if(socket.isConnected()) {
				history+= "(Me): " + messageToSend+"\n";
				bufferedWriter.write(username + ": " + messageToSend);
				bufferedWriter.newLine();
				bufferedWriter.flush();
			}
			
		} catch (IOException e) {
			closeEverything();
		}
	}
	
	public void listenForMessage() {
		new Thread(() -> {
			String msgFromGroupChat;
			while(socket.isConnected()) {
				try {
					msgFromGroupChat = bufferedReader.readLine();
					history+=msgFromGroupChat+"\n";
				} catch (IOException e) {
					closeEverything();
				}
			}
		}).start();
	}

	private void closeEverything() {
		try {
			if (bufferedReader != null) {
				bufferedReader.close();
			}
			if (bufferedWriter != null) {
				bufferedWriter.close();
			}
			if (socket != null) {
				socket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getHistory() {
		return history;
	}
}
