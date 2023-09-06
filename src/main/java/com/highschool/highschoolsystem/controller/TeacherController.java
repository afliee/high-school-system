package com.highschool.highschoolsystem.controller;

import com.highschool.highschoolsystem.config.FaultType;
import com.highschool.highschoolsystem.exception.NotFoundException;
import com.highschool.highschoolsystem.service.*;
import com.highschool.highschoolsystem.util.BreadCrumb;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.WebUtils;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/teacher")
@Tag(name = "Teacher", description = "Teacher views for CRUD")
public class TeacherController {
    private final JwtService jwtService;
    private final TeacherService teacherService;
    private final ClassService classService;
    private final StudentService studentService;
    private final SubjectService subjectService;
    private final AssignmentService assignmentService;
    private final FaultService faultService;

    @GetMapping({"/home", "/", ""})
    public String index(
            HttpServletRequest request,
            HttpServletResponse response,
            Model model
    ) {
        var tokenCookie = WebUtils.getCookie(request, "token");
        if (tokenCookie == null || tokenCookie.getValue().isEmpty()) {
            return "redirect:/login?with=teacher";
        }

        if (!jwtService.validateToken(tokenCookie.getValue())) {
            return "redirect:/login?with=teacher";
        }

        if (!jwtService.isTeacher(tokenCookie.getValue())) {
            return "redirect:/login?with=teacher";
        }

        var username = jwtService.extractUsername(tokenCookie.getValue());
        var teacher = teacherService.findByUsername(username).orElseThrow(
                () -> new NotFoundException("Teacher not found")
        );

        model.addAttribute("teacher", teacher);
        return "redirect:/teacher/form-teacher";
    }

    @GetMapping("/form-teacher")
    public String formTeacher(
            HttpServletRequest request,
            HttpServletResponse response,
            Model model
    ) {

        var tokenCookie = WebUtils.getCookie(request, "token");
        String isTeacher = teacherService.requireTeacherToken(tokenCookie);
        if (isTeacher != null) return isTeacher;

        assert tokenCookie != null;
        var username = jwtService.extractUsername(tokenCookie.getValue());
        var teacher = teacherService.findByUsername(username).orElseThrow(
                () -> new NotFoundException("Teacher not found")
        );

        var classEntity = classService.findByTeacher(teacher.getId());
        if (classEntity == null) {
            return "redirect:/?component=chooseLogin";
        }

        model.addAttribute("classEntity", classEntity);
        model.addAttribute("teacher", teacher);
        return "pages/teacher/form-teacher";
    }

    @GetMapping("/form-teacher/{studentId}")
    public String studentDetail(
            @PathVariable String studentId,
            HttpServletRequest request,
            HttpServletResponse response,
            Model model
    ) {
        var tokenCookie = WebUtils.getCookie(request, "token");

        String isTeacher = teacherService.requireTeacherToken(tokenCookie);
        if (isTeacher != null) return isTeacher;

        assert tokenCookie != null;
        var username = jwtService.extractUsername(tokenCookie.getValue());
        var teacher = teacherService.findByUsername(username).orElseThrow(
                () -> new NotFoundException("Teacher not found")
        );

        var student = studentService.findOne(studentId);

        if (student == null) {
            return "redirect:/?component=chooseLogin";
        }

        var breadCrumbs = generateBreadCrumbs();
        breadCrumbs.add(new BreadCrumb(student.getFullName(), "/teacher/form-teacher/" + studentId, true));

        model.addAttribute("breadCrumbs", breadCrumbs);
        model.addAttribute("teacher", teacher);
        model.addAttribute("student", student);

        return "pages/teacher/student-detail";
    }

    @GetMapping("/enroll/{subjectId}")
    public String getSubjectEnroll(
            @PathVariable String subjectId,
            HttpServletRequest request,
            HttpServletResponse response,
            Model model
    ) {
        var tokenCookie = WebUtils.getCookie(request, "token");

        String isTeacher = teacherService.requireTeacherToken(tokenCookie);
        if (isTeacher != null) return isTeacher;

        assert tokenCookie != null;
        var username = jwtService.extractUsername(tokenCookie.getValue());
        var teacher = teacherService.findByUsername(username).orElseThrow(
                () -> new NotFoundException("Teacher not found")
        );

        var subject = subjectService.findById(subjectId);
        var students = subjectService.getStudents(subjectId);
        var todos = assignmentService.getAssigmentBySubjectId(subjectId);

        var breadCrumbs = generateBreadCrumbs();
        breadCrumbs.add(new BreadCrumb(subject.getName(), "/teacher/enroll/" + subjectId, true));

        model.addAttribute("breadCrumbs", breadCrumbs);
        model.addAttribute("teacher", teacher);
        model.addAttribute("subject", subject);
        model.addAttribute("students", students);
        model.addAttribute("todos", todos);

        return "pages/teacher/enroll";
    }

    @GetMapping("/enroll/update/{assignmentId}")
    public String updateAssignment(
            @PathVariable String assignmentId,
            HttpServletRequest request,
            HttpServletResponse response,
            Model model
    ) {
        var tokenCookie = WebUtils.getCookie(request, "token");

        String isTeacher = teacherService.requireTeacherToken(tokenCookie);
        if (isTeacher != null) return isTeacher;

        assert tokenCookie != null;
        var username = jwtService.extractUsername(tokenCookie.getValue());
        var teacher = teacherService.findByUsername(username).orElseThrow(
                () -> new NotFoundException("Teacher not found")
        );

        var assignment = assignmentService.findById(assignmentId);

        var breadCrumbs = generateBreadCrumbs();
        breadCrumbs.add(new BreadCrumb(assignment.getTitle(), "/teacher/enroll/update/" + assignmentId, true));

        model.addAttribute("breadCrumbs", breadCrumbs);
        model.addAttribute("teacher", teacher);
        model.addAttribute("assignment", assignment);

        return "pages/teacher/editAssignment";
    }


    @GetMapping("/assign/{subjectId}")
    public String getSubjectAssign(
            @PathVariable String subjectId,
            HttpServletRequest request,
            HttpServletResponse response,
            Model model
    ) {
        var tokenCookie = WebUtils.getCookie(request, "token");

        String isTeacher = teacherService.requireTeacherToken(tokenCookie);
        if (isTeacher != null) return isTeacher;

        assert tokenCookie != null;
        var username = jwtService.extractUsername(tokenCookie.getValue());
        var teacher = teacherService.findByUsername(username).orElseThrow(
                () -> new NotFoundException("Teacher not found")
        );

        var subject = subjectService.findById(subjectId);

        var breadCrumbs = generateBreadCrumbs();
        breadCrumbs.add(new BreadCrumb(subject.getName(), "/teacher/assign/" + subjectId, true));

        model.addAttribute("breadCrumbs", breadCrumbs);
        model.addAttribute("teacher", teacher);
        model.addAttribute("subject", subject);


        return "pages/teacher/assign";
    }

    @GetMapping("/grade/{assignmentId}") // /teacher/grade/{assignmentId}
    public String grade(
            @PathVariable String assignmentId,
            @RequestParam(name = "filter", required = false, defaultValue = "all") String filter, // all, turned-in, unturned-in
            HttpServletRequest request,
            HttpServletResponse response,
            Model model
    ) {
        var tokenCookie = WebUtils.getCookie(request, "token");

        String isTeacher = teacherService.requireTeacherToken(tokenCookie);
        if (isTeacher != null) return isTeacher;

        assert tokenCookie != null;
        var username = jwtService.extractUsername(tokenCookie.getValue());
        var teacher = teacherService.findByUsername(username).orElseThrow(
                () -> new NotFoundException("Teacher not found")
        );

        var assignment = assignmentService.findById(assignmentId);

        switch (filter) {
            case "turned-in" -> assignment.getSubmitting().removeIf(Submitting -> Submitting.getFile() == null);
            case "unturned-in" -> assignment.getSubmitting().removeIf(Submitting -> Submitting.getFile() != null);
            default -> {
            } // all
        }


        int totalStudentTurnedIn = assignment.getSubmitting().stream().filter(Submitting -> Submitting.getFile() != null).toArray().length;
        int totalStudentUnTurnedIn = assignment.getSubmitting().size() - totalStudentTurnedIn;
        var breadCrumbs = generateBreadCrumbs();
        breadCrumbs.add(new BreadCrumb(assignment.getTitle(), "/teacher/grade/" + assignmentId, true));
        model.addAttribute("breadCrumbs", breadCrumbs);
        model.addAttribute("teacher", teacher);
        model.addAttribute("assignment", assignment);
        model.addAttribute("totalStudentTurnedIn", totalStudentTurnedIn);
        model.addAttribute("totalStudentUnTurnedIn", totalStudentUnTurnedIn);
        model.addAttribute("filter", filter);
        return "pages/teacher/grade";
    }

    @GetMapping("/fault/check")
    public String faultCheck(
            @RequestParam(name = "studentId", defaultValue = "" , required = true) String studentId,
            @RequestParam(name = "subjectId", defaultValue = "" , required = true) String subjectId,
            HttpServletRequest request,
            HttpServletResponse response,
            Model model
    )  {
        var tokenCookie = WebUtils.getCookie(request, "token");

        String isTeacher = teacherService.requireTeacherToken(tokenCookie);
        if (isTeacher != null) return isTeacher;

        assert tokenCookie != null;
        var username = jwtService.extractUsername(tokenCookie.getValue());
        var teacher = teacherService.findByUsername(username).orElseThrow(
                () -> new NotFoundException("Teacher not found")
        );
        var student = studentService.findOne(studentId);
        var faults = faultService.findFaultByStudentIdAndSubjectIdGroupByDate(studentId, subjectId);

        var breadCrumbs = generateBreadCrumbs();
        breadCrumbs.add(new BreadCrumb(student.getFullName(), "/teacher/fault/check?studentId=" + studentId, true));
        model.addAttribute("breadCrumbs", breadCrumbs);
        model.addAttribute("teacher", teacher);
        model.addAttribute("student", student);
        model.addAttribute("subjectId", subjectId);
        model.addAttribute("faults", faults);

        return "pages/teacher/check_fault";
    }

    @GetMapping("/fault")
    public String fault(
            @RequestParam(name = "studentId", defaultValue = "" , required = true) String studentId,
            @RequestParam(name = "subjectId", defaultValue = "" , required = true) String subjectId,
            HttpServletRequest request,
            HttpServletResponse response,
            Model model
    ) {
        var tokenCookie = WebUtils.getCookie(request, "token");

        String isTeacher = teacherService.requireTeacherToken(tokenCookie);
        if (isTeacher != null) return isTeacher;

        assert tokenCookie != null;
        var username = jwtService.extractUsername(tokenCookie.getValue());
        var teacher = teacherService.findByUsername(username).orElseThrow(
                () -> new NotFoundException("Teacher not found")
        );
        var student = studentService.findOne(studentId);


        model.addAttribute("breadCrumbs", List.of(
                new BreadCrumb("Home", "/teacher/home", false),
                new BreadCrumb("Teacher", "/teacher/enroll/" + subjectId, false),
                new BreadCrumb(student.getFullName(), "/teacher/fault?studentId=" + studentId, false)
        ));
        model.addAttribute("teacher", teacher);
        model.addAttribute("student", student);
        model.addAttribute("subjectId", subjectId);
        model.addAttribute("faults", FaultType.values());
        return "pages/teacher/create_fault";
    }


    private List<BreadCrumb> generateBreadCrumbs() {
        var breadCrumbs = new ArrayList<BreadCrumb>();
        breadCrumbs.add(new BreadCrumb("Home", "/teacher/home", false));
        breadCrumbs.add(new BreadCrumb("Teacher", "/teacher/form-teacher", false));
        return breadCrumbs;
    }
}
