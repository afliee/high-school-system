package com.highschool.highschoolsystem.controller.api;

import com.highschool.highschoolsystem.service.NavigatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/navigator")
public class NavigationController {
    private final NavigatorService navigatorService;

    @GetMapping("/get")
    public ResponseEntity<?> get(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(navigatorService.findAll(page, size));
    }
}
