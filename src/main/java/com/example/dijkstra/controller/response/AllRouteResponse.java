package com.example.dijkstra.controller.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AllRouteResponse {
    private Long id;
    private String cargoName;
    private String startAddress;
    private String endAddress;
    private LocalDateTime startDate;
    private Double weight;
    private DriverRouteResponse driver;
    private DriverRouteResponse manager;
    private String status;
}
