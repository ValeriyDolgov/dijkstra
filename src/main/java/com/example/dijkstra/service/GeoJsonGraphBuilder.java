package com.example.dijkstra.service;

import com.example.dijkstra.model.RoadSurfaceConfig;
import com.example.dijkstra.model.RoadTypeConfig;
import com.example.dijkstra.repository.RoadSurfaceConfigRepository;
import com.example.dijkstra.repository.RoadTypeConfigRepository;
import com.example.dijkstra.service.record.RoadRuleConfig;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.jgrapht.Graph;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class GeoJsonGraphBuilder {
    private final Map<String, Double> roadConfigCache = new HashMap<>();
    private final Graph<String, DefaultWeightedEdge> graph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
    private final Map<String, double[]> coordinatesMap = new HashMap<>();
    private final RoadSurfaceConfigRepository roadSurfaceConfigRepository;
    private final RoadTypeConfigRepository roadTypeConfigRepository;

    @PostConstruct
    public void init() throws IOException {
        preloadRoadConfigs();
        loadGeoJson("src/main/resources/roads.geojson");
    }

    public void preloadRoadConfigs() {
        List<RoadSurfaceConfig> surfaceConfigs = roadSurfaceConfigRepository.findAll();
        List<RoadTypeConfig> typeConfigs = roadTypeConfigRepository.findAll();

        for (var surfaceConfig : surfaceConfigs) {
            roadConfigCache.put(surfaceConfig.getSurface(), surfaceConfig.getMultiplier());
        }

        for (var typeConfig : typeConfigs) {
            roadConfigCache.put(typeConfig.getType(), typeConfig.getMultiplier());
        }
    }

    public void loadGeoJson(String filePath) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode root = objectMapper.readTree(new File(filePath));
        JsonNode features = root.get("features");

        for (JsonNode feature : features) {
            JsonNode geometry = feature.get("geometry");
            if (geometry == null || !geometry.get("type").asText().equals("LineString")) continue;

            JsonNode coords = geometry.get("coordinates");
            JsonNode properties = feature.get("properties");
            processRoadSegment(coords, properties);
        }
    }

    private void processRoadSegment(JsonNode coords, JsonNode properties) {
        String prevNode = null;
        double roadMultiplier = getRoadMultiplier(properties);

        for (JsonNode coord : coords) {
            double lon = coord.get(0).asDouble(); // Долгота
            double lat = coord.get(1).asDouble(); // Широта
            String nodeId = lat + "," + lon;

            coordinatesMap.put(nodeId, new double[]{lat, lon});
            graph.addVertex(nodeId);

            if (prevNode != null) {
                double distance = haversineDistance(coordinatesMap.get(prevNode), coordinatesMap.get(nodeId));
                double weightedDistance = distance * roadMultiplier;
                DefaultWeightedEdge edge = graph.addEdge(prevNode, nodeId);
                if (edge != null) graph.setEdgeWeight(edge, weightedDistance);
            }

            prevNode = nodeId;
        }
    }

    private double getRoadMultiplier(JsonNode properties) {
        if (properties == null) return 1.0;
        double multiplier = 1.0;
        String surface = properties.has("surface") ? properties.get("surface").asText() : "";
        int lanes = properties.has("lanes") ? properties.get("lanes").asInt(1) : 1;
        int maxspeed = properties.has("maxspeed") ? properties.get("maxspeed").asInt(50) : 50;
        String roadType = properties.has("highway") ? properties.get("highway").asText() : "unknown";

        multiplier *= roadConfigCache.getOrDefault(surface, 1.3);
        multiplier *= Math.max(1.0, 2.0 / lanes);
        multiplier *= 50.0 / maxspeed;
        multiplier *= roadConfigCache.getOrDefault(roadType, 1.4);

        return multiplier;
    }

    public List<double[]> findShortestPath(double[] start, double[] end) {
        String startNode = findNearestNode(start);
        String endNode = findNearestNode(end);

        DijkstraShortestPath<String, DefaultWeightedEdge> dijkstra = new DijkstraShortestPath<>(graph);
        List<String> path = dijkstra.getPath(startNode, endNode).getVertexList();

        List<double[]> correctedPath = new ArrayList<>();
        for (String node : path) {
            double[] coords = coordinatesMap.get(node);
            correctedPath.add(new double[]{coords[0], coords[1]}); // Меняем lat и lon местами
        }
        return correctedPath;
    }

    public void updateRoads() throws IOException {
        clearGraph();
        preloadRoadConfigs();
        loadGeoJson("src/main/resources/roads.geojson");
    }

    public void clearGraph() {
        graph.removeAllVertices(new HashSet<>(graph.vertexSet()));
        coordinatesMap.clear();
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