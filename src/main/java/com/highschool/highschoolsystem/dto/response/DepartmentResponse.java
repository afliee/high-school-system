package com.highschool.highschoolsystem.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class DepartmentResponse implements Serializable {
    private String id;
    private String name;
    private String description;
    private String foundedDate;
    private List<TeacherResponse> teachers;
    private List<SubjectResponse> subjects;
// [
//  //    {
////      name: "Math",
////      color: "rgb(255, 99, 132)",
////    },
////    {
////      name: "English",
////      color: "rgb(54, 162, 235)",
////    },
// ]
    private List<SubjectResponse> subjectColors;

}
