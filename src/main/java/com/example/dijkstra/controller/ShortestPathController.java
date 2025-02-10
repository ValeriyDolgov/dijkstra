package com.example.dijkstra.controller;

import com.example.dijkstra.controller.request.RouteRequest;
import com.example.dijkstra.model.City;
import com.example.dijkstra.service.ShortestPathService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/route")
@RequiredArgsConstructor
public class ShortestPathController {

    private final ShortestPathService routeService;

    @PostMapping("/optimize")
    public List<City> optimizeRoute(@RequestBody RouteRequest request) {
        return routeService.findOptimizedRoute(request.getCityNames(), request.getStartCityName(), request.getEndCityName());
    }

}
