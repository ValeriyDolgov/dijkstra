package com.example.dijkstra.service;

import com.example.dijkstra.controller.response.NominatimResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class GeocodingService {

    private final RestTemplate restTemplate;

    public String reverseGeocode(double latitude, double longitude) {
        String url = UriComponentsBuilder.fromUriString("https://nominatim.openstreetmap.org/reverse")
                                         .queryParam("format", "json")
                                         .queryParam("lat", latitude)
                                         .queryParam("lon", longitude)
                                         .queryParam("addressdetails", 1)
                                         .queryParam("zoom", 18)
                                         .queryParam("accept-language", "ru") // чтобы получить адрес на русском
                                         .toUriString();

        var headers = new org.springframework.http.HttpHeaders();
        headers.set("User-Agent", "MySpringApp/1.0 (your@email.com)"); // обязателен для Nominatim
        var entity = new org.springframework.http.HttpEntity<>(headers);

        var response = restTemplate.exchange(
                url,
                org.springframework.http.HttpMethod.GET,
                entity,
                NominatimResponse.class
        );

        var body = response.getBody();
        return body != null ? body.getDisplayName() : "Адрес не найден";
    }
}

