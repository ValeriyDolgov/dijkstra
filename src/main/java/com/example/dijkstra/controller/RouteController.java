package com.example.dijkstra.controller;

import com.example.dijkstra.service.GeoJsonGraphBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/routes")
public class RouteController {
    private final GeoJsonGraphBuilder graphBuilder;

    @GetMapping
    public List<double[]> getRoute(@RequestParam double startLat, @RequestParam double startLon,
                                 @RequestParam double endLat, @RequestParam double endLon) {
        return graphBuilder.findShortestPath(new double[]{startLat, startLon}, new double[]{endLat, endLon});
    }
}