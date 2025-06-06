package com.example.dijkstra.service.mapper;

import com.example.dijkstra.controller.response.AllRouteResponse;
import com.example.dijkstra.controller.response.DetailRouteResponse;
import com.example.dijkstra.model.Route;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface RouteMapper {

    @Mapping(target = "driver.id", source = "driver.id")
    @Mapping(target = "manager.id", source = "manager.id")
    @Mapping(target = "driver.fio", source = "driver.fio")
    @Mapping(target = "manager.fio", source = "manager.fio")
    @Mapping(target = "status", source = "routeStatus.name")
    AllRouteResponse toAllRouteResponse(Route route);

    @Mapping(target = "driver.id", source = "driver.id")
    @Mapping(target = "manager.id", source = "manager.id")
    @Mapping(target = "driver.fio", source = "driver.fio")
    @Mapping(target = "manager.fio", source = "manager.fio")
    @Mapping(target = "status", source = "routeStatus.name")
    DetailRouteResponse toDetailRouteResponse(Route route);

    List<AllRouteResponse> toAllRouteResponseList(List<Route> routes);
}
