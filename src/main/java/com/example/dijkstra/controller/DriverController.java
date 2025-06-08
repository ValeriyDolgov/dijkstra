package com.example.dijkstra.controller;

import com.example.dijkstra.controller.request.CreateRouteRequest;
import com.example.dijkstra.controller.response.DetailRouteResponse;
import com.example.dijkstra.model.User;
import com.example.dijkstra.service.RouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/driver")
@PreAuthorize("hasAuthority('DRIVER')")
@RequiredArgsConstructor
public class DriverController {

    private final RouteService routeService;

    @GetMapping
    public String mainPage() {
        return "driver/index";
    }

    @GetMapping("/routes")
    public String getAllNotModeratedCourses(Model model) {
        var allNotModeratedPrograms = routeService.findAll();
        model.addAttribute("programs", allNotModeratedPrograms);
        return "driver/routes";
    }

    @GetMapping("/routes/{routeId}")
    public String showEdProgramModerateForm(@PathVariable Long routeId, Model model) {
        DetailRouteResponse routeData;
        try {
            routeData = routeService.getRoute(routeId);
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "driver/create-route";
        }
        model.addAttribute("route", routeData);
        return "driver/create-route";
    }

    @PostMapping("/routes/{routeId}")
    public String saveNewEdProgram(User user, Model model, @PathVariable Long routeId) {
        try {
            routeService.assignRoute(routeId, user);
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
        }
        return "create-ed-program";
    }
}
