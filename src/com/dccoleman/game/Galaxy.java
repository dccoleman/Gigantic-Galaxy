package com.dccoleman.game;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import com.dccoleman.graph.Graph;

public class Galaxy {
	private final double CONNECTIVITY = .4;
	
	private final static int MAX_FUEL_USE = 4;
	
	private final int numSectors;
	
	private Graph<Sector> galaxy;
	
	public Galaxy(int numSectors) {
		this.numSectors = numSectors;
		
		generateBoard();
		
		//System.out.println(galaxy);
	}
	
	private void generateBoard() {
		galaxy = new Graph<>();
		
		List<Sector> created = generateSectors();
		
		generateNPaths(created, null, numSectors);
		
		List<List<Sector>> discon;
		while((discon = galaxy.getAllPartitions()).size() > 1) {
			
			System.out.println("Found " + discon.size() + " disconnected graphs");
			List<Sector> l = discon.get(ThreadLocalRandom.current().nextInt(0, discon.size()));
			List<Sector> center = discon.get(ThreadLocalRandom.current().nextInt(0, discon.size()));
			if(l.equals(center)) continue;
			System.out.println("L " + l + " center " + center);
			generateNPaths(l, center, 1);
		}
		
		//print out list of nodes w/cardinality > 3
		System.out.println(galaxy.getNodeWithCardinality(3));
		
		try {
			galaxy.toCSV("C:\\Users\\Bacon\\Desktop");
		} catch (FileNotFoundException e) {
			System.out.println("CSV Error");
			e.printStackTrace();
		}
	}

	private void generateNPaths(List<Sector> first, List<Sector> second, int n) {
		if(second == null) second = first;
		for(int i = 0; i < n; i++) {
			
			int r1, r2;
			r1 = ThreadLocalRandom.current().nextInt(0, first.size());
			r2 = ThreadLocalRandom.current().nextInt(0, second.size());
			
			if((first.size() == 1 && second.size() == 99) || (first.size() == 99 && second.size() == 1)) {
				System.out.println("stuck as a duck in muck");
			}
			
			
			//can't generate edge from node to itself
			while(r1 == r2) {
				if(first.size() == 1 && second.size() == 1) break;
				r1 = ThreadLocalRandom.current().nextInt(0, first.size());
				r2 = ThreadLocalRandom.current().nextInt(0, second.size());
			}
			
			//Roll to see if it satisfies the connectivity requirement
			if(Math.random() < CONNECTIVITY || n == 1) {
				int weight = ThreadLocalRandom.current().nextInt(1, MAX_FUEL_USE);
				
				galaxy.addEdge(new Sector(r1), new Sector(r2), weight);
			}
			
			if((first.size() == 1 && second.size() > 1) || (first.size() > 1 && second.size() == 1)) break;
		}
		
	}

	private List<Sector> generateSectors() {
		List<Sector> generated = new ArrayList<>();
		
		int id = 0;
		for(int i = 0; i < numSectors; i++) {
			System.out.println("Generating sector for " + i);
			Sector s = new Sector(id++);
			galaxy.addVertex(s);
			generated.add(s);
		}
		
		return generated;
	}	
}
