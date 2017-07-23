package com.dccoleman.graph;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import com.dccoleman.game.Sector;

public class Graph<T extends Comparable<T>> {
	private List<Vertex> vertices;
	private List<Edge> edges;
	
	public Graph() {
		vertices = new ArrayList<>();
		edges = new ArrayList<>();
	}
	
	public void addEdge(T v1, T v2, int weight) {
		addEdge(new Vertex(v1), new Vertex(v2), weight);
	}
	
	public void addVertex(T v) {
		addVertex(new Vertex(v));
	}
	
	public Edge getEdge(Vertex v1, Vertex v2) {
		if(v1 == v2) return null;
		for(Edge e : edges) {
			if(e.containsVertex(v1) && e.containsVertex(v2)) {
				return e;
			}
		}
		return null;
	}
	
	public void addEdge(Vertex v1, Vertex v2, int weight) {
		if(!vertices.contains(v1)) vertices.add(v1);
		if(!vertices.contains(v2)) vertices.add(v2);
		Edge e;
		if((e = getEdge(v1,v2)) == null) {
			e = new Edge(v1,v2,weight);
			edges.add(e);
			vertices.get(vertices.indexOf(v1)).addEdge(e);
			vertices.get(vertices.indexOf(v2)).addEdge(e);
		} else {
			e.setWeight(weight);
		}
	}
	
	public void addVertex(Vertex v) {
		if(!vertices.contains(v)) {
			vertices.add(v);
		} else {
			System.out.println("Vertex already in graph");
		}
	}
	
	public List<List<T>> getAllPartitions() {
		int numFound = 0;
		List<List<T>> nodes = new ArrayList<>();
		for(Vertex v : vertices) {
			if(v.getMarked_id() == -1) {
				mark(v,numFound);
				numFound++;
			}
		}
		
		if(numFound > 1) {
			for(int i = 0; i < numFound; i++) {
				nodes.add(new ArrayList<T>());
			}
			
			for(Vertex v : vertices) {
				nodes.get(v.marked_id).add(v.getValue());
				v.setMarked_id(-1);
			}
		}
		
		return nodes;
	}
	
	private void mark(Vertex v, int i) {
		if(v.getMarked_id() == -1) {
			v.setMarked_id(i);
			
			for(Vertex v1 : v.getAdjacentVertices()) {
				mark(vertices.get(vertices.indexOf(v1)),i);
			}
		}
	}
	
	public List<T> getNodeWithCardinality(int c) {
		List<T> cardinality = new ArrayList<>();
		for(Vertex v : vertices) {
			if(v.getAdjacentEdges().size() > c) {
				cardinality.add(v.getValue());
			}
		}
		return cardinality;
	}
	
	@Override
	public String toString() {
		String ret = "graph | edges ";
		for(Edge e : edges) {
			ret += e.toString() + ",";
		}
		ret += " | vertices ";
		for(Vertex v : vertices) {
			ret += v.toString() + ",";
		}
		return ret;
	}
	
	public void toCSV(String path) throws FileNotFoundException {
		PrintWriter f = new PrintWriter(new File(path + "\\vertices.csv"));

		StringBuilder sb = new StringBuilder();
		sb.append("id,label\n");
		for(Vertex v : vertices) {
			sb.append(v.getValue().toString());
			sb.append("\n");
		}
		
		f.write(sb.toString());
		f.close();
		
		f = new PrintWriter(new File(path + "\\edges.csv"));

		sb = new StringBuilder();
		sb.append("Source,Target,Type,weight\n");
		for(Edge e : edges) {
			for(Vertex v : e.getVertices()) {
				Sector s = (Sector) v.getValue();
				sb.append(s.getSectorID() + ",");
			}
			sb.append("Undirected,");
			sb.append(e.getWeight());
			sb.append("\n");
		}
		
		f.write(sb.toString());
		f.close();
	}
	
	public class Vertex implements Comparable<Vertex>{
		private T value;
		private List<Edge> adjacentEdges;
		private List<Vertex> adjacentVertices;
		private int marked_id;
		
		public int getMarked_id() {
			return marked_id;
		}

		public void setMarked_id(int marked_id) {
			this.marked_id = marked_id;
		}

		public Vertex(T value) {
			this.value = value;
			adjacentEdges = new ArrayList<>();
			adjacentVertices = new ArrayList<>();
			marked_id = -1;
		}
		
		public T getValue() {
			return value;
		}
		
		public void setValue(T value) {
			this.value = value;
		}
		
		public List<Edge> getAdjacentEdges() {
			return adjacentEdges;
		}
		
		public List<Vertex> getAdjacentVertices() {
			return adjacentVertices;
		}
		
		public void addEdge(Edge e) {
			if(e.containsVertex(this)) {
				adjacentEdges.add(e);
				adjacentVertices.add(e.getOther(this));
			} else {
				System.out.println("edge does not contain this vertex");
			}
		}
		
		@Override
		public String toString() {
			return value.toString();
		}

		@Override
		public int compareTo(Graph<T>.Vertex o) {
			return this.getValue().compareTo(o.getValue());
		}
		
		@Override
		public boolean equals(Object o) {
			if(o == null) return false;
			if(o.getClass() == Vertex.class) {
				Vertex o2 = (Vertex) o;
				return this.getValue().equals(o2.getValue());
			} else {
				return false;
			}
		}
	}
	
	public class Edge {
		private List<Vertex> vertices;
		private int weight;
		
		public Edge(Vertex v1, Vertex v2, int weight) {
			if(v1.getValue().compareTo(v2.getValue()) != 0) {
				vertices = new ArrayList<>();
				vertices.add(v1);
				vertices.add(v2);
			}
			
			this.weight = weight;
		}

		public void setWeight(int weight) {
			this.weight = weight;
		}
		
		public int getWeight() {
			return weight;
		}

		public boolean containsVertex(Vertex v) {
			return vertices.contains(v);
		}
		
		public Vertex getOther(Vertex v) {
			int index = 0;
			if((index = vertices.indexOf(v)) != -1) {
				return vertices.get(index == 0 ? 1 : 0);
			}
			return null;
		}
		
		public List<Vertex> getVertices() {
			return vertices;
		}
		
		@Override
		public String toString() {
			String ret = "edge ";
			for(Vertex v : vertices) {
				ret += v.toString() + ",";
			}
			ret += weight + "\n";
			return ret;
		}
	}
}
