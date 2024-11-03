package com.example.dijkstra.service;

import com.example.dijkstra.model.Edge;
import com.example.dijkstra.model.Node;
import com.example.dijkstra.repository.EdgeRepository;
import com.example.dijkstra.repository.NodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class DijkstraService {

    private final NodeRepository nodeRepository;
    private final EdgeRepository edgeRepository;

    public String getShortestPathBetweenTwoNodes(String startNodeName, String endNodeName) {
        var startNode = nodeRepository.findByName(startNodeName)
                                      .orElseThrow(() -> new RuntimeException("Start node not found"));
        var endNode = nodeRepository.findByName(endNodeName)
                                    .orElseThrow(() -> new RuntimeException("End node not found"));

        // Рассчитываем путь в обоих направлениях и выбираем более короткий
        var pathForward = calculatePathWithDistance(startNode, endNode);
        var pathBackward = calculatePathWithDistance(endNode, startNode);

        // Выбираем путь с меньшим расстоянием
        return (pathForward.distance <= pathBackward.distance) ? pathForward.path : pathBackward.path;
    }

    private PathResult calculatePathWithDistance(Node startNode, Node endNode) {
        List<Node> shortestPathNodes = getShortestPath(startNode, endNode);
        int totalDistance = 0;

        if (shortestPathNodes.size() <= 1) {
            return new PathResult("Путь не найден", Integer.MAX_VALUE);
        }

        var path = new StringBuilder();

        for (int i = 0; i < shortestPathNodes.size() - 1; i++) {
            var currentNode = shortestPathNodes.get(i);
            var nextNode = shortestPathNodes.get(i + 1);

            path.append(currentNode.getName()).append(" -> ");

            // Получаем расстояние между текущим и следующим узлом
            int distance = edgeRepository.findBySourceAndDestination(currentNode, nextNode)
                                         .orElseThrow(() -> new RuntimeException("Edge not found")).getWeight();
            totalDistance += distance;
        }

        path.append(shortestPathNodes.get(shortestPathNodes.size() - 1).getName());
        path.append(" Расстояние: ").append(totalDistance).append(" м");

        return new PathResult(path.toString(), totalDistance);
    }

    private List<Node> getShortestPath(Node startNode, Node endNode) {
        Map<Node, Integer> distances = new HashMap<>();
        Map<Node, Node> previousNodes = new HashMap<>();
        PriorityQueue<Node> priorityQueue = new PriorityQueue<>(Comparator.comparingInt(distances::get));

        for (Node node : nodeRepository.findAll()) {
            distances.put(node, Integer.MAX_VALUE);
        }
        distances.put(startNode, 0);
        priorityQueue.add(startNode);

        while (!priorityQueue.isEmpty()) {
            Node currentNode = priorityQueue.poll();

            if (currentNode.equals(endNode)) {
                break;
            }

            for (Edge edge : edgeRepository.findBySource(currentNode)) {
                Node neighbor = edge.getDestination();
                int newDist = distances.get(currentNode) + edge.getWeight();

                if (newDist < distances.get(neighbor)) {
                    distances.put(neighbor, newDist);
                    previousNodes.put(neighbor, currentNode);
                    priorityQueue.add(neighbor);
                }
            }
        }

        List<Node> path = new ArrayList<>();
        for (Node node = endNode; node != null; node = previousNodes.get(node)) {
            path.add(node);
        }

        Collections.reverse(path);
        return path;
    }

    private static class PathResult {
        String path;
        int distance;

        PathResult(String path, int distance) {
            this.path = path;
            this.distance = distance;
        }
    }
}

