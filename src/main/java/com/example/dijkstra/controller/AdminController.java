package com.example.dijkstra.controller;

import com.example.dijkstra.controller.request.CreateManagerRequest;
import com.example.dijkstra.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('SUPER_ADMIN')")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;

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
}
