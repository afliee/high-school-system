package com.highschool.highschoolsystem.controller;

import com.highschool.highschoolsystem.service.AdminService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/auth/admin")
@Tag(name = "Admin", description = "Admin views for CRUD")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @GetMapping({"/", "", "/dashboard"})
    public String index(
            HttpServletRequest request,
            HttpServletRequest response
    ) {

        String redirect = adminService.requireAdminLogin(request);

        if (redirect != null) {
            return redirect;
        }

        return "pages/admin/dashboard";
    }

    @GetMapping("/login")
    public String login(
            HttpServletRequest request,
            HttpServletRequest response
    ) {
        return "pages/auth/adminSignin";
    }

    @GetMapping("/members")
    public String teachers(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam(name = "filter", required = true, defaultValue = "") String filter,
            @RequestParam(name = "page", required = true, defaultValue = "1") int page,
            Model model
    ) {
        String redirect = adminService.requireAdminLogin(request);
        model.addAttribute("filter", filter);
        model.addAttribute("page", page);
        return redirect != null ? redirect : "pages/admin/" + filter + "s";
    }

    @GetMapping("/classes")
    public String classes(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        String redirect = adminService.requireAdminLogin(request);

        return redirect != null ? redirect : "pages/admin/classes";
    }

    @GetMapping("/classes/{classId}")
    public String classDetails(
            HttpServletRequest request,
            @PathVariable String classId,
            Model model
    ) {
        String redirect = adminService.requireAdminLogin(request);

        if (redirect != null) {
            return redirect;
        }
        model.addAttribute("classId", classId);
        return "pages/admin/classDetails";
    }
}
