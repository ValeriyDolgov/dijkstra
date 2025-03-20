package com.example.dijkstra.controller;

import com.example.dijkstra.service.RouteFinderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/routes")
public class RouteController {
    private final RouteFinderService routeFinderService;

    public RouteController(RouteFinderService routeFinderService) {
        this.routeFinderService = routeFinderService;
    }

    @GetMapping
    public List<double[]> getRoute(@RequestParam double startLat, @RequestParam double startLon, @RequestParam double endLat,
                                   @RequestParam double endLon) {
        return routeFinderService.findShortestPath(new double[]{startLat, startLon}, new double[]{endLat, endLon});
    }
}