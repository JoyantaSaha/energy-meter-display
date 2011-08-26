package se.bugumala.energymeterdisplay;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Date;

public class SocketClient {
	private Socket socket;
	private int port = 60112;
	private ObjectInputStream ois;

	public SocketClient() {
		try {
			socket = new Socket("192.168.10.190", port);
			ois = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		SocketClient example = new SocketClient();
		example.handleConnection();
	}

	public void handleConnection() {
		System.out.println("Waiting for server message...");

		//
		// The server do a loop here reading all objects
		//
		while (true) {
			try {
				String message = (String) ois.readObject();
				String[] parts = message.split(";");
				long timestamp = Long.parseLong(parts[0]);
				long pulsesPerHour = Long.parseLong(parts[1]);
				System.out.println("unix time: " + timestamp + "( = " + new Date(timestamp) + ")");
				System.out.println("pulses per hour: " + pulsesPerHour);		

			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
