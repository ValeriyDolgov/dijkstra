package com.example.dijkstra.repository;

import com.example.dijkstra.model.Node;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NodeRepository extends JpaRepository<Node, Long> {
    Optional<Node> findByName(String startNodeName);
}
