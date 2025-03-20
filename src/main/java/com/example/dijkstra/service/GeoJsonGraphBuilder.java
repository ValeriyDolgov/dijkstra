package com.example.dijkstra.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jgrapht.Graph;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
public class GeoJsonGraphBuilder {
    private final Graph<String, DefaultWeightedEdge> graph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
    private final Map<String, double[]> coordinatesMap = new HashMap<>();

    public void loadGeoJson(String filePath) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode root = objectMapper.readTree(new File(filePath));
        JsonNode features = root.get("features");

        for (JsonNode feature : features) {
            JsonNode geometry = feature.get("geometry");
            if (geometry == null || !geometry.get("type").asText().equals("LineString")) continue;

            JsonNode coords = geometry.get("coordinates");
            processRoadSegment(coords);
        }
    }

    private void processRoadSegment(JsonNode coords) {
        String prevNode = null;
        for (JsonNode coord : coords) {
            double lon = coord.get(0).asDouble(); // Долгота
            double lat = coord.get(1).asDouble(); // Широта
            String nodeId = lat + "," + lon;

            coordinatesMap.put(nodeId, new double[]{lat, lon});
            graph.addVertex(nodeId);

            if (prevNode != null) {
                double distance = haversineDistance(coordinatesMap.get(prevNode), coordinatesMap.get(nodeId));
                DefaultWeightedEdge edge = graph.addEdge(prevNode, nodeId);
                if (edge != null) graph.setEdgeWeight(edge, distance);
            }

            prevNode = nodeId;
        }
    }

    public List<double[]> findShortestPath(double[] start, double[] end) {
        String startNode = findNearestNode(start);
        String endNode = findNearestNode(end);

        DijkstraShortestPath<String, DefaultWeightedEdge> dijkstra = new DijkstraShortestPath<>(graph);
        List<String> path = dijkstra.getPath(startNode, endNode).getVertexList();

        List<double[]> correctedPath = new ArrayList<>();
        for (String node : path) {
            double[] coords = coordinatesMap.get(node);
            correctedPath.add(new double[]{coords[1], coords[0]}); // Меняем lat и lon местами
        }
        return correctedPath;
    }

    private String findNearestNode(double[] point) {
        return coordinatesMap.entrySet()
                             .stream()
                             .min(Comparator.comparingDouble(e -> haversineDistance(point, e.getValue())))
                             .map(Map.Entry::getKey)
                             .orElseThrow();
    }

    private double haversineDistance(double[] p1, double[] p2) {
        final int R = 6371; // Радиус Земли в км
        double dLat = Math.toRadians(p2[0] - p1[0]);
        double dLon = Math.toRadians(p2[1] - p1[1]);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                   Math.cos(Math.toRadians(p1[0])) * Math.cos(Math.toRadians(p2[0])) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        return R * (2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a)));
    }
}