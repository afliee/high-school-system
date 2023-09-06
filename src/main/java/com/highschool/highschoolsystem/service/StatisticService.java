package com.highschool.highschoolsystem.service;

import com.highschool.highschoolsystem.config.SubmitStatus;
import com.highschool.highschoolsystem.dto.response.LessonResponse;
import com.highschool.highschoolsystem.entity.AttendanceEntity;
import com.highschool.highschoolsystem.entity.ClassEntity;
import com.highschool.highschoolsystem.entity.LessonEntity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class StatisticService {
    @Autowired
    private StudentService studentService;
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private ClassService classService;
    @Autowired
    private SubjectService subjectService;
    @Autowired
    private SemesterService semesterService;
    @Autowired
    private LevelService levelService;
    @Autowired
    private LessonService lessonService;
    @Autowired
    private AssignmentService assignmentService;
    public Map<String, Object> getStatisticHeader() {
        return Map.of(
                "totalStudents", studentService.countStudent(),
                "totalTeachers", teacherService.count(),
                "totalClasses", classService.count(),
                "totalSubjects", subjectService.count(),
                "currentSemester", semesterService.findCurrentSemesterEntity()
        );
    }


    public Map<String, Double> getAttendanceStatistic() {
        List<String> levels = levelService.getAllLevelId();

        if (levels.isEmpty()) {
            return Map.of();
        }

        Map<String, Double> result = new HashMap<>();
        for (String level : levels) {
            List<ClassEntity> classes = classService.findClassCurrentSemesterAndLevel(level);

            if (classes.isEmpty()) {
                continue;
            }

            for (var classEntity : classes) {
                List<AttendanceEntity> attendances = classEntity.getAttendances().stream().toList();
                int totalAttendance = attendances.size();

//                sum student present inside attendances
                int totalStudentPresent = attendances.stream().mapToInt(AttendanceEntity::getPresent).sum();
                System.out.println("totalStudentPresent: " + totalStudentPresent);
                System.out.println("totalAttendance: " + totalAttendance);
                result.put(classEntity.getName(), totalAttendance == 0 ? 0 : (double) (totalStudentPresent * 100) / (totalAttendance * classEntity.getStudents().size()));
            }
        }

        return result;
    }

    /*
     * [TODO] data response parsed:
     * {
     *   "level1": {
     *      "subject1": 10,
     *      "subject2": 20
     *   },...
     * }
     * */
    public Map<String, Map<String, Double>> getAcademicPerformanceStatistic() {
        var levels = levelService.getAllLevel();
        Map<String, Map<String, Double>> result = new HashMap<>();

        if (levels.isEmpty()) {
            return Map.of();
        }

        levels.forEach(level -> {
            Map<String, Double> classResult = new HashMap<>();
            var subjects = level.getSubjects();

            System.out.println("subjects size: " + subjects.size());
            if (subjects.isEmpty()) {
                return;
            }


            final String name = level.getName();

            double averageStudentSubmitted = 0;

            subjects.forEach(subject -> {
                var assignments = assignmentService.findAllBySubjectId(subject.getId());
                var students = subjectService.getStudents(subject.getId());
                System.out.println(subject.getName() + " students size: " + students.size());
                if (students.isEmpty()) {
                    return;
                }

                System.out.println(subject.getName() + " assignments size: " + assignments.size());
                if (assignments.isEmpty()) {
                    return;
                }

                double averageAssignmentSubmitted = assignments.stream().mapToDouble(assignment -> {
                    var submitting = assignment.getSubmitting();
                    if (submitting.isEmpty()) {
                        return 0;
                    }

                    return submitting.stream().filter(submit -> submit.getStatus().equals(SubmitStatus.SUBMITTED) || submit.getStatus().equals(SubmitStatus.GRADED)).count();
                }).sum() / (assignments.size() * students.size());

                System.out.println("averageAssignmentSubmitted: " + averageAssignmentSubmitted);

                classResult.put(subject.getName(), averageAssignmentSubmitted * 100);
            });

            result.put(name, classResult);
        });

        return result;
    }

    public List<LessonEntity> getLessonToday(int limit) {
        if (limit < -1) {
            return List.of();
        }

        if (limit == -1) {
            return lessonService.getLessonToday(-1);
        }

        return lessonService.getLessonToday(limit);
    }
}
