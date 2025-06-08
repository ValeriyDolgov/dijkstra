package com.example.dijkstra.controller;

import com.example.dijkstra.model.Role;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String index(Authentication authentication) {
        if (authentication == null) {
            return "redirect:/login";
        } else if (authentication.getAuthorities().contains(Role.SUPER_ADMIN) || authentication.getAuthorities().contains(Role.MANAGER)) {
            return "redirect:/manager";
        } else {
            return "redirect:/driver";
        }
    }
}
