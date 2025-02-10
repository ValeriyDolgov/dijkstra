package com.example.dijkstra.controller.request;

import lombok.Data;

import java.util.List;

@Data
public class RouteRequest {
    private List<String> cityNames;
    private String startCityName;
    private String endCityName;
}
