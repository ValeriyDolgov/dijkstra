package com.example.dijkstra.repository;

import com.example.dijkstra.model.RoadSurfaceConfig;
import com.example.dijkstra.model.RoadTypeConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoadTypeConfigRepository extends JpaRepository<RoadTypeConfig, Long> {
}
