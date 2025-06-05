package com.example.dijkstra.model;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    SUPER_ADMIN,
    ADMIN,
    MANAGER,
    OPERATOR,
    DRIVER;

    @Override
    public String getAuthority() {
        return this.name();
    }
}