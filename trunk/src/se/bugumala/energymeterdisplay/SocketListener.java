package se.bugumala.energymeterdisplay;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class SocketListener {
	private ServerSocket server;
	private int port = 7777;

	public SocketListener() {
		try {
			server = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		SocketListener example = new SocketListener();
		example.handleConnection();
	}

	public void handleConnection() {
		System.out.println("Waiting for client message...");

		//
		// The server do a loop here to accept all connection initiated by the
		// client application.
		//
		while (true) {
			try {
				Socket socket = server.accept();
				new ConnectionHandler(socket);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}

class ConnectionHandler implements Runnable {
	private Socket socket;

	public ConnectionHandler(Socket socket) {
		this.socket = socket;

		Thread t = new Thread(this);
		t.start();
	}

	public void run() {
		try {
			//
			// Read a message sent by client application
			//
			ObjectInputStream ois = new ObjectInputStream(socket
					.getInputStream());
			String message = (String) ois.readObject();
			String[] parts = message.split(";");
			long timestamp = Long.parseLong(parts[0]);
			long pulsesPerHour = Long.parseLong(parts[1]);
			System.out.println("unix time: " + timestamp + "( = " + new Date(timestamp) + ")");
			System.out.println("pulses per hour: " + pulsesPerHour);		

			ois.close();
			socket.close();

			System.out.println("Waiting for client message...");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
