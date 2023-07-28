package com.highschool.highschoolsystem.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
public class SchedulingResponse {

    @Builder
    @Data
    public static class SubjectByLevelResponse {
        private String id;
        private String name;
        private String departmentName;
        private TeacherResponse teacher;
        private String color;
        private List<LessonResponse> lessons;
//        private List<String> lessons;
    }
}
