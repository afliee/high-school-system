package com.highschool.highschoolsystem.service;

import com.highschool.highschoolsystem.config.FaultType;
import com.highschool.highschoolsystem.dto.request.FaultRequest;
import com.highschool.highschoolsystem.entity.FaultDetailEntity;
import com.highschool.highschoolsystem.entity.FaultEntity;
import com.highschool.highschoolsystem.exception.NotFoundException;
import com.highschool.highschoolsystem.repository.FaultDetailRepository;
import com.highschool.highschoolsystem.repository.FaultRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class FaultService {
    @Autowired
    private FaultRepository faultRepository;
    @Autowired
    private FaultDetailRepository faultDetailRepository;
    @Autowired
    private StudentService studentService;
    @Autowired
    private ClassService classService;
    @Autowired
    private SubjectService subjectService;

    @Transactional
    public void create(FaultRequest request) {
        try {
            var student = studentService.findOne(request.getStudentId());
            var clazz = classService.findById(student.getClassEntity().getId());
            var hasSubject = subjectService.hasSubject(clazz, request.getSubjectId());

            if (!hasSubject) {
                throw new RuntimeException("Student does not have this subject");
            }

            var fault = FaultEntity.builder()
                    .student(student)
                    .subject(request.getSubjectId())
                    .build();

            List<FaultDetailEntity> faultDetails = new ArrayList<>();
            for (String faultRequest : request.getFaults()) {
                if (faultRequest.equals(FaultType.OTHER.name())) {
                    if (request.getOtherFault() == null) {
                        throw new RuntimeException("Other fault must not be empty");
                    }
                    var faultDetail  = FaultDetailEntity.builder()
                            .faultType(FaultType.OTHER)
                            .faultDescription(request.getOtherFault())
                            .fault(fault);

                    faultDetails.add(faultDetail.build());
                } else {
                    var faultType = FaultType.valueOf(faultRequest);

                    var faultDetail = FaultDetailEntity.builder()
                            .faultType(faultType)
                            .faultDescription(faultType.getFaultDescription())
                            .fault(fault);
                    faultDetails.add(faultDetail.build());
                }
            }

            fault.setFaultDetails(new HashSet<>(faultDetails));
            faultRepository.save(fault);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error while creating fault");
        }
    }

    public List<FaultEntity> findFaultByStudentIdAndSubjectId(String studentId, String subjectId) {
        try {
            return faultRepository.findAllByStudentIdAndSubject(studentId, subjectId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error while finding fault");
        }
    }

    public Map<LocalDate, List<FaultEntity>> findFaultByStudentIdAndSubjectIdGroupByDate(String studentId, String subjectId) {
        try {
            return faultRepository.findAllByStudentIdAndSubject(studentId, subjectId)
                    .stream()
                    .collect(Collectors.groupingBy(FaultEntity::getCreatedDate));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error while finding fault");
        }
    }

    /*
    * [TODO] Delete a fault detail of a fault
    * */
    public void delete(String faultDetailId) {
        try {
            var faultDetail = faultDetailRepository.findById(faultDetailId)
                    .orElseThrow(() -> new NotFoundException("Fault detail not found"));


            // Delete fault detail
            System.out.println("Deleting fault detail: " + faultDetailId);
            faultDetailRepository.deleteById(faultDetailId);

            // Delete fault if it has no fault detail
            var fault = faultDetail.getFault();
            if (fault.getFaultDetails().isEmpty()) {
                System.out.println("Deleting fault: " + fault.getId());
                faultRepository.deleteById(fault.getId());
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error while deleting fault");
        }
    }
}
