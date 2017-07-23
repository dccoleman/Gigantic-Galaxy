package com.dccoleman.game;

public class Galaxy {
	private final double SECTORS_PER_GALAXY = .4;
	
	private int x, y;
	
	private int[][] board;
	
	public Galaxy(int x, int y) {
		this.x = x;
		this.y = y;
		
		board = new int[x][y];
		
		generateBoard();
	}
	
	private void generateBoard() {
		int count = 0;
		for(int i = 0; i < x; i++) {
			for(int j = 0; j < y; j++) {
				if(Math.random() <= SECTORS_PER_GALAXY) {
					System.out.println("Sector generated at " + i + " " + j);
					count++;
				}
			}
		}
		System.out.println(count + " sectors generated in galaxy of size " + x*y);
		
	}

	public final int[][] getBoard() {
		return board;
	}
	
}
