package fr.isep.tp6;

import java.util.*;

public class Dijkstra {
	WeightedGraph graph;
	Map<String, Boolean> marked;
	Map<String, String> previous;
	Map<String, Double> distance;


	public Dijkstra(WeightedGraph graph, String s) {
		this.graph = graph;
		this.marked = new HashMap<>(graph.vertices.size());
		this.previous = new HashMap<>(graph.vertices.size());
		this.distance = new HashMap<>(graph.vertices.size());

		distance.put(s, 0.0);

		for(int count = 0; count < graph.vertices.size()-1; count++) {
			String current = nodeAtMinDistance(graph.vertices.keySet());
			marked.put(current, true);

			graph.vertices.get(current).getEdges().forEach((destination, edge) -> {
				if(!marked.getOrDefault(destination,false) &&
						distance.getOrDefault(edge.from, Double.MAX_VALUE) != Integer.MAX_VALUE &&
						distance.getOrDefault(edge.from, Double.MAX_VALUE) + ((WeightedEdge) edge).weight < distance.getOrDefault(edge.to, Double.MAX_VALUE)
				){
					previous.put(destination, edge.from);
					distance.put(destination, distance.get(edge.from) + ((WeightedEdge) edge).weight);
				}
			});
		}
	}

	private String nodeAtMinDistance(Set<String> nodes){
		double min_dist = Double.MAX_VALUE;
		String min_node = "";

		for(String node: nodes){
			if(!marked.getOrDefault(node, false) && distance.getOrDefault(node, Double.MAX_VALUE) <= min_dist){
				min_dist = distance.getOrDefault(node, Double.MAX_VALUE);
				min_node = node;
			}
		}

		return min_node;
	}

	public void verifyNonNegative(WeightedGraph g) {
		g.vertices.forEach((k, vertex) -> {
			vertex.getEdges().forEach((dst, edge) -> {
				if (((WeightedEdge) edge).weight < 0){
					throw new IllegalArgumentException("Negative weight");
				}
			});
		});
	}

	public Boolean hasPathTo(String v) {
		return marked.getOrDefault(v, false);
	}

	public Double distTo(String v) {
		return distance.getOrDefault(v, Double.MAX_VALUE);
	}

	public List<WeightedEdge> getShortesPath(String v){
		LinkedList<WeightedEdge> path = new LinkedList<>();
		String crawl = v;
		String crawl2 = previous.get(crawl);;

		while (crawl2 != null) {
			path.add((WeightedEdge) graph.vertices.get(crawl2).getEdges().get(crawl));

			crawl = crawl2;
			crawl2 = previous.get(crawl2);
		}

		return path;
	}

	public void printSP(String v) {
		List<WeightedEdge> path = getShortesPath(v);

		System.out.println("Path is :");
		for (int i = path.size() - 1; i >= 0; i--) {
			System.out.print(path.get(i).from + " ->" + path.get(i).to + " ");
		}
	}
}
