package com.example.dijkstra.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "road_config")
public class RoadConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String surface;
    private Integer lanes;
    private Integer maxspeed;
    private String roadType;
    private Double multiplier;
}

