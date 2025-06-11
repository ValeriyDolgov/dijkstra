package com.example.dijkstra.repository;

import com.example.dijkstra.model.RoadSurfaceConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoadSurfaceConfigRepository extends JpaRepository<RoadSurfaceConfig, Long> {
}
