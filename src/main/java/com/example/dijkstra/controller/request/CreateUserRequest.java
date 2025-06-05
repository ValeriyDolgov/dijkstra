package com.example.dijkstra.controller.request;

import lombok.Data;

@Data
public class CreateUserRequest {
    private String fio;
    private String email;
    private String password;
    private String mobileNumber;
    private String driverIdNumber;
}
