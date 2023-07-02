package com.highschool.highschoolsystem.controller;

import com.highschool.highschoolsystem.entity.DepartmentEntity;
import com.highschool.highschoolsystem.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/department")
public class DepartmentController {
    @Autowired
    private DepartmentService departmentService;

//    get all department
    @GetMapping({"/", ""})
    public ResponseEntity<?> index() {
        Iterable<DepartmentEntity> departmentEntities = departmentService.findAll();
        return new ResponseEntity<>(departmentEntities, HttpStatus.OK);
    }

}
