package com.example.dijkstra.controller.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NominatimResponse {
    @JsonProperty("display_name")
    private String displayName;

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}

