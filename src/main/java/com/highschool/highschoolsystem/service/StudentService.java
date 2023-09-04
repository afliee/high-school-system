package com.highschool.highschoolsystem.service;

import com.highschool.highschoolsystem.converter.StudentConverter;
import com.highschool.highschoolsystem.converter.SubmittingConverter;
import com.highschool.highschoolsystem.dto.request.UpdateStudentRequest;
import com.highschool.highschoolsystem.dto.response.StudentResponse;
import com.highschool.highschoolsystem.dto.response.SubmittingResponse;
import com.highschool.highschoolsystem.entity.StudentEntity;
import com.highschool.highschoolsystem.exception.NotFoundException;
import com.highschool.highschoolsystem.repository.StudentRepository;
import com.highschool.highschoolsystem.util.FileUploadUtils;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class StudentService {
    @Autowired
    private StudentRepository studentRepository;

    public StudentResponse get(String id) {
        return studentRepository.findById(id)
                .map(StudentConverter::toResponse)
                .orElseThrow(() -> new NotFoundException("Student not found"));
    }

    public StudentEntity findOne(String id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Student not found"));
    }

    public StudentResponse update(String id, UpdateStudentRequest request) {
        StudentEntity studentEntity = studentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Student not found"));

        String uploadDir = "/uploads/images/user-photos/";
        String fileName = studentEntity.getId() + ".jpg";
        System.out.println("request " + request);
        studentEntity.setEmail(request.getEmail());
        studentEntity.setLocation(request.getLocation());
        studentEntity.setCardId(request.getCardId());
        studentEntity.setAvatar(uploadDir + fileName);

        if (request.getGender() != null) {
            studentEntity.setGender(Integer.parseInt(request.getGender()) != 0);
        }
        studentRepository.save(studentEntity);
        try {
            if (request.getAvatar() != null) {
                FileUploadUtils.saveFile(uploadDir, fileName, request.getAvatar());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return StudentConverter.toResponse(studentEntity);
    }

    public List<SubmittingResponse> getSubmittingTodayByStudent(String id, String subjectId) {
        var student = studentRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Student not found")
        );

        var now = LocalDateTime.now();
        return SubmittingConverter.toResponse(student.getSubmittingSet().stream().filter(submitting -> {
            if (!submitting.getAssignment().isDue()) {
                return false;
            }
            var subject = submitting.getAssignment().getSubject();
            if (!subjectId.equals(subject.getId())) {
                return false;
            }
            var submittingEndDate = submitting.getAssignment().getClosedDate();
            return submittingEndDate.isAfter(now.minusDays(1)) && submittingEndDate.isBefore(now.plusDays(1)) && submitting.getFile() == null;
        }).toList());
    }

    public long countStudent() {
        return studentRepository.count();
    }
}
