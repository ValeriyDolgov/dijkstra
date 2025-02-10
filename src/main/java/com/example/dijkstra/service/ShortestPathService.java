package com.example.dijkstra.service;

import com.example.dijkstra.model.City;
import com.example.dijkstra.model.Route;
import com.example.dijkstra.repository.CityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShortestPathService {

    private final CityRepository cityRepository;
    private final DijkstraService dijkstraService;

    public List<City> findOptimizedRoute(String startCityName, String endCityName) {
        // Fetch cities from the database
        var cities = cityRepository.findAll();
        // Find start and end cities
        City start = cities.stream()
                .filter(city -> city.getName().equals(startCityName))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Start city not found"));

        City end = cities.stream()
                .filter(city -> city.getName().equals(endCityName))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("End city not found"));

        // Generate routes dynamically
        List<Route> routes = dijkstraService.generateRoutes(cities, 500); // Max distance = 200 km

        // Find the shortest path
        return dijkstraService.findShortestPath(cities, routes, start, end);
    }

//    public List<City> findOptimizedRoute(List<String> cityNames, String startCityName, String endCityName) {
//        // Fetch cities from the database
//        List<City> cities = cityRepository.findByNameIn(cityNames);
//
//        // Find start and end cities
//        City start = cities.stream()
//                .filter(city -> city.getName().equals(startCityName))
//                .findFirst()
//                .orElseThrow(() -> new RuntimeException("Start city not found"));
//
//        City end = cities.stream()
//                .filter(city -> city.getName().equals(endCityName))
//                .findFirst()
//                .orElseThrow(() -> new RuntimeException("End city not found"));
//
//        // Find the shortest path
//        return dijkstraService.findShortestPath(cities, start, end);
//    }
}
