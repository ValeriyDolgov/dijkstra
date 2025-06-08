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
        return "driver/index";
    }

    @GetMapping("/create/manager")
    public String showEdProgramForm(Model model) {
        var request = new CreateManagerRequest();
        model.addAttribute("manager", request);
        return "create-ed-program";
    }

    @PostMapping("/create/route")
    public String saveNewEdProgram(Model model, @ModelAttribute CreateManagerRequest request) {
        String response = null;
        try {
            response = userService.createManagerUser(request);
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
        }
        model.addAttribute("password", response);
        return "create-ed-program";
    }

}
