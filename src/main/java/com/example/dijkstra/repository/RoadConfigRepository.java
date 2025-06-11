package com.example.dijkstra.repository;

import com.example.dijkstra.model.RoadConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoadConfigRepository extends JpaRepository<RoadConfig, Long> {
}
