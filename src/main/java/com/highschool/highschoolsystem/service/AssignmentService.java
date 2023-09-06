package com.highschool.highschoolsystem.service;

import com.highschool.highschoolsystem.config.SubmitStatus;
import com.highschool.highschoolsystem.converter.AssignmentConverter;
import com.highschool.highschoolsystem.converter.SubmittingConverter;
import com.highschool.highschoolsystem.dto.request.CreateAssignmentRequest;
import com.highschool.highschoolsystem.dto.request.GradingRequest;
import com.highschool.highschoolsystem.dto.request.SubmitRequest;
import com.highschool.highschoolsystem.dto.response.AssignmentResponse;
import com.highschool.highschoolsystem.dto.response.SubmittingResponse;
import com.highschool.highschoolsystem.entity.AssignmentEntity;
import com.highschool.highschoolsystem.entity.StudentEntity;
import com.highschool.highschoolsystem.entity.Submitting;
import com.highschool.highschoolsystem.exception.NotFoundException;
import com.highschool.highschoolsystem.repository.AssignmentRepository;
import com.highschool.highschoolsystem.repository.ClassRepository;
import com.highschool.highschoolsystem.repository.StudentRepository;
import com.highschool.highschoolsystem.repository.SubmittingRepository;
import com.highschool.highschoolsystem.util.FileUploadUtils;
import com.highschool.highschoolsystem.util.mail.EmailDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
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
    @Autowired
    private EmailService emailService;

    @Transactional
    public void create(CreateAssignmentRequest request) {
        boolean isDue = request.getIsDue().equals("true");
        var teacher = teacherService.findById(request.getTeacherId());
        var subject = subjectService.findByIdEntity(request.getSubjectId()).orElseThrow(
                () -> new NotFoundException("Subject not found")
        );
        var assignment = AssignmentEntity.builder();

        assignment.title(request.getTitle());
        assignment.description(request.getDescription());
        assignment.subject(subject);
        assignment.points(Double.parseDouble(request.getPoints()));
        assignment.isDue(isDue);

        var statedDate = LocalDateTime.parse(request.getStartedDate(), formatter);
        assignment.startedDate(statedDate);
        if (isDue) {
            var dueDate = LocalDateTime.parse(request.getClosedDate(), formatter);

            assignment.closedDate(dueDate);
        }

        assignment.teacher(teacher);

//        save file assignment

        if (request.getAttachment() != null) {
            String fileName = request.getAttachment().getOriginalFilename();
            String randomName = UUID.randomUUID().toString();
            String path = """
                    %s/%s""".formatted(randomName, subject.getId());
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
                        .status(SubmitStatus.NOT_SUBMITTED)
                        .build();
                if (student.getSubmittingSet() == null) {
                    student.setSubmittingSet(new HashSet<>());
                    student.getSubmittingSet().add(submitting);
                } else {
                    student.getSubmittingSet().add(submitting);
                }

                if (assignmentEntity.getSubmitting() == null || assignmentEntity.getSubmitting().isEmpty()) {
                    assignmentEntity.setSubmitting(new HashSet<>());
                    assignmentEntity.getSubmitting().add(submitting);
                } else {
                    assignmentEntity.getSubmitting().add(submitting);
                }

                submittingRepository.save(submitting);
                studentRepository.save(student);
            });
        }
//        System.out.println("assignment: " + assignmentEntity.getSubmitting().size());
        assignmentRepository.save(assignmentEntity);

        CompletableFuture.runAsync(() -> {
            assignmentEntity.getSubmitting().forEach(submit -> {
                var student = submit.getStudent();
                if (student == null) {
                    return;
                }

                var emailDetails = EmailDetails.builder()
                        .subject(teacher.getFullName() + "has given your mission: \" " + assignmentEntity.getTitle() + "\"")
                        .to(student.getEmail())
                        .pathToAttachment(assignmentEntity.getAttachment())
                        .build();

                emailService.sendAssignmentEmail(emailDetails, assignmentEntity);
            });
        });
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
                () -> new NotFoundException("Assignment not found")
        );
    }

    public List<StudentEntity> getStudentsTurnedIn(String assignmentId) {
        var assignment = assignmentRepository.findById(assignmentId).orElseThrow(
                () -> new NotFoundException("Assignment not found")
        );
        var submitting = assignment.getSubmitting();
        if (submitting == null) return null;
        return submitting.stream().filter(submitting1 -> submitting1.getFile() != null).map(Submitting::getStudent).collect(Collectors.toList());
    }

    public List<StudentEntity> getStudentsUnTurnedIn(String assignmentId) {
        var assignment = assignmentRepository.findById(assignmentId).orElseThrow(
                () -> new NotFoundException("Assignment not found")
        );
        var submitting = assignment.getSubmitting();
        if (submitting == null) return null;
        return submitting.stream().filter(submitting1 -> submitting1.getFile() == null).map(Submitting::getStudent).collect(Collectors.toList());
    }

    public AssignmentResponse update(String id, CreateAssignmentRequest request) {
        var assignment = assignmentRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Assignment not found")
        );
        boolean isDue = request.getIsDue().equals("true");
        var teacher = teacherService.findById(request.getTeacherId());
        var subject = subjectService.findByIdEntity(request.getSubjectId()).orElseThrow(
                () -> new NotFoundException("Subject not found")
        );

        assignment.setTitle(request.getTitle());
        assignment.setDescription(request.getDescription());
        assignment.setSubject(subject);
        assignment.setPoints(Double.parseDouble(request.getPoints()));
        assignment.setDue(isDue);

        var statedDate = LocalDateTime.parse(request.getStartedDate(), formatter);
        assignment.setStartedDate(statedDate);
        if (isDue) {
            System.out.println("closed date: " + request.getClosedDate());
            var dueDate = LocalDateTime.parse(request.getClosedDate(), formatter);

            assignment.setClosedDate(dueDate);
        }

        assignment.setTeacher(teacher);

        if (request.getAttachment() != null) {
//            remove old file
            if (assignment.getAttachment() != null) {
                try {
                    FileUploadUtils.removeFile(assignment.getAttachment());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }


            try {
                String fileName = request.getAttachment().getOriginalFilename();
                String path = "";
                if (assignment.getAttachment() != null) {
                    path = assignment.getAttachment().substring(0, assignment.getAttachment().lastIndexOf("/"));
                } else {
                    String randomName = UUID.randomUUID().toString();
                    path = """
                            %s/%s""".formatted(randomName, subject.getId());
                }
                FileUploadUtils.saveAssignmentFile(path, fileName, request.getAttachment());
                assignment.setAttachment(path + "/" + fileName);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Could not save file", e);
            }
        }

        return AssignmentConverter.toResponse(assignmentRepository.save(assignment));
    }

    public void delete(String id) {
        var assignment = assignmentRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Assignment not found")
        );
        if (assignment.getAttachment() != null) {
            try {
                FileUploadUtils.removeDir(assignment.getAttachment());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        assignment.getSubmitting().forEach(submitting -> {
            submitting.setAssignment(null);
            submitting.setStudent(null);
            submittingRepository.delete(submitting);
        });

        assignmentRepository.delete(assignment);
    }

    public void submit(String id, SubmitRequest request) {
        var assignment = assignmentRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Assignment not found")
        );

        var student = studentRepository.findById(request.getStudentId()).orElseThrow(
                () -> new NotFoundException("Student not found")
        );

        var submitting = submittingRepository.findByAssignmentIdAndStudentId(id, student.getId()).orElseThrow(
                () -> new NotFoundException("Submitting not found")
        );

        var now = LocalDateTime.now();
        submitting.setTurnedAt(now);
        if (submitting.getAssignment().getClosedDate() != null) {
            if (now.isAfter(submitting.getAssignment().getClosedDate())) {
                submitting.setTurnedLate(true);
            }
        }

        if (request.getFile() != null) {
//            String path = assignment.getAttachment().substring(0, assignment.getAttachment().lastIndexOf("/"));
            String path = "";
            if (assignment.getAttachment() == null) {
                String randomName = UUID.randomUUID().toString();
                path = """
                        %s/%s""".formatted(randomName, assignment.getSubject().getId());

                try {
                    FileUploadUtils.createDir(path);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } else {
                path = assignment.getAttachment().substring(0, assignment.getAttachment().lastIndexOf("/"));
            }
            String originFileName = request.getFile().getOriginalFilename();
            String pathFile = path + "/submitting/" + student.getId();
            try {
                FileUploadUtils.saveAssignmentFile(pathFile, originFileName, request.getFile());
                submitting.setFile(pathFile + "/" + originFileName);
                submitting.setStatus(SubmitStatus.SUBMITTED);
                submittingRepository.save(submitting);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Could not save file: " + originFileName, e);
            }
        } else {
            submitting.setStatus(SubmitStatus.SUBMITTED);
            submittingRepository.save(submitting);
        }
    }

    public void reSubmit(String id, SubmitRequest request) {
        var assignment = assignmentRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Assignment not found")
        );

        var student = studentRepository.findById(request.getStudentId()).orElseThrow(
                () -> new NotFoundException("Student not found")
        );

        var submitting = submittingRepository.findByAssignmentIdAndStudentId(id, student.getId()).orElseThrow(
                () -> new NotFoundException("Submitting not found")
        );

        var now = LocalDateTime.now();
        submitting.setTurnedAt(now);
        if (submitting.getAssignment().getClosedDate() != null) {
            if (now.isAfter(submitting.getAssignment().getClosedDate())) {
                submitting.setTurnedLate(true);
            }
        }

        if (request.getFile() != null) {
            String path = assignment.getAttachment().substring(0, assignment.getAttachment().lastIndexOf("/"));
            String originFileName = request.getFile().getOriginalFilename();
            String pathFile = path + "/submitting/" + student.getId();
            try {
                if (submitting.getFile() != null) {
                    FileUploadUtils.removeFile(submitting.getFile());
                }
                FileUploadUtils.saveAssignmentFile(pathFile, originFileName, request.getFile());
                submitting.setFile(pathFile + "/" + originFileName);
                submitting.setStatus(SubmitStatus.SUBMITTED);
                submittingRepository.save(submitting);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Could not save file: " + originFileName, e);
            }
        } else {
            submitting.setStatus(SubmitStatus.SUBMITTED);
            submittingRepository.save(submitting);
        }
    }

    public SubmittingResponse grading(String submittingId, GradingRequest request) {
        var submitting = submittingRepository.findById(submittingId).orElseThrow(
                () -> new NotFoundException("Submitting not found")
        );

        if (submitting.getScore() > submitting.getAssignment().getPoints()) {
            throw new RuntimeException("Score must be less than points");
        }
        submitting.setScore(request.getScore());
        submitting.setComment(request.getComment());
        submitting.setStatus(SubmitStatus.GRADED);

        submittingRepository.save(submitting);

        CompletableFuture.runAsync(() -> {
            var emailDetails = EmailDetails.builder()
                    .subject("Your assignment has been graded")
                    .to(submitting.getStudent().getEmail())
                    .build();

            emailService.sendAssignmentGradingEmail(emailDetails, submitting);
        });

        return SubmittingConverter.toResponse(submitting);
    }

    public List<AssignmentEntity> findAllBySubjectId(String subjectId) {
        return assignmentRepository.findAllBySubjectId(subjectId);
    }
}
