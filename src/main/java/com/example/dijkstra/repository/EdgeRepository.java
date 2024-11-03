package com.example.dijkstra.repository;

import com.example.dijkstra.model.Edge;
import com.example.dijkstra.model.Node;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EdgeRepository extends JpaRepository<Edge, Long> {
    List<Edge> findBySource(Node currentNode);

    Optional<Edge> findBySourceAndDestination(Node currentNode, Node nextNode);
}
