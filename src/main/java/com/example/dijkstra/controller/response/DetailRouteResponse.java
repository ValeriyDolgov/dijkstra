package com.example.dijkstra.controller.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class DetailRouteResponse {
    private Long id;
    private String cargoName;
    private String startAddress;
    private String endAddress;
    private LocalDateTime startDate;
    private Double weight;
    private DriverRouteResponse driver;
    private DriverRouteResponse manager;
    private List<double[]> routeCoords;
    private String status;
}
