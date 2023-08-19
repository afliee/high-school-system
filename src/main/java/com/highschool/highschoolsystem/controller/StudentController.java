package com.highschool.highschoolsystem.controller;

import com.highschool.highschoolsystem.entity.StudentEntity;
import com.highschool.highschoolsystem.repository.TokenRepository;
import com.highschool.highschoolsystem.repository.UserRepository;
import com.highschool.highschoolsystem.service.AuthenticationService;
import com.highschool.highschoolsystem.service.JwtService;
import com.highschool.highschoolsystem.service.NavigatorService;
import com.highschool.highschoolsystem.service.UserDetailServiceImpl;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.token.TokenService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.WebUtils;

@Controller
@RequestMapping("/student")
public class StudentController {
    @Autowired
    private NavigatorService navigatorService;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping({"/home", "/", ""})
    public String index() {
        return "pages/student/index";
    }

    @GetMapping("/navigator-registration")
    public String navigatorRegistration(
            HttpServletRequest request,
            Model model
    ) {
        Cookie token = WebUtils.getCookie(request, "token");
        if (token == null) {
            return "redirect:/?component=chooseLogin";
        }

        var navigator = navigatorService.findNavigatorAlreadyRegistered(token.getValue());
        if (navigator != null) {
            model.addAttribute("navigator", navigator);
        }

        if (token.getValue().isEmpty()) {
            return "redirect:/?component=chooseLogin";
        }

        var username = jwtService.extractUsername(token.getValue());
        var user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found."));

        model.addAttribute("student", user.getUserId());
        return "pages/student/navigator-registration";
    }
}
