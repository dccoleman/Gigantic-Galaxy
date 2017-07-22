package com.dccoleman.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.dccoleman.communication.Communicator;

public class Server {
	
	private int port;
	
	private ServerSocket server;
	private Socket client;
	
	private Communicator c;
	
	public Server(int port) {
		this.port = port;
	}
	
	public void setUp() {
		try {
			server = new ServerSocket(port);
		} catch (IOException e) {
			System.out.println("Error opening socket");
			e.printStackTrace();
		}
	}
	
	public void run() {
		if(server != null) {
			try {
				client = server.accept();
			} catch (IOException e) {
				System.out.println("Error accepting client connection");
				e.printStackTrace();
			}
			
			if(client != null) {
				c = new Communicator(client);
				
				c.say("Hello client!");
				
			}
		}
		System.out.println("Run finished!");
	}
	
}
