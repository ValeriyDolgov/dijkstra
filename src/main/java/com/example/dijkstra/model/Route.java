package com.example.dijkstra.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Route {
    private City startCity;
    private City endCity;
    private double distance;
}
