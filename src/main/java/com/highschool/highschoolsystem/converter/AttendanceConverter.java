package com.highschool.highschoolsystem.converter;

import com.highschool.highschoolsystem.dto.response.AttendanceResponse;
import com.highschool.highschoolsystem.entity.AttendanceEntity;

import java.util.Collection;
import java.util.List;

import static com.fasterxml.jackson.databind.type.LogicalType.Collection;

public class AttendanceConverter {
    public static AttendanceResponse toResponse(AttendanceEntity attendance) {
        var studentsInClass = attendance.getClassEntity().getStudents();

//        student absent
        var studentsAbsent = studentsInClass.stream()
                .filter(student -> !attendance.getStudents().contains(student))
                .map(student -> {
                    var studentResponse = StudentConverter.toResponse(student);
                    studentResponse.setAbsent(true);
                    return studentResponse;
                })
                .toList();

        var students = StudentConverter.toResponse(attendance.getStudents().stream().toList());
        students.addAll(studentsAbsent);

//        add student and student absent to list
        return AttendanceResponse.builder()
                .navigator(attendance.getNavigator().getStudent().getFullName())
                .classEntity(ClassConverter.toResponse(attendance.getClassEntity()))
                .students(students)
                .present(attendance.getPresent())
                .build();
    }
}
