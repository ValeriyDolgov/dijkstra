package com.example.dijkstra.service;

import com.example.dijkstra.model.RoadConfig;
import com.example.dijkstra.repository.RoadConfigRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoadConfigService {
    private final RoadConfigRepository roadConfigRepository;
    private final GeoJsonGraphBuilder geoJsonGraphBuilder;

    public List<RoadConfig> findAll() {
        return roadConfigRepository.findAll();
    }

    public RoadConfig findById(Long id) {
        return roadConfigRepository.findById(id).orElseThrow();
    }

    @Transactional
    public void updateRoadConfig(long id, String surface, String roadType, Integer lanes, Integer maxspeed) throws IOException {
        // Установка значений по умолчанию
        int safeLanes = (lanes != null && lanes > 0) ? lanes : 1;
        int safeMaxspeed = (maxspeed != null && maxspeed > 0) ? maxspeed : 50;

        var config = roadConfigRepository.findById(id).orElseThrow();

        double multiplier = calculateMultiplier(surface, roadType, safeLanes, safeMaxspeed);
        config.setLanes(safeLanes);
        config.setMaxspeed(safeMaxspeed);
        config.setSurface(surface);
        config.setMultiplier(multiplier);
        config.setRoadType(roadType);
        roadConfigRepository.save(config);
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

