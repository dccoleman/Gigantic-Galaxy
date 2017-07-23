package com.dccoleman.main;

import com.dccoleman.client.GameClient;
import com.dccoleman.server.Server;

public class Driver {

	public static void main(String[] args) {
		
		String run = args[0];
		
		int port = 6969;
		
		switch(run) {
		case "Client":
			System.out.println("Client Initializing");
			GameClient client = new GameClient();
			client.connect("localhost", port);
			client.run();
			client.tearDown();
			break;
			
		case "Server":
			System.out.println("Server Initializing");
			Server server = new Server(port);
			server.run();
		}
		

	}

}
