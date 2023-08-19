package com.highschool.highschoolsystem.service;

import com.highschool.highschoolsystem.converter.SubjectConverter;
import com.highschool.highschoolsystem.dto.request.SubjectRequest;
import com.highschool.highschoolsystem.dto.response.SubjectGroupByResponse;
import com.highschool.highschoolsystem.dto.response.SubjectResponse;
import com.highschool.highschoolsystem.entity.SubjectEntity;
import com.highschool.highschoolsystem.exception.NotFoundException;
import com.highschool.highschoolsystem.repository.DepartmentRepository;
import com.highschool.highschoolsystem.repository.LevelRepository;
import com.highschool.highschoolsystem.repository.SubjectRepository;
import com.highschool.highschoolsystem.repository.TeacherRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class SubjectService {
    @Autowired
    private SubjectRepository subjectRepository;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private LevelRepository levelRepository;

    public Page<?> getAll(int page, int size, String sort, String sortBy, String filter) {
//        ?page=0&size=10&sort=ASC&sortBy=id&filter=name:chemical;teacherName:HuynhTruong;departmentId:0
        if (!filter.isEmpty()) {
            String[] filters = filter.split(";");

            Map<String, String> filterMap = new HashMap<>();

            for (String filterItem : filters) {
                String[] filterItemSplit = filterItem.split(":");
                filterMap.put(filterItemSplit[0], filterItemSplit[1]);
            }

            Pageable pageable = SubjectConverter.toPageable(page, size, sort, sortBy);

//            String departmentId = filterMap.get("departmentId");

            String name = filterMap.get("name");
            String teacherName = filterMap.get("teacherName");

            if (!filterMap.containsKey("departmentId") || filterMap.get("departmentId").equals("0")) {
//                return all subject
                if (name != null && teacherName != null) {
                    var subjects = subjectRepository.findAllByNameContainingAndTeacher_FullNameContaining(
                            name, teacherName, pageable
                    );
                    var subjectResponses = SubjectConverter.toResponse(subjects.getContent());
                    return new PageImpl<>(subjectResponses, pageable, subjects.getTotalElements());
                } else if (name != null) {
                    var subjects = subjectRepository.findAllByNameContaining(
                            name, pageable
                    );
                    var subjectResponses = SubjectConverter.toResponse(subjects.getContent());
                    return new PageImpl<>(subjectResponses, pageable, subjects.getTotalElements());
                } else if (teacherName != null) {
                    var subjects = subjectRepository.findAllByTeacherContaining(
                            teacherName, pageable
                    );
                    var subjectResponses = SubjectConverter.toResponse(subjects.getContent());
                    return new PageImpl<>(subjectResponses, pageable, subjects.getTotalElements());
                } else {
                    var subjects = subjectRepository.findAll(pageable);
                    var subjectResponses = SubjectConverter.toResponse(subjects.getContent());
                    return new PageImpl<>(subjectResponses, pageable, subjects.getTotalElements());
                }
            } else {
                String departmentId = filterMap.get("departmentId");
//                return subject by department
                if (name != null && teacherName != null) {
                    var subjects = subjectRepository.findAllByNameContainingOrDepartment_Id(
                            name, departmentId
                    );
                    var subjectResponses = SubjectConverter.toResponse(subjects);
                    return new PageImpl<>(subjectResponses, pageable, subjects.size());
                } else if (name != null) {
                    var subjects = subjectRepository.findAllByNameContaining(
                            name
                    );
                    var subjectResponses = SubjectConverter.toResponse(subjects);
                    return new PageImpl<>(subjectResponses, pageable, subjects.size());
                } else if (teacherName != null) {
                    var subjects = subjectRepository.findAllByTeacherContaining(
                            teacherName, pageable
                    );
                    var subjectResponses = SubjectConverter.toResponse(subjects.getContent());
                    return new PageImpl<>(subjectResponses, pageable, subjects.getTotalElements());
                } else {
                    var subjects = subjectRepository.findAllByDepartment_Id(
                            departmentId
                    );
                    var subjectResponses = SubjectConverter.toResponse(subjects);
                    return new PageImpl<>(subjectResponses, pageable, subjects.size());
                }
            }

        }
        return null;
    }

    public SubjectResponse create(SubjectRequest subjectRequest) {
        var level = levelRepository.findById(subjectRequest.getLevelId()).orElseThrow(
                () -> new RuntimeException("Level not found")
        );

        var teacher = teacherRepository.findById(subjectRequest.getTeacherId()).orElseThrow(
                () -> new RuntimeException("Teacher not found")
        );

        var department = departmentRepository.findById(subjectRequest.getDepartmentId()).orElseThrow(
                () -> new RuntimeException("Department not found")
        );

        var subject = subjectRepository.save(
                SubjectConverter.toEntity(subjectRequest, teacher, department, level)
        );

        department.getSubjects().add(subject);
        departmentRepository.save(department);

        level.getSubjects().add(subject);
        levelRepository.save(level);

        return SubjectConverter.toResponse(subject);
    }

    public SubjectResponse update(String subjectId, SubjectRequest subjectRequest) {
        var subject = subjectRepository.findById(subjectId).orElseThrow(
                () -> new RuntimeException("Subject not found")
        );

        var teacher = teacherRepository.findById(subjectRequest.getTeacherId()).orElseThrow(
                () -> new RuntimeException("Teacher not found")
        );

        var department = departmentRepository.findById(subjectRequest.getDepartmentId()).orElseThrow(
                () -> new RuntimeException("Department not found")
        );

        SubjectConverter.from(subject, subjectRequest, teacher, department);
        subjectRepository.save(subject);

        return SubjectConverter.toResponse(subject);
    }

    public SubjectResponse findById(String id) {
        var subject = subjectRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Subject not found")
        );

        return SubjectConverter.toResponse(subject);
    }

    public SubjectResponse delete(String id) {
        var subject = subjectRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Subject not found")
        );

        departmentRepository.findById(subject.getDepartment().getId()).ifPresent(
                department -> department.getSubjects().remove(subject)
        );

        subjectRepository.delete(subject);

        return SubjectConverter.toResponse(subject);
    }

    public SubjectResponse get(String id) {
        var subject = subjectRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Subject not found")
        );

        return SubjectConverter.toResponse(subject);
    }

    public List<SubjectGroupByResponse> findAllGroupByDepartment() {
        List<SubjectEntity> subjects = subjectRepository.findAll();

        Map<String, Object> subjectsGrouped = new HashMap<>();

//        subjects.forEach(subject -> {
//            if (subjectsGrouped.containsKey(subject.getDepartment().getId())) {
//
//            }
//        });
        return null;
    }
}
