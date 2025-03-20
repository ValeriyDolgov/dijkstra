package com.example.dijkstra.config;

import com.example.dijkstra.service.RoadGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class AppConfig {
    @Bean
    public SimpleWeightedGraph<String, DefaultWeightedEdge> roadGraphBean(RoadGraph roadGraph) throws IOException {
        roadGraph.loadGeoJson("src/main/resources/roads.geojson");
        return roadGraph.getGraph();
    }
}
