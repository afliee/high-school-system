//package com.highschool.highschoolsystem.service;
//
//import com.highschool.highschoolsystem.config.SubmitStatus;
//import com.highschool.highschoolsystem.entity.AttendanceEntity;
//import com.highschool.highschoolsystem.entity.ClassEntity;
//import com.highschool.highschoolsystem.entity.LessonEntity;
//import com.highschool.highschoolsystem.entity.SemesterEntity;
//import com.highschool.highschoolsystem.service.*;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.util.*;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class StatisticServiceTest {
//
//    @Mock
//    private StudentService studentService;
//    @Mock
//    private TeacherService teacherService;
//    @Mock
//    private ClassService classService;
//    @Mock
//    private SubjectService subjectService;
//    @Mock
//    private SemesterService semesterService;
//    @Mock
//    private LevelService levelService;
//    @Mock
//    private LessonService lessonService;
//
//    @InjectMocks
//    private StatisticService statisticService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void testGetStatisticHeader() {
//        // Mock the behavior of the dependent services
//        SemesterEntity semesterEntity = SemesterEntity.builder().build();
//        when(studentService.countStudent()).thenReturn(100L);
//        when(teacherService.count()).thenReturn(10L);
//        when(classService.count()).thenReturn(5L);
//        when(subjectService.count()).thenReturn(8L);
//        when(semesterService.findCurrentSemesterEntity()).thenReturn(semesterEntity);
//
//        // Call the getStatisticHeader() method
//        Map<String, Object> result = statisticService.getStatisticHeader();
//
//        // Verify the expected values in the result
//        assertEquals(100, result.get("totalStudents"));
//        assertEquals(10, result.get("totalTeachers"));
//        assertEquals(5, result.get("totalClasses"));
//        assertEquals(8, result.get("totalSubjects"));
//        assertEquals("2023 Fall Semester", result.get("currentSemester"));
//
//        // Verify that the dependent services were called
//        verify(studentService, times(1)).countStudent();
//        verify(teacherService, times(1)).count();
//        verify(classService, times(1)).count();
//        verify(subjectService, times(1)).count();
//        verify(semesterService, times(1)).findCurrentSemesterEntity();
//    }
//
//    @Test
//    void testGetAttendanceStatistic() {
//        // Mock the behavior of the dependent services
//        List<String> levels = Arrays.asList("level1", "level2");
//        when(levelService.getAllLevelId()).thenReturn(levels);
//
//        List<ClassEntity> classesLevel1 = Arrays.asList(
//                createClassEntity("class1", 10),
//                createClassEntity("class2", 15)
//        );
//        List<ClassEntity> classesLevel2 = Collections.singletonList(
//                createClassEntity("class3", 20)
//        );
//        when(classService.findClassCurrentSemesterAndLevel("level1")).thenReturn(classesLevel1);
//        when(classService.findClassCurrentSemesterAndLevel("level2")).thenReturn(classesLevel2);
//
//        // Mock the behavior of the attendances
//        AttendanceEntity attendance1 = createAttendanceEntity(1, 1);
//        AttendanceEntity attendance2 = createAttendanceEntity(0, 1);
//        AttendanceEntity attendance3 = createAttendanceEntity(1, 1);
//        AttendanceEntity attendance4 = createAttendanceEntity(1, 1);
//        AttendanceEntity attendance5 = createAttendanceEntity(1, 1);
//        List<AttendanceEntity> attendancesClass1 = Arrays.asList(attendance1, attendance2);
//        List<AttendanceEntity> attendancesClass2 = Arrays.asList(attendance3, attendance4, attendance5);
//        when(lessonService.getAttendances(any())).thenReturn(attendancesClass1, attendancesClass2);
//
//        // Call the getAttendanceStatistic() method
//        Map<String, Double> result = statisticService.getAttendanceStatistic();
//
//        // Verify the expected values in the result
//        assertEquals(2, result.size());
//        assertEquals(50.0, result.get("class1"));
//        assertEquals(60.0, result.get("class3"));
//
//        // Verify that the dependent services were called
//        verify(levelService, times(1)).getAllLevelId();
//        verify(classService, times(1)).findClassCurrentSemesterAndLevel("level1");
//        verify(classService, times(1)).findClassCurrentSemesterAndLevel("level2");
//        verify(lessonService, times(2)).getAttendances(any());
//    }
//
//    @Test
//    void testGetAttendanceStatistic_NoLevels_ReturnsEmptyMap() {
//        // Mock the behavior of the dependent services
//        when(levelService.getAllLevelId()).thenReturn(Collections.emptyList());
//
//        // Call the getAttendanceStatistic() method
//        Map<String, Double> result = statisticService.getAttendanceStatistic();
//
//        // Verify that the result is an empty map
//        assertTrue(result.isEmpty());
//
//        // Verify that the dependentservices were called
//        verify(levelService, times(1)).getAllLevelId();
//        verify(classService, never()).findClassCurrentSemesterAndLevel(any());
//        verify(lessonService, never()).getAttendances(any());
//    }
//
//    @Test
//    void testGetAcademicPerformanceStatistic() {
//        // Mock the behavior of the dependent services
//        List<LevelEntity> levels = Arrays.asList(
//                createLevelEntity("level1", Arrays.asList(createSubjectEntity("subject1"), createSubjectEntity("subject2"))),
//                createLevelEntity("level2", Collections.singletonList(createSubjectEntity("subject3")))
//        );
//        when(levelService.getAllLevel()).thenReturn(levels);
//
//        // Mock the behavior of the dependent services
//        List<AssignmentEntity> assignmentsSubject1 = Arrays.asList(
//                createAssignmentEntity(SubmitStatus.SUBMITTED),
//                createAssignmentEntity(SubmitStatus.GRADED),
//                createAssignmentEntity(SubmitStatus.NOT_SUBMITTED)
//        );
//        List<AssignmentEntity> assignmentsSubject2 = Collections.singletonList(
//                createAssignmentEntity(SubmitStatus.SUBMITTED)
//        );
//        List<AssignmentEntity> assignmentsSubject3 = Arrays.asList(
//                createAssignmentEntity(SubmitStatus.GRADED),
//                createAssignmentEntity(SubmitStatus.GRADED)
//        );
//        when(subjectService.getAssignments(1)).thenReturn(assignmentsSubject1);
//        when(subjectService.getAssignments(2)).thenReturn(assignmentsSubject2);
//        when(subjectService.getAssignments(3)).thenReturn(assignmentsSubject3);
//
//        List<StudentEntity> studentsSubject1 = Arrays.asList(
//                createStudentEntity("student1"),
//                createStudentEntity("student2")
//        );
//        List<StudentEntity> studentsSubject2 = Collections.singletonList(
//                createStudentEntity("student3")
//        );
//        List<StudentEntity> studentsSubject3 = Arrays.asList(
//                createStudentEntity("student4"),
//                createStudentEntity("student5"),
//                createStudentEntity("student6")
//        );
//        when(subjectService.getStudents(1)).thenReturn(studentsSubject1);
//        when(subjectService.getStudents(2)).thenReturn(studentsSubject2);
//        when(subjectService.getStudents(3)).thenReturn(studentsSubject3);
//
//        // Call the getAcademicPerformanceStatistic() method
//        Map<String, Map<String, Double>> result = statisticService.getAcademicPerformanceStatistic();
//
//        // Verify the expected values in the result
//        assertEquals(2, result.size());
//        assertTrue(result.containsKey("level1"));
//        assertTrue(result.containsKey("level2"));
//        Map<String, Double> level1Result = result.get("level1");
//        Map<String, Double> level2Result = result.get("level2");
//        assertEquals(2, level1Result.size());
//        assertEquals(1, level2Result.size());
//        assertEquals(66.66666666666666, level1Result.get("subject1"));
//        assertEquals(100.0, level1Result.get("subject2"));
//        assertEquals(66.66666666666666, level2Result.get("subject3"));
//
//        // Verify that the dependent services were called
//        verify(levelService, times(1)).getAllLevel();
//        verify(subjectService, times(1)).getAssignments(1);
//        verify(subjectService, times(1)).getAssignments(2);
//        verify(subjectService, times(1)).getAssignments(3);
//        verify(subjectService, times(1)).getStudents(1);
//        verify(subjectService, times(1)).getStudents(2);
//        verify(subjectService, times(1)).getStudents(3);
//    }
//
//    @Test
//    void testGetAcademicPerformanceStatistic_NoLevels_ReturnsEmptyMap() {
//        // Mock the behavior of the dependent services
//        when(levelService.getAllLevel()).thenReturn(Collections.emptyList());
//
//        // Call the getAcademicPerformanceStatistic() method
//        Map<String, Map<String, Double>> result = statisticService.getAcademicPerformanceStatistic();
//
//        // Verify that the result is an empty map
//        assertTrue(result.isEmpty());
//
//        // Verify that the dependent services were called
//        verify(levelService, times(1)).getAllLevel();
//        verify(subjectService, never()).getAssignments(any());
//        verify(subjectService, never()).getStudents(any());
//    }
//
//    @Test
//    void testGetLessonToday_LimitNegative_ReturnsEmptyList() {
//        // Call the getLessonToday() method with a negative limit
//        List<LessonEntity> result = statisticService.getLessonToday(-2);
//
//        // Verify that the result is an empty list
//        assertTrue(result.isEmpty());
//
//        // Verify that the lessonService.getLessonToday() method was not called
//        verify(lessonService, never()).getLessonToday(anyInt());
//    }
//
//    @Test
//    void testGetLessonToday_LimitZero_ReturnsEmptyList() {
//        // Call the getLessonToday() method with a limit of zero
//        List<LessonEntity> result = statisticService.getLessonToday(0);
//
//        // Verify that the result is an empty list
//        assertTrue(result.isEmpty());
//
//        // Verify that the lesson