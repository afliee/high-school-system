package com.highschool.highschoolsystem.service;

import com.highschool.highschoolsystem.config.FaultType;
import com.highschool.highschoolsystem.dto.request.FaultRequest;
import com.highschool.highschoolsystem.entity.ClassEntity;
import com.highschool.highschoolsystem.entity.FaultDetailEntity;
import com.highschool.highschoolsystem.entity.FaultEntity;
import com.highschool.highschoolsystem.entity.StudentEntity;
import com.highschool.highschoolsystem.repository.FaultDetailRepository;
import com.highschool.highschoolsystem.repository.FaultRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class FaultServiceTest {
    @Mock
    private FaultRepository faultRepository;

    @Mock
    private FaultDetailRepository faultDetailRepository;

    @Mock
    private StudentService studentService;

    @Mock
    private ClassService classService;

    @Mock
    private SubjectService subjectService;

    @InjectMocks
    private FaultService faultService;

    @BeforeEach
    void setUp() {
        // Initialize Mockito annotations
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testCreate_ValidRequest_CreatesFaultEntity() {
        // Mock the dependencies
        String studentId = "1";
        String subjectId = "2";
        String otherFault = "Other fault description";
        String[] faults = new String[] {FaultType.OTHER.name()};

        FaultRequest request = new FaultRequest();
        request.setStudentId(studentId);
        request.setSubjectId(subjectId);
        request.setOtherFault(otherFault);
        request.setFaults(faults);

        StudentEntity studentEntity = new StudentEntity();
        studentEntity.setId(studentId);

        ClassEntity classEntity = new ClassEntity();
        classEntity.setId("classId");

        when(studentService.findOne(studentId)).thenReturn(studentEntity);
        when(classService.findById(anyString())).thenReturn(classEntity);
        when(subjectService.hasSubject(any(ClassEntity.class), anyString())).thenReturn(true);

        // Call the create() method of the faultService
        faultService.create(request);

        // Verify that the faultRepository.save() method is called
        verify(faultRepository, times(1)).save(any(FaultEntity.class));
    }

    @Test
    void testCreate_InvalidRequestWithMissingOtherFault_ThrowsRuntimeException() {
        // Mock the dependencies
        String studentId = "1";
        String subjectId = "2";
        String[] faults = new String[]{FaultType.OTHER.name()};

        FaultRequest request = new FaultRequest();
        request.setStudentId(studentId);
        request.setSubjectId(subjectId);
        request.setFaults(faults);

        StudentEntity studentEntity = new StudentEntity();
        studentEntity.setId(studentId);

        ClassEntity classEntity = new ClassEntity();
        classEntity.setId("classId");

        when(studentService.findOne(studentId)).thenReturn(studentEntity);
        when(classService.findById(anyString())).thenReturn(classEntity);
        when(subjectService.hasSubject(any(ClassEntity.class), anyString())).thenReturn(true);

        // Call the create() method of the faultService and verify that it throws a RuntimeException
        assertThrows(RuntimeException.class, () -> faultService.create(request));
    }

    @Test
    void testFindFaultByStudentIdAndSubjectId_ReturnsListOfFaultEntities() {
        // Mock the behavior of the faultRepository.findAllByStudentIdAndSubject() method
        String studentId = "1";
        String subjectId = "2";
        List<FaultEntity> expectedFaults = new ArrayList<>();
        expectedFaults.add(new FaultEntity());

        when(faultRepository.findAllByStudentIdAndSubject(studentId, subjectId)).thenReturn(expectedFaults);

        // Call the findFaultByStudentIdAndSubjectId() method of the faultService
        List<FaultEntity> actualFaults = faultService.findFaultByStudentIdAndSubjectId(studentId, subjectId);

        // Verify the result
        assertEquals(expectedFaults, actualFaults);
    }

    @Test
    void testFindFaultByStudentIdAndSubjectIdGroupByDate_ReturnsMapOfFaultEntitiesGroupedByDate() {
        // Mock the behavior of the faultRepository.findAllByStudentIdAndSubject() method
        String studentId = "1";
        String subjectId = "2";
        List<FaultEntity> expectedFaults = new ArrayList<>();
        expectedFaults.add(new FaultEntity());

        when(faultRepository.findAllByStudentIdAndSubject(studentId, subjectId)).thenReturn(expectedFaults);

        Map<LocalDate, List<FaultEntity>> actualFaults = faultService.findFaultByStudentIdAndSubjectIdGroupByDate(studentId, subjectId);

        // Verify the result
        assertEquals(expectedFaults.size(), actualFaults.size());
        assertTrue(actualFaults.containsKey(expectedFaults.get(0).getCreatedDate()));
        assertEquals(expectedFaults, actualFaults.get(expectedFaults.get(0).getCreatedDate()));
    }

    @Test
    void testCreate_InvalidRequestWithNonexistentSubject_ThrowsRuntimeException() {
        // Mock the dependencies
        String studentId = "1";
        String subjectId = "2";
        String[] faults = new String[] {FaultType.OTHER.name()};

        FaultRequest request = new FaultRequest();
        request.setStudentId(studentId);
        request.setSubjectId(subjectId);
        request.setFaults(faults);

        StudentEntity studentEntity = new StudentEntity();
        studentEntity.setId(studentId);

        ClassEntity classEntity = new ClassEntity();
        classEntity.setId("classId");

        when(studentService.findOne(studentId)).thenReturn(studentEntity);
        when(classService.findById(anyString())).thenReturn(classEntity);
        when(subjectService.hasSubject(any(ClassEntity.class), anyString())).thenReturn(false);

        // Call the create() method of the faultService and verify that it throws a RuntimeException
        assertThrows(RuntimeException.class, () -> faultService.create(request));
    }

    @Test
    void testFindFaultByStudentIdAndSubjectId_ExceptionInRepository_ThrowsRuntimeException() {
        // Mock the behavior of the faultRepository.findAllByStudentIdAndSubject() method
        String studentId = "1";
        String subjectId = "2";

        when(faultRepository.findAllByStudentIdAndSubject(studentId, subjectId)).thenThrow(new RuntimeException("Database error"));

        // Call the findFaultByStudentIdAndSubjectId() method of the faultService and verify that it throws a RuntimeException
        assertThrows(RuntimeException.class, () -> faultService.findFaultByStudentIdAndSubjectId(studentId, subjectId));
    }

    @Test
    void testFindFaultByStudentIdAndSubjectIdGroupByDate_ExceptionInRepository_ThrowsRuntimeException() {
        // Mock the behavior of the faultRepository.findAllByStudentIdAndSubject() method
        String studentId = "1";
        String subjectId = "2";

        when(faultRepository.findAllByStudentIdAndSubject(studentId, subjectId)).thenThrow(new RuntimeException("Database error"));

        // Call the findFaultByStudentIdAndSubjectIdGroupByDate() method of the faultService and verify that it throws a RuntimeException
        assertThrows(RuntimeException.class, () -> faultService.findFaultByStudentIdAndSubjectIdGroupByDate(studentId, subjectId));
    }
}