package com.highschool.highschoolsystem.service;

import com.highschool.highschoolsystem.converter.DepartmentConverter;
import com.highschool.highschoolsystem.dto.request.DepartmentRequest;
import com.highschool.highschoolsystem.dto.response.DepartmentResponse;
import com.highschool.highschoolsystem.entity.DepartmentEntity;
import com.highschool.highschoolsystem.exception.NotFoundException;
import com.highschool.highschoolsystem.repository.DepartmentRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    public DepartmentResponse save(DepartmentRequest departmentRequest) {
        departmentRepository.findByName(departmentRequest.getName()).ifPresent(
                departmentEntity -> {
                    throw new RuntimeException("Department already exist");
                }
        );


        var departmentEntity = DepartmentConverter.toEntity(departmentRequest);
        departmentEntity = departmentRepository.save(departmentEntity);
        return DepartmentConverter.toResponse(departmentEntity);
    }

    public DepartmentResponse update(String id, DepartmentRequest request) {
        var department = departmentRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Department not found")
        );

        var departmentUpdated =  DepartmentConverter.receive(request).to(department);
        departmentRepository.save(departmentUpdated);
        return DepartmentConverter.toResponse(departmentUpdated);
    }

    public void delete(String id) {

    }

    public Iterable<DepartmentEntity> findAll() {
//        get all department form page
        return departmentRepository.findAll();
    }


    public Page<?> findAll(int page, int size, String sortBy) {
        PageRequest pageRequest = PageRequest.of(page, size).withSort(Sort.by(sortBy));
        var departments = departmentRepository.findAll(pageRequest);
        var departmentEntities = departments.getContent();
        var departmentDtos = departmentEntities.stream().map(DepartmentConverter::toResponse).toList();
        return new PageImpl<>(departmentDtos, pageRequest, departments.getTotalElements());
    }


    public DepartmentResponse findById(String id) {
        var department = departmentRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Department not found")
        );

        return DepartmentConverter.toDetailResponse(department);
    }
}
