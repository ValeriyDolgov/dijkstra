package com.example.dijkstra.controller.request;

import lombok.Data;

@Data
public class CreateManagerRequest {
    private String fio;
    private String email;
    private String mobileNumber;
    private String password;
}
