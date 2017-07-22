package com.dccoleman.communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * 
 * @author Devon Coleman
 * A class responsible for handling communication between client and server
 */
public class Communicator {
	
	private PrintWriter out;
	private BufferedReader in;
	
	public Communicator(Socket socket) {
		try {
			out = new PrintWriter(socket.getOutputStream(), true);
		} catch (IOException e) {
			System.out.println("Failed creating output stream");
			e.printStackTrace();
		}
		
		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			System.out.println("Failed creating input stream");
			e.printStackTrace();
		}
		
		System.out.println("Communicator complete!");
	}
	
	public void say(String message) {
		System.out.println("Saying " + message);
		out.println(message);
	}
	
	public String waitForMessage() {
		String message = "Error";
		try {
			message = in.readLine();
		} catch (IOException e) {
			System.out.println("Error waiting for message!");
			e.printStackTrace();
		}
		return message;
	}
}
