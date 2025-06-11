package com.example.dijkstra.service;

import com.example.dijkstra.model.RoadConfig;
import com.example.dijkstra.repository.RoadConfigRepository;
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
    private final Map<String, RoadRuleConfig> roadConfigCache = new HashMap<>();
    private final Graph<String, DefaultWeightedEdge> graph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
    private final Map<String, double[]> coordinatesMap = new HashMap<>();
    private final RoadConfigRepository roadConfigRepository;

    @PostConstruct
    public void init() throws IOException {
        preloadRoadConfigs();
        loadGeoJson("src/main/resources/roads.geojson");
    }

    public void preloadRoadConfigs() {
        List<RoadConfig> configs = roadConfigRepository.findAll();

        for (RoadConfig config : configs) {
            String key = (config.getSurface() + "|" + config.getRoadType() + "|" + config.getLanes() + "|" + config.getMaxspeed()).toLowerCase();

            roadConfigCache.put(key, new RoadRuleConfig(
                    config.getSurface(),
                    config.getRoadType(),
                    config.getLanes(),
                    config.getMaxspeed(),
                    config.getMultiplier()
            ));
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

        String surface = properties.has("surface") ? properties.get("surface").asText().toLowerCase() : "unknown";
        String roadType = properties.has("highway") ? properties.get("highway").asText().toLowerCase() : "unknown";
        Integer lanes = properties.has("lanes") && properties.get("lanes").canConvertToInt() ? properties.get("lanes").asInt() : null;
        Integer maxspeed = properties.has("maxspeed") && properties.get("maxspeed").canConvertToInt() ? properties.get("maxspeed").asInt() : null;

        RoadRuleConfig config = getRoadRuleConfig(surface, roadType, lanes, maxspeed);
        return config.multiplier();
    }

    private RoadRuleConfig getRoadRuleConfig(String surface, String roadType, Integer lanes, Integer maxspeed) {
        List<String> fallbackKeys = List.of(
                buildKey(surface, roadType, lanes, maxspeed),
                buildKey(surface, roadType, lanes, null),
                buildKey(surface, roadType, null, null)
        );

        for (String key : fallbackKeys) {
            if (roadConfigCache.containsKey(key)) {
                return roadConfigCache.get(key);
            }
        }

        // Возврат дефолтного значения
        return new RoadRuleConfig(surface, roadType, lanes != null ? lanes : 1, maxspeed != null ? maxspeed : 50, 1.3);
    }

    private String buildKey(String surface, String roadType, Integer lanes, Integer maxspeed) {
        return String.format("%s|%s|%s|%s",
                             surface != null ? surface : "unknown",
                             roadType != null ? roadType : "unknown",
                             lanes != null ? lanes : "null",
                             maxspeed != null ? maxspeed : "null"
        ).toLowerCase();
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