package com.example.dijkstra.repository;

import com.example.dijkstra.model.Route;
import com.example.dijkstra.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RouteRepository extends JpaRepository<Route, Long> {

    List<Route> findAllByStatus_Id(Long id);
    List<Route> findAllByDriver(User driver);
}
