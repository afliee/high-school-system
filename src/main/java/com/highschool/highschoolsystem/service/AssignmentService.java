package com.highschool.highschoolsystem.service;

import com.highschool.highschoolsystem.dto.request.CreateAssignmentRequest;
import com.highschool.highschoolsystem.entity.AssignmentEntity;
import com.highschool.highschoolsystem.entity.StudentEntity;
import com.highschool.highschoolsystem.entity.Submitting;
import com.highschool.highschoolsystem.repository.AssignmentRepository;
import com.highschool.highschoolsystem.repository.ClassRepository;
import com.highschool.highschoolsystem.repository.StudentRepository;
import com.highschool.highschoolsystem.repository.SubmittingRepository;
import com.highschool.highschoolsystem.util.FileUploadUtils;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class AssignmentService {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private TeacherServiceImpl teacherService;
    @Autowired
    private ClassService classService;
    @Autowired
    private SubjectService subjectService;
    @Autowired
    private AssignmentRepository assignmentRepository;
    @Autowired
    private ClassRepository classRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private SubmittingRepository submittingRepository;

    @Transactional
    public void create(CreateAssignmentRequest request) {
        boolean isDue = request.getIsDue().equals("true");
        var teacher = teacherService.findById(request.getTeacherId());
        var subject = subjectService.findByIdEntity(request.getSubjectId());
        var assignment = AssignmentEntity.builder();

        assignment.title(request.getTitle());
        assignment.description(request.getDescription());
        assignment.subject(subject);
        assignment.points(Double.parseDouble(request.getPoints()));
        assignment.isDue(isDue);

        if (isDue) {
            var statedDate = LocalDateTime.parse(request.getStartedDate(), formatter);
            var dueDate = LocalDateTime.parse(request.getClosedDate(), formatter);

            assignment.startedDate(statedDate);
            assignment.closedDate(dueDate);
        }

        assignment.teacher(teacher);

//        save file assignment
        String fileName = request.getAttachment().getOriginalFilename();
        String randomName = UUID.randomUUID().toString();
        String path = """
                %s/%s""".formatted(randomName, subject.getId());
        if (request.getAttachment() != null) {
            try {
                FileUploadUtils.saveAssignmentFile(path, fileName, request.getAttachment());
                assignment.attachment(path + "/" + fileName);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Could not save file: " + fileName, e);
            }
        }
        var assignmentEntity = assignment.build();
        var students = subjectService.getStudents(subject.getId());
        System.out.println("students: " + students.size());
        if (!students.isEmpty()) {
            students.forEach(student -> {
                var submitting = Submitting.builder()
                        .student(student)
                        .assignment(assignmentEntity)
                        .build();
                if (student.getSubmittingSet() == null) {
                    student.setSubmittingSet(new HashSet<>());
                    student.getSubmittingSet().add(submitting);
                } else {
                    student.getSubmittingSet().add(submitting);
                }

                if (assignmentEntity.getSubmitting() == null) {
                    assignmentEntity.setSubmitting(new HashSet<>());
                    assignmentEntity.getSubmitting().add(submitting);
                } else {
                    assignmentEntity.getSubmitting().add(submitting);
                }

                submittingRepository.save(submitting);
                studentRepository.save(student);
            });
        }
        System.out.println("assignment: " + assignmentEntity.getSubmitting().size());
        assignmentRepository.save(assignmentEntity);
    }

    public Map<LocalDate, List<AssignmentEntity>> getAssigmentBySubjectId(String subjectId) {
        var assignments = assignmentRepository.findAllBySubjectId(subjectId);
        var dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return assignments.stream().collect(
                Collectors.groupingBy(
                        assignmentEntity -> {
                            var createdDate = assignmentEntity.getCreatedDate();
                            var formattedDate = createdDate.format(dateFormatter);
                            return LocalDate.parse(formattedDate, dateFormatter);
                        },
                        Collectors.toList()
                )
        );
    }

    public AssignmentEntity findById(String id) {
        return assignmentRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Assignment not found")
        );
    }

    public List<StudentEntity> getStudentsTurnedIn(String assignmentId) {
        var assignment = assignmentRepository.findById(assignmentId).orElseThrow(
                () -> new RuntimeException("Assignment not found")
        );
        var submitting = assignment.getSubmitting();
        if (submitting == null) return null;
        return submitting.stream().filter(submitting1 -> submitting1.getFile() != null).map(Submitting::getStudent).collect(Collectors.toList());
    }

    public List<StudentEntity> getStudentsUnTurnedIn(String assignmentId) {
        var assignment = assignmentRepository.findById(assignmentId).orElseThrow(
                () -> new RuntimeException("Assignment not found")
        );
        var submitting = assignment.getSubmitting();
        if (submitting == null) return null;
        return submitting.stream().filter(submitting1 -> submitting1.getFile() == null).map(Submitting::getStudent).collect(Collectors.toList());
    }
}
