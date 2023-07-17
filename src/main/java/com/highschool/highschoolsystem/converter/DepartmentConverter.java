package com.highschool.highschoolsystem.converter;

import com.highschool.highschoolsystem.dto.request.DepartmentRequest;
import com.highschool.highschoolsystem.dto.response.DepartmentResponse;
import com.highschool.highschoolsystem.dto.response.SubjectResponse;
import com.highschool.highschoolsystem.entity.DepartmentEntity;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;


@NoArgsConstructor
public class DepartmentConverter {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final long DEFAULT_LIMIT = 5;
    private DepartmentEntity departmentEntity;
    private static DepartmentRequest departmentRequest;

    public DepartmentConverter(DepartmentRequest departmentRequest) {
        DepartmentConverter.departmentRequest = departmentRequest;
    }

    public DepartmentConverter(DepartmentEntity departmentEntity) {
        this.departmentEntity = departmentEntity;
    }

    // format with key specified by argument
    public static DepartmentResponse toResponse(DepartmentEntity departmentEntity) {
        return getDepartmentResponse(departmentEntity, DEFAULT_LIMIT);
    }

    public static DepartmentResponse toResponse(DepartmentEntity departmentEntity, long limit) {
        return getDepartmentResponse(departmentEntity, limit);
    }

    private static DepartmentResponse getDepartmentResponse(DepartmentEntity departmentEntity, long limit) {
        var subjects = departmentEntity.getSubjects();
        List<SubjectResponse> subjectColors = null;
        if (subjects != null) {
            subjectColors = subjects.stream().map(subject ->
                    SubjectResponse.builder()
                            .name(subject.getName())
                            .color(subject.getColor())
                            .build()
            ).limit(limit).toList();
        }
        return DepartmentResponse.builder()
                .id(departmentEntity.getId())
                .name(departmentEntity.getName())
                .description(departmentEntity.getDescription())
                .foundedDate(departmentEntity.getFoundedDate().format(DATE_TIME_FORMATTER))
                .subjectColors(subjectColors)
                .build();
    }


    public static DepartmentResponse toDetailResponse(DepartmentEntity departmentEntity) {
        return DepartmentResponse.builder()
                .id(departmentEntity.getId())
                .name(departmentEntity.getName())
                .description(departmentEntity.getDescription())
                .foundedDate(departmentEntity.getFoundedDate().format(DATE_TIME_FORMATTER))
                .teachers(departmentEntity.getTeachers().stream().map(TeacherConverter::toResponse).toList())
                .subjects(departmentEntity.getSubjects().stream().map(SubjectConverter::toResponse).toList())
                .build();
    }

    public static DepartmentEntity toEntity(DepartmentRequest departmentRequest) {
        LocalDate now = LocalDate.now();
//        convert now to yyyy-MM-dd format
        now.format(DATE_TIME_FORMATTER);
        return DepartmentEntity.builder()
                .name(departmentRequest.getName())
                .description(departmentRequest.getDescription())
                .foundedDate(now)
                .build();
    }

    public static DepartmentResponse from(DepartmentEntity departmentEntity) {
        return DepartmentResponse.builder()
                .id(departmentEntity.getId())
                .name(departmentEntity.getName())
                .description(departmentEntity.getDescription())
                .foundedDate(departmentEntity.getFoundedDate().format(DATE_TIME_FORMATTER))
                .build();
    }

    public static DepartmentConverter with(DepartmentEntity departmentEntity) {
        return new DepartmentConverter(departmentEntity);
    }

//    DepartmentConverter.receive(departmentRequest).to(departmentEntity)
    public static DepartmentConverter receive(DepartmentRequest departmentRequest) {
        return new DepartmentConverter(departmentRequest);
    }

    public  DepartmentEntity to(DepartmentEntity entity) {
        entity.setName(departmentRequest.getName());
        entity.setDescription(departmentRequest.getDescription());
        return entity;
    }

    public DepartmentResponse toResponse() {
        return DepartmentResponse.builder()
                .id(departmentEntity.getId())
                .name(departmentEntity.getName())
                .description(departmentEntity.getDescription())
                .foundedDate(departmentEntity.getFoundedDate().format(DATE_TIME_FORMATTER))
                .build();
    }
}
