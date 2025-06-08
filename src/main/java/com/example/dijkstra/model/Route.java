package com.example.dijkstra.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "route")
public class Route {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "route_coords", columnDefinition = "jsonb")
    private List<double[]> routeCoords;
    @Column(name = "start_address")
    private String startAddress;
    @Column(name = "end_address")
    private String endAddress;
    @Column(name = "start_date")
    private LocalDateTime startDate;
    @Column(name = "cargo_name")
    private String cargoName;
    @Column(name = "weight")
    private Double weight;
    @ManyToOne(fetch = FetchType.EAGER)
    private RouteStatus status;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "driver_id")
    private User driver;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "manager_id")
    private User manager;
}
