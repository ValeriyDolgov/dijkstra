package com.example.dijkstra.service;

import com.example.dijkstra.model.RoadSurfaceConfig;
import com.example.dijkstra.model.RoadTypeConfig;
import com.example.dijkstra.repository.RoadSurfaceConfigRepository;
import com.example.dijkstra.repository.RoadTypeConfigRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoadConfigService {
    private final RoadSurfaceConfigRepository roadSurfaceConfigRepository;
    private final RoadTypeConfigRepository roadTypeConfigRepository;
    private final GeoJsonGraphBuilder geoJsonGraphBuilder;

    public RoadSurfaceConfig findById(Long id) {
        return roadSurfaceConfigRepository.findById(id).orElseThrow();
    }

    @Transactional
    public void updateRoadConfig(List<RoadTypeConfig> typeConfigs, List<RoadSurfaceConfig> surfaceConfigs) throws IOException {
        // Установка значений по умолчанию
        roadSurfaceConfigRepository.saveAll(surfaceConfigs);
        roadTypeConfigRepository.saveAll(typeConfigs);
        geoJsonGraphBuilder.updateRoads();
    }

    private double calculateMultiplier(String surface, String roadType, int lanes, int maxspeed) {
        double multiplier = 1.0;

        // Surface
        switch (surface != null ? surface : "") {
            case "asphalt", "paved" -> multiplier *= 1.0;
            case "gravel"           -> multiplier *= 1.2;
            case "dirt"             -> multiplier *= 1.5;
            default                 -> multiplier *= 1.3;
        }

        multiplier *= Math.max(1.0, 2.0 / lanes);
        multiplier *= 50.0 / (maxspeed > 0 ? maxspeed : 50);

        // Road type
        switch (roadType != null ? roadType : "") {
            case "motorway"     -> multiplier *= 0.9;
            case "primary"      -> multiplier *= 1.0;
            case "secondary"    -> multiplier *= 1.1;
            case "tertiary"     -> multiplier *= 1.2;
            case "residential"  -> multiplier *= 1.3;
            default             -> multiplier *= 1.4;
        }

        return multiplier;
    }
}

