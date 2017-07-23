package com.dccoleman.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.dccoleman.communication.Communicator;
import com.dccoleman.game.Galaxy;

public class Server {
	
	private ServerSocket server;
	private Socket client;
	
	private Communicator c;
	
	private Galaxy g;
	
	public Server(int port) {
		try {
			server = new ServerSocket(port);
		} catch (IOException e) {
			System.out.println("Error opening socket");
			e.printStackTrace();
		}
		
		g = new Galaxy(100);
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
				int i = 0;
				c = new Communicator(client);
				String response;
				while(true) {
					
					c.say(Integer.toString(++i));
					
					if((response = c.waitForMessage()) == null) break;
					System.out.println(response);
				}
			}
		}
		System.out.println("Run finished!");
	}
	
}
