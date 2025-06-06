package com.example.dijkstra.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/driver")
@PreAuthorize("hasAuthority('DRIVER')")
@RequiredArgsConstructor
public class DriverController {}
