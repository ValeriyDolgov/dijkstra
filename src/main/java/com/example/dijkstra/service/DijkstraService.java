package com.example.dijkstra.service;


import com.example.dijkstra.model.City;
import com.example.dijkstra.model.Route;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class DijkstraService {

    public List<City> findShortestPath(List<City> cities, List<Route> routes, City start, City end) {
        Map<City, Double> distances = new HashMap<>();
        Map<City, City> previous = new HashMap<>();
        PriorityQueue<City> queue = new PriorityQueue<>(Comparator.comparingDouble(distances::get));

        // Initialize distances
        for (City city : cities) {
            if (city.equals(start)) {
                distances.put(city, 0.0);
            } else {
                distances.put(city, Double.MAX_VALUE);
            }
            queue.add(city);
        }

        // Dijkstra's algorithm
        while (!queue.isEmpty()) {
            City current = queue.poll();

            // Stop if we reach the destination
            if (current.equals(end)) {
                break;
            }

            // Find all neighbors of the current city
            for (Route route : routes) {
                if (route.getStartCity().equals(current)) {
                    City neighbor = route.getEndCity();
                    double newDistance = distances.get(current) + route.getDistance();

                    // Update the distance if a shorter path is found
                    if (newDistance < distances.get(neighbor)) {
                        distances.put(neighbor, newDistance);
                        previous.put(neighbor, current);
                        // Re-add the neighbor to the queue to re-evaluate its neighbors
                        queue.remove(neighbor);
                        queue.add(neighbor);
                    }
                }
            }
        }

        // Build the shortest path
        List<City> path = new ArrayList<>();
        for (City city = end; city != null; city = previous.get(city)) {
            path.add(city);
        }
        Collections.reverse(path);

        return path;
    }

    public List<Route> generateRoutes(List<City> cities, double maxDistance) {
        List<Route> routes = new ArrayList<>();

        for (int i = 0; i < cities.size(); i++) {
            for (int j = i + 1; j < cities.size(); j++) {
                City city1 = cities.get(i);
                City city2 = cities.get(j);

                double distance = this.calculateDistance(city1, city2);

                if (distance <= maxDistance) {
                    routes.add(new Route(city1, city2, distance));
                    routes.add(new Route(city2, city1, distance)); // Add bidirectional route
                }
            }
        }

        return routes;
    }

    public List<City> findShortestPath(List<City> cities, City start, City end) {
        Map<City, Double> distances = new HashMap<>();
        Map<City, City> previous = new HashMap<>();
        PriorityQueue<City> queue = new PriorityQueue<>(Comparator.comparingDouble(distances::get));

        // Initialize distances
        for (City city : cities) {
            if (city.equals(start)) {
                distances.put(city, 0.0);
            } else {
                distances.put(city, Double.MAX_VALUE);
            }
            queue.add(city);
        }

        // Dijkstra's algorithm
        while (!queue.isEmpty()) {
            City current = queue.poll();

            // Stop if we reach the destination
            if (current.equals(end)) {
                break;
            }

            // Find all neighbors (all other cities)
            for (City neighbor : cities) {
                if (!neighbor.equals(current)) {
                    double distance = calculateDistance(current, neighbor);
                    double newDistance = distances.get(current) + distance;

                    // Update the distance if a shorter path is found
                    if (newDistance < distances.get(neighbor)) {
                        distances.put(neighbor, newDistance);
                        previous.put(neighbor, current);
                        // Re-add the neighbor to the queue to re-evaluate its neighbors
                        queue.remove(neighbor);
                        queue.add(neighbor);
                    }
                }
            }
        }

        // Build the shortest path
        List<City> path = new ArrayList<>();
        for (City city = end; city != null; city = previous.get(city)) {
            path.add(city);
        }
        Collections.reverse(path);

        return path;
    }

    private double calculateDistance(City city1, City city2) {
        double lat1 = Math.toRadians(city1.getLatitude());
        double lon1 = Math.toRadians(city1.getLongitude());
        double lat2 = Math.toRadians(city2.getLatitude());
        double lon2 = Math.toRadians(city2.getLongitude());

        double dLat = lat2 - lat1;
        double dLon = lon2 - lon1;

        double a = Math.pow(Math.sin(dLat / 2), 2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(dLon / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double radius = 6371; // Earth's radius in kilometers
        return radius * c;
    }
}
