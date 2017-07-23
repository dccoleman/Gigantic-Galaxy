package com.dccoleman.game;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import com.dccoleman.graph.EdgeData;
import com.dccoleman.graph.Graph;

public class Galaxy {
	
	private final static int MAX_FUEL_USE = 4;
	
	private final int numSectors;
	private final long numPaths;
	private Graph<Sector> galaxy;
	
	public Galaxy(int numSectors, double pathFactor) {
		this.numSectors = numSectors;
		int maxPaths = (numSectors * (numSectors -1))/2;
		this.numPaths = Math.round(numSectors * pathFactor > maxPaths ? maxPaths : numSectors * pathFactor);
		
		System.out.println("Generating " + numSectors + " sectors with " + numPaths + " paths");
		
		generateBoard();
	}
	
	private void generateBoard() {
		galaxy = new Graph<>();
		
		//Generate all sectors
		generateSectors();
		
		int mstCount;
		long randomCount;
		//Edge generation algorithm
		System.out.println("Generated " + (mstCount = generateMST()) + " paths as part of the mst");
		
		//add random paths
		System.out.println("Generated " + (randomCount = addRandomPaths(mstCount)) + " paths randomly");
		
		System.out.println("Generated " + (randomCount + mstCount) + " total paths");
		
		int count = 0;
		int sum = 0;
		int max = 0;
		int current = 0;
		for(Sector s : galaxy.getAllNodes()) {
			current = galaxy.getCardinalityOfNode(s);
			sum += current;
			if(current > max) max = current;
			count++;
		}
		System.out.println("Average cardinality is " + sum/count);
		System.out.println("Maximum cardinality is " + max);
		
		try {
			galaxy.toCSV("C:\\Users\\Bacon\\Desktop");
		} catch (FileNotFoundException e) {
			System.out.println("CSV Error");
			e.printStackTrace();
		}
	}

	private long addRandomPaths(long pathCount) {
		List<Sector> sectors = galaxy.getAllNodes();
		
		long numGeneratedHere = 0;
		
		//generate all possible paths then randomly pull from them
		List<EdgeData<Sector>> generated = new ArrayList<>();
		for(int i = 0; i < sectors.size(); i++) {
			for(int j = i; j < sectors.size(); j++) {
				if(!sectors.get(i).equals(sectors.get(j))) {
					generated.add(new EdgeData<Sector>(sectors.get(i),sectors.get(j),0));	
				}
			}
		}
		
		while(pathCount < numPaths) {
			EdgeData<Sector> e = popRandomEdgeData(generated);
			
			//System.out.println("Adding edge number " + pathCount);
			galaxy.addEdge(e.getV1(), e.getV2(), getRandomWeight());
			
			pathCount++;
			numGeneratedHere++;
			
		}
		
		return numGeneratedHere;
	}

	private EdgeData<Sector> popRandomEdgeData(List<EdgeData<Sector>> l) {
		return l.remove(ThreadLocalRandom.current().nextInt(0,l.size()));
	}

	private int generateMST() {
		List<Sector> created = galaxy.getAllNodes();
		List<Sector> visited = new ArrayList<>();
		Sector current = popRandomSector(created);
		visited.add(current);
		
		int pathCount = 0;
		
		while(created.size() > 0) {
			Sector neighbor = popRandomSector(created);
			if(!visited.contains(neighbor)) {
				visited.add(neighbor);
				galaxy.addEdge(current, neighbor, getRandomWeight());
				current = neighbor;
				pathCount++;
			}
		}
		
		return pathCount;
	}

	private int getRandomWeight() {
		return ThreadLocalRandom.current().nextInt(0,MAX_FUEL_USE);
	}

	private Sector popRandomSector(List<Sector> l) {
		return l.remove(ThreadLocalRandom.current().nextInt(0,l.size()));
	}

	private List<Sector> generateSectors() {
		List<Sector> generated = new ArrayList<>();
		
		int id = 0;
		for(int i = 0; i < numSectors; i++) {
			//System.out.println("Generating sector for " + i);
			Sector s = new BasicSector(id++);
			galaxy.addVertex(s);
			generated.add(s);
		}
		
		return generated;
	}	
}
