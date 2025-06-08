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
@RequestMapping("/manager")
@PreAuthorize("hasAnyAuthority('MANAGER', 'SUPER_ADMIN')")
@RequiredArgsConstructor
public class ManagerController {

    private final RouteService routeService;

    @GetMapping
    public String mainPage() {
        return "manager/index";
    }

    @GetMapping("/routes")
    public String getAllNotModeratedCourses(Model model) {
        var allNotModeratedPrograms = routeService.findAll();
        model.addAttribute("routes", allNotModeratedPrograms);
        return "manager/all-routes";
    }

    @GetMapping("/routes/{routeId}")
    public String showEdProgramModerateForm(@PathVariable Long routeId, Model model) {
        DetailRouteResponse routeData;
        try {
            routeData = routeService.getRoute(routeId);
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "manager/route";
        }
        model.addAttribute("route", routeData);
        return "manager/route";
    }

    @GetMapping("/create/route")
    public String showEdProgramForm(Model model) {
        var request = new CreateRouteRequest();
        model.addAttribute("route", request);
        return "manager/create-route";
    }

    @PostMapping("/create/route")
    public String saveNewEdProgram(User user, Model model, @ModelAttribute CreateRouteRequest request) {
        DetailRouteResponse response = null;
        try {
            response = routeService.createRoute(request, user);
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "manager/create-route";
        }
        model.addAttribute("route", response);
        return "redirect:/manager/routes/" + response.getId();
    }

}
