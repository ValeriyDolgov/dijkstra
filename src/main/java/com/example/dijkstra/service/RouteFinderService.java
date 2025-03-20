package com.example.dijkstra.service;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class RouteFinderService {
    private final SimpleWeightedGraph<String, DefaultWeightedEdge> graph;
    private final Map<String, double[]> nodeCoordinates;

    public RouteFinderService(SimpleWeightedGraph<String, DefaultWeightedEdge> graph, RoadGraph roadGraph) {
        this.graph = graph;
        this.nodeCoordinates = roadGraph.getNodeCoordinates();
    }

    public List<double[]> findShortestPath(double[] start, double[] end) {
        String startNode = findNearestNode(start);
        String endNode = findNearestNode(end);

        var dijkstra = new DijkstraShortestPath<>(graph);
        var path = dijkstra.getPath(startNode, endNode);

        return path.getVertexList().stream().map(nodeCoordinates::get).map(coords -> new double[]{coords[1], coords[0]}).toList();
    }

    private String findNearestNode(double[] point) {
        return nodeCoordinates.entrySet()
                              .stream()
                              .min((a, b) -> Double.compare(distance(point, a.getValue()), distance(point, b.getValue())))
                              .orElseThrow()
                              .getKey();
    }

    private double distance(double[] a, double[] b) {
        double latDiff = a[0] - b[0];
        double lonDiff = a[1] - b[1];
        return Math.sqrt(latDiff * latDiff + lonDiff * lonDiff);
    }
}
