package com.dccoleman.client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import com.dccoleman.communication.Communicator;

public class GameClient {
	
	Communicator c;
	Socket server;
	Scanner in;
	
	public GameClient() {
		in = new Scanner(System.in);
	}
	
	public void connect(String hostname, int port) {
		try {
			server = new Socket(hostname, port);
		} catch (UnknownHostException e) {
			System.out.println("Can't resolve hostname..");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("IO error..");
			e.printStackTrace();
		}
		
		c = new Communicator(server);
		
	}
	
	public void run() {
		if(c != null && server != null) {
			while(true) {
				String message = c.waitForMessage();
				System.out.println(message);
				c.say(in.nextLine());
			}
		}
	}
	
	public void tearDown() {
		try {
			server.close();
		} catch (IOException e) {
			System.out.println("Error closing socket");
			e.printStackTrace();
		}
		in.close();
	}
}
