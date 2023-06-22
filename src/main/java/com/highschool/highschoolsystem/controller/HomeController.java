package com.highschool.highschoolsystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class HomeController {
    @GetMapping({"", "/home"})
    public String index(@RequestParam(value = "component", defaultValue = "", required = true) String component, Model model) {
        model.addAttribute("title", "Home");
        if (component.isEmpty()) {
            return "index";
        }
        return "pages/" + component;
    }

    @GetMapping("/login")
    public String login(@RequestParam(value = "with", defaultValue = "", required = true) String role, Model model) {
        model.addAttribute("title", "Sign In");
        model.addAttribute("role", role);
        if (role.isEmpty()) {
            return "404";
        }
        return "pages/auth/signin";
    }
}
