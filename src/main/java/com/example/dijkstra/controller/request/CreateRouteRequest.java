package com.example.dijkstra.controller.request;

import lombok.Data;

@Data
public class CreateRouteRequest {
    private Double startLat;
    private Double startLon;
    private Double endLat;
    private Double endLon;
    private String cargoName;
    private Double weight;
}
