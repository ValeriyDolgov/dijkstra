package com.example.dijkstra.controller;

import com.example.dijkstra.service.DijkstraService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/graph")
public class GraphController {

    private final DijkstraService dijkstraService;

    @GetMapping("/shortest-path")
    public String getShortestPath(@RequestParam String startNode, @RequestParam String endNode) {
        return dijkstraService.getShortestPathBetweenTwoNodes(startNode, endNode);
    }
}
