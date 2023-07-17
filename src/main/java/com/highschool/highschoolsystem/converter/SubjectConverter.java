package com.highschool.highschoolsystem.converter;

import com.highschool.highschoolsystem.dto.request.SubjectRequest;
import com.highschool.highschoolsystem.dto.response.DepartmentResponse;
import com.highschool.highschoolsystem.dto.response.SubjectResponse;
import com.highschool.highschoolsystem.entity.DepartmentEntity;
import com.highschool.highschoolsystem.entity.SubjectEntity;
import com.highschool.highschoolsystem.entity.TeacherEntity;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class SubjectConverter {

    public static SubjectResponse toResponse(SubjectEntity subjectEntity) {
//        parse date to localdate
//        format date to format yyyy-MM-dd
        return SubjectResponse.builder()
                .id(subjectEntity.getId())
                .name(subjectEntity.getName())
                .teacher(TeacherConverter.toResponse(subjectEntity.getTeacher()))
                .department(subjectEntity.getDepartment().getName())
                .color(subjectEntity.getColor())
                .createdAt(subjectEntity.getCreatedDate())
                .departmentDetail(
                        DepartmentResponse.builder()
                                .id(subjectEntity.getDepartment().getId())
                                .name(subjectEntity.getDepartment().getName())
                                .build()
                )
                .build();
    }

    public static SubjectEntity toEntity(SubjectResponse subjectResponse) {
        return SubjectEntity.builder()
                .name(subjectResponse.getName())
                .description(subjectResponse.getDescription())
                .teacher(TeacherConverter.toEntity(subjectResponse.getTeacher()))
                .build();
    }

    public static SubjectEntity toEntity(SubjectRequest request, TeacherEntity teacher, DepartmentEntity department) {
        Random ramdom = new Random();
        int r = ramdom.nextInt(255);
        int g = ramdom.nextInt(255);
        int b = ramdom.nextInt(255);

        return SubjectEntity.builder()
                .name(request.getName())
                .description(request.getDescription())
                .teacher(teacher)
                .department(department)
                .color("rgb(" + r + "," + g + "," + b + ")")
                .build();
    }

    public static SubjectEntity from(SubjectEntity subject, SubjectRequest request, TeacherEntity teacher, DepartmentEntity department) {
        subject.setName(request.getName());
        subject.setDescription(request.getDescription());
        subject.setTeacher(teacher);
        subject.setDepartment(department);
        return subject;
    }

    public static Pageable toPageable(int page, int size, String sort, String sortBy) {
        return PageRequest.of(page, size, Sort.by(sort.equals("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy));
    }

    public static Specification<?> toSpecification(Map<String, String> filterMap) {
        return null;

    }

    public static List<SubjectResponse> toResponse(List<SubjectEntity> content) {
        return content.stream().map(SubjectConverter::toResponse).toList();
    }

    public static SubjectResponse toDetail(SubjectEntity subjectEntity) {
        return SubjectResponse.builder()
                .id(subjectEntity.getId())
                .name(subjectEntity.getName())
                .teacher(TeacherConverter.toResponse(subjectEntity.getTeacher()))
                .department(subjectEntity.getDepartment().getName())
                .color(subjectEntity.getColor())
                .description(subjectEntity.getDescription())
                .createdAt(subjectEntity.getCreatedDate())
                .build();
    }
}
