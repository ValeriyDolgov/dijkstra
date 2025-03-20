package com.example.dijkstra.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class RoadGraph {
    private final SimpleWeightedGraph<String, DefaultWeightedEdge> graph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
    private final Map<String, double[]> nodeCoordinates = new HashMap<>();

    public void loadGeoJson(String filePath) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode root = objectMapper.readTree(new File(filePath));

        for (JsonNode feature : root.get("features")) {
            JsonNode geometry = feature.get("geometry");
            if (geometry != null && "LineString".equals(geometry.get("type").asText())) {
                JsonNode coordinates = geometry.get("coordinates");
                for (int i = 0; i < coordinates.size() - 1; i++) {
                    double[] start = parseCoordinates(coordinates.get(i));
                    double[] end = parseCoordinates(coordinates.get(i + 1));

                    String startNode = start[0] + "," + start[1];
                    String endNode = end[0] + "," + end[1];

                    graph.addVertex(startNode);
                    graph.addVertex(endNode);
                    DefaultWeightedEdge edge = graph.addEdge(startNode, endNode);
                    if (edge != null) {
                        double weight = calculateDistance(start, end);
                        graph.setEdgeWeight(edge, weight);
                    }

                    nodeCoordinates.put(startNode, start);
                    nodeCoordinates.put(endNode, end);
                }
            }
        }
    }

    private double[] parseCoordinates(JsonNode node) {
        return new double[]{node.get(1).asDouble(), node.get(0).asDouble()}; // Lat, Lon
    }

    private double calculateDistance(double[] a, double[] b) {
        double latDiff = a[0] - b[0];
        double lonDiff = a[1] - b[1];
        return Math.sqrt(latDiff * latDiff + lonDiff * lonDiff);
    }

    public SimpleWeightedGraph<String, DefaultWeightedEdge> getGraph() {
        return graph;
    }

    public Map<String, double[]> getNodeCoordinates() {
        return nodeCoordinates;
    }
}
