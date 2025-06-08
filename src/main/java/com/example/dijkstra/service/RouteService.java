package com.example.dijkstra.service;

import com.example.dijkstra.controller.request.CreateRouteRequest;
import com.example.dijkstra.controller.response.AllRouteResponse;
import com.example.dijkstra.controller.response.DetailRouteResponse;
import com.example.dijkstra.model.Route;
import com.example.dijkstra.model.RouteStatus;
import com.example.dijkstra.model.User;
import com.example.dijkstra.repository.RouteRepository;
import com.example.dijkstra.repository.RouteStatusRepository;
import com.example.dijkstra.service.mapper.RouteMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RouteService {
    private final RouteMapper routeMapper;
    private final RouteRepository routeRepository;
    private final RouteStatusRepository routeStatusRepository;
    private final GeocodingService geocodingService;
    private final GeoJsonGraphBuilder geoJsonGraphBuilder;

    public Route findById(Long id) {
        return routeRepository.findById(id).orElseThrow(() -> new RuntimeException("Route not found"));
    }

    public List<AllRouteResponse> findAll() {
        var allRoutes = routeRepository.findAll();
        return routeMapper.toAllRouteResponseList(allRoutes);
    }

    public List<AllRouteResponse> findAllDriverRoutes(User user) {
        var allRoutes = routeRepository.findAllByDriver(user);
        return routeMapper.toAllRouteResponseList(allRoutes);
    }

    public List<AllRouteResponse> findAllAvailableRoutes() {
        var allRoutes = routeRepository.findAllByStatus_Id(RouteStatus.STATUS_NEW);
        return routeMapper.toAllRouteResponseList(allRoutes);
    }

    @Transactional
    public DetailRouteResponse createRoute(CreateRouteRequest request, User manager) throws JsonProcessingException {
        var startAddress = geocodingService.reverseGeocode(request.getStartLat(), request.getStartLon());
        var endAddress = geocodingService.reverseGeocode(request.getEndLat(), request.getEndLon());
        var newRoute = new Route();
        newRoute.setStartAddress(startAddress);
        newRoute.setEndAddress(endAddress);
        newRoute.setWeight(request.getWeight());
        newRoute.setCargoName(request.getCargoName());
        newRoute.setStartDate(request.getStartDate());
        newRoute.setStatus(routeStatusRepository.findById(RouteStatus.STATUS_NEW).get());
        ObjectMapper mapper = new ObjectMapper();
        newRoute.setRouteCoords(mapper.readValue(request.getPathCoords(), new TypeReference<List<double[]>>() {}));
        newRoute.setManager(manager);
        var createdEntity = routeRepository.save(newRoute);
        return routeMapper.toDetailRouteResponse(createdEntity);
    }

    public void assignRoute(Long id, User driver) {
        var route = findById(id);
        route.setDriver(driver);
        route.setStatus(routeStatusRepository.findById(RouteStatus.STATUS_TAKEN).get());
        routeRepository.save(route);
    }

    public DetailRouteResponse getRoute(Long id) {
        var route = findById(id);
        return routeMapper.toDetailRouteResponse(route);
    }

}
