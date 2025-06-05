package com.example.dijkstra.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "route_status")
public class RouteStatus {

    public static final Long STATUS_NEW = 1L;
    public static final Long STATUS_TAKEN = 2L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
}
