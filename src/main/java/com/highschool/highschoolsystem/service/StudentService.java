package com.highschool.highschoolsystem.service;

import com.highschool.highschoolsystem.converter.StudentConverter;
import com.highschool.highschoolsystem.dto.request.UpdateStudentRequest;
import com.highschool.highschoolsystem.dto.response.StudentResponse;
import com.highschool.highschoolsystem.entity.StudentEntity;
import com.highschool.highschoolsystem.exception.NotFoundException;
import com.highschool.highschoolsystem.repository.StudentRepository;
import com.highschool.highschoolsystem.util.FileUploadUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentService {
    @Autowired
    private StudentRepository studentRepository;

    public StudentResponse get(String id) {
        return studentRepository.findById(id)
                .map(StudentConverter::toResponse)
                .orElseThrow(() -> new NotFoundException("Student not found"));
    }

    public StudentResponse update(String id, UpdateStudentRequest request) {
        StudentEntity studentEntity = studentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Student not found"));

        String uploadDir = "/uploads/images/user-photos/";
        String fileName = studentEntity.getId() + ".jpg";

        studentEntity.setEmail(request.getEmail());
        studentEntity.setLocation(request.getLocation());
        studentEntity.setCardId(request.getCardId());
        studentEntity.setAvatar(uploadDir + fileName);

        studentRepository.save(studentEntity);
        try {
            FileUploadUtils.saveFile(uploadDir, fileName, request.getAvatar());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return StudentConverter.toResponse(studentEntity);
    }
}
