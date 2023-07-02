package com.highschool.highschoolsystem.service;

import com.highschool.highschoolsystem.entity.DepartmentEntity;
import com.highschool.highschoolsystem.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DepartmentService implements GeneralService<DepartmentEntity> {

    @Autowired
    private DepartmentRepository departmentRepository;
    @Override
    public DepartmentEntity save(DepartmentEntity departmentEntity) {
        return departmentRepository.save(departmentEntity);
    }

    @Override
    public DepartmentEntity update(DepartmentEntity departmentEntity) {
        return null;
    }

    @Override
    public void delete(String id) {

    }

    @Override
    public Iterable<DepartmentEntity> findAll() {
//        get all department form page
        return departmentRepository.findAll();
    }

    @Override
    public DepartmentEntity findById(String id) {
        return departmentRepository.findById(id).orElse(null);
    }
}
