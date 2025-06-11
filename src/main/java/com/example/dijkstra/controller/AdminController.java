package com.example.dijkstra.controller;

import com.example.dijkstra.controller.request.CreateManagerRequest;
import com.example.dijkstra.controller.request.UpdateRoadConfigRequest;
import com.example.dijkstra.model.RoadConfig;
import com.example.dijkstra.service.RoadConfigService;
import com.example.dijkstra.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('SUPER_ADMIN')")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;
    private final RoadConfigService roadConfigService;

    @GetMapping
    public String mainPage() {
        return "admin/index";
    }

    @GetMapping("/create/manager")
    public String showEdProgramForm(Model model) {
        var request = new CreateManagerRequest();
        model.addAttribute("manager", request);
        return "admin/create-manager";
    }

    @PostMapping("/create/manager")
    public String saveNewEdProgram(Model model, @ModelAttribute CreateManagerRequest request) {
        try {
            userService.createManagerUser(request);
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "admin/create-manager";
        }
        return "admin/index";
    }

    @GetMapping("/road-config/all")
    public String showAllRoadConfigs(Model model) {
        model.addAttribute("roadConfigs", roadConfigService.findAll());
        return "admin/all-road-configs";
    }

    @GetMapping("/road-config/{id}")
    public String showAllRoadConfigs(@PathVariable Long id, Model model) {
        var config = roadConfigService.findById(id);
        model.addAttribute("config", config);
        return "admin/config";
    }

    @GetMapping("/road-config/{id}/update")
    public String updateRoadConfig(@PathVariable Long id, Model model) {
        var config = roadConfigService.findById(id);
        model.addAttribute("config", config);
        return "admin/update-config";
    }

    @PostMapping("/road-config/{id}/update")
    public String updateRoadConfig(@PathVariable Long id, @ModelAttribute RoadConfig request) throws IOException {
        roadConfigService.updateRoadConfig(request.getId(),
                                           request.getSurface(),
                                           request.getRoadType(),
                                           request.getLanes(),
                                           request.getMaxspeed());
        return "redirect:/admin/road-config/" + id;
    }
}
