package com.example.dijkstra.controller.request;

import lombok.Data;

@Data
public class UpdateRoadConfigRequest {
    private int id;
    private String surface;
    private String roadType;
    private Integer lanes;
    private Integer maxspeed;
}
