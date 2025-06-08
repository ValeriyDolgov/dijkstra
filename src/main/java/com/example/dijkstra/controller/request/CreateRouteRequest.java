package com.example.dijkstra.controller.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateRouteRequest {
    private Double startLat;
    private Double startLon;
    private Double endLat;
    private Double endLon;
    private String pathCoords;
    private String cargoName;
    private Double weight;
    private LocalDateTime startDate;
}
