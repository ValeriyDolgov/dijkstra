package com.example.dijkstra.controller;

import com.example.dijkstra.controller.request.CreateManagerRequest;
import com.example.dijkstra.model.RoadSurfaceConfig;
import com.example.dijkstra.model.RoadTypeConfig;
import com.example.dijkstra.repository.RoadSurfaceConfigRepository;
import com.example.dijkstra.repository.RoadTypeConfigRepository;
import com.example.dijkstra.service.RoadConfigService;
import com.example.dijkstra.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('SUPER_ADMIN')")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;
    private final RoadConfigService roadConfigService;
    private final RoadTypeConfigRepository roadTypeConfigRepository;
    private final RoadSurfaceConfigRepository roadSurfaceConfigRepository;

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
        var form =  new RoadConfigForm();
        form.setSurfaceConfigs(roadSurfaceConfigRepository.findAll());
        form.setTypeConfigs(roadTypeConfigRepository.findAll());
        model.addAttribute("configs", form);
        return "admin/all-road-configs";
    }

    @GetMapping("/road-config/update")
    public String updateRoadConfig(Model model) {
        var form =  new RoadConfigForm();
        form.setSurfaceConfigs(roadSurfaceConfigRepository.findAll());
        form.setTypeConfigs(roadTypeConfigRepository.findAll());
        model.addAttribute("configs", form);
        return "admin/update-config";
    }

    @PostMapping("/road-config/update")
    public String updateRoadConfig(@ModelAttribute RoadConfigForm roadConfigForm) throws IOException {
        roadConfigService.updateRoadConfig(roadConfigForm.getTypeConfigs(), roadConfigForm.getSurfaceConfigs());
        return "redirect:/admin/road-config/all";
    }

    public class RoadConfigForm {
        private List<RoadSurfaceConfig> surfaceConfigs;
        private List<RoadTypeConfig> typeConfigs;

        // Обязательно геттеры и сеттеры
        public List<RoadSurfaceConfig> getSurfaceConfigs() {
            return surfaceConfigs;
        }

        public void setSurfaceConfigs(List<RoadSurfaceConfig> surfaceConfigs) {
            this.surfaceConfigs = surfaceConfigs;
        }

        public List<RoadTypeConfig> getTypeConfigs() {
            return typeConfigs;
        }

        public void setTypeConfigs(List<RoadTypeConfig> typeConfigs) {
            this.typeConfigs = typeConfigs;
        }
    }

}
