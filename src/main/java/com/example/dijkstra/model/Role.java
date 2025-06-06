package com.example.dijkstra.model;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    SUPER_ADMIN,
    MANAGER,
    DRIVER;

    @Override
    public String getAuthority() {
        return this.name();
    }
}