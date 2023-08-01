package com.highschool.highschoolsystem.controller.api;

import com.highschool.highschoolsystem.dto.response.DayResponse;
import com.highschool.highschoolsystem.service.DayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/day")
public class DayApiController {
    @Autowired
    private DayService dayService;

    @GetMapping("/all")
    public List<DayResponse> getAllDays(){
        return dayService.getAllDays();
    }
}
