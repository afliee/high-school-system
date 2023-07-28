package com.highschool.highschoolsystem.controller;

import com.highschool.highschoolsystem.service.ShiftService;
import com.highschool.highschoolsystem.dto.response.LevelResponse;
import com.highschool.highschoolsystem.dto.response.SemesterResponse;
import com.highschool.highschoolsystem.service.*;
import com.highschool.highschoolsystem.util.BreadCrumb;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.logging.Logger;

@Controller
@RequestMapping("/auth/admin")
@Tag(name = "Admin", description = "Admin views for CRUD")
public class AdminController {
    Logger logger = Logger.getLogger(AdminController.class.getName());

    @Autowired
    private AdminService adminService;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private SubjectService subjectService;
    @Autowired
    private SemesterService semesterService;
    @Autowired
    private DayService dayService;
    @Autowired
    private ShiftService shiftService;
    @Autowired
    private LevelService levelService;

    @GetMapping({"/", "", "/dashboard"})
    public String index(
            HttpServletRequest request,
            HttpServletRequest response
    ) {

        String redirect = adminService.requireAdminLogin(request);

        if (redirect != null) {
            return redirect;
        }

        return "pages/admin/dashboard";
    }

    @GetMapping("/login")
    public String login(
            HttpServletRequest request,
            HttpServletRequest response
    ) {
        return "pages/auth/adminSignin";
    }

    @GetMapping("/members")
    public String teachers(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam(name = "filter", required = true, defaultValue = "") String filter,
            @RequestParam(name = "page", required = true, defaultValue = "1") int page,
            Model model
    ) {
        String redirect = adminService.requireAdminLogin(request);
        model.addAttribute("filter", filter);
        model.addAttribute("page", page);
        return redirect != null ? redirect : "pages/admin/" + filter + "s";
    }

    @GetMapping("/classes")
    public String classes(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        String redirect = adminService.requireAdminLogin(request);

        return redirect != null ? redirect : "pages/admin/classes";
    }

    @GetMapping("/classes/{classId}")
    public String classDetails(
            HttpServletRequest request,
            @PathVariable String classId,
            Model model
    ) {
        String redirect = adminService.requireAdminLogin(request);

        if (redirect != null) {
            return redirect;
        }
        model.addAttribute("classId", classId);
        return "pages/admin/classDetails";
    }

    @GetMapping("/subjects")
    public String subjects(
            HttpServletRequest request,
            HttpServletResponse response,
            Model model
    ) {
        String redirect = adminService.requireAdminLogin(request);

        List<BreadCrumb> breadCrumbs = adminService.getSubjectBreadCrumbs("");

        model.addAttribute("breadCrumbs", breadCrumbs);

        return redirect != null ? redirect : "pages/admin/subjects";
    }

    @GetMapping("/subjects/add")
    public String addSubject(
            @RequestParam(name = "departmentId", defaultValue = "") String departmentId,
            HttpServletRequest request,
            Model model
    ) {
        String redirect = adminService.requireAdminLogin(request);

        List<BreadCrumb> breadCrumbs = adminService.getDepartmentBreadCrumbs(departmentId);

        var department = departmentService.findById(departmentId);

        model.addAttribute("departmentId", departmentId);
        model.addAttribute("breadCrumbs", breadCrumbs);
        model.addAttribute("department", department);

        return redirect != null ? redirect : "pages/admin/addSubject";
    }

    @GetMapping("/subjects/{subjectId}")
    public String subjectDetail(
            @PathVariable String subjectId,
            HttpServletRequest request,
            Model model
    ) {
        String redirect = adminService.requireAdminLogin(request);
        var semester = semesterService.findCurrentSemester();

        List<BreadCrumb> breadCrumbs = adminService.getSubjectBreadCrumbs(subjectId);

        logger.info(semester.toString());
        model.addAttribute("breadCrumbs", breadCrumbs);
        model.addAttribute("subjectId", subjectId);
        model.addAttribute("semester", semester);

        return redirect != null ? redirect : "pages/admin/subjectDetails";
    }

    @GetMapping("/subjects/{subjectId}/lessons/add")
    public String addLessons(
            @RequestParam(value = "semesterId", defaultValue = "") String semesterId,
            @PathVariable String subjectId,
            HttpServletRequest request,
            Model model
    ) {
        String redirect = adminService.requireAdminLogin(request);
        SemesterResponse semester = null;
        if (semesterId.isEmpty()) {
            semester = semesterService.findCurrentSemester();
        } else {
            semester = semesterService.findById(semesterId);
        }

        var days = dayService.getAllDays();
        var shifts = shiftService.getAll();
        logger.info(days.toString());

        List<BreadCrumb> breadCrumbs = adminService.getSubjectBreadCrumbs(subjectId);

        model.addAttribute("breadCrumbs", breadCrumbs);
        model.addAttribute("subjectId", subjectId);
        model.addAttribute("semester", semester);
        model.addAttribute("days", days);
        model.addAttribute("shifts", shifts);

        return redirect != null ? redirect : "pages/admin/subject/addLessons";
    }


    @GetMapping("/department")
    public String department(
            HttpServletRequest request
    ) {
        String redirect = adminService.requireAdminLogin(request);

        return redirect != null ? redirect : "pages/admin/department";
    }

    @GetMapping("/department/{departmentId}")
    public String departmentDetails(
            @PathVariable String departmentId,
            HttpServletRequest request,
            Model model
    ) {
        List<BreadCrumb> breadCrumbs = adminService.getDepartmentBreadCrumbs(departmentId);
        model.addAttribute("breadCrumbs", breadCrumbs);
        model.addAttribute("departmentId", departmentId);

        String redirect = adminService.requireAdminLogin(request);
        return redirect != null ? redirect : "pages/admin/departmentDetails";
    }

    @GetMapping("/range-scheduling")
    public String rangeScheduling(
            HttpServletRequest request,
            HttpServletResponse response,
            Model model
    ) {
        String redirect = adminService.requireAdminLogin(request);

        return redirect != null ? redirect : "pages/admin/rangeScheduling";
    }

    @GetMapping("/level")
    public String levelShow(
            HttpServletRequest request,
            HttpServletResponse response,
            Model model
    ) {
        String redirect = adminService.requireAdminLogin(request);
        List<LevelResponse> levels = levelService.findAll();
        model.addAttribute("levels", levels);
        return redirect != null ? redirect : "pages/admin/level";
    }

    @GetMapping("/level/{levelId}")
    public String levelDetail(
            @PathVariable String levelId,
            HttpServletRequest request,
            Model model
    ) {
        String redirect = adminService.requireAdminLogin(request);
        List<BreadCrumb> breadCrumbs = adminService.getLevelBreadCrumbs(levelId);
        List<LevelResponse> levels = levelService.findAll();
        model.addAttribute("breadCrumbs", breadCrumbs);
        model.addAttribute("levelId", levelId);
        model.addAttribute("levels", levels);
        return redirect != null ? redirect : "pages/admin/levelDetails";
    }

    @GetMapping("/semester")
    public String showSemester(
            HttpServletRequest request,
            HttpServletResponse response,
            Model model
    ) {
        String redirect = adminService.requireAdminLogin(request);
        List<BreadCrumb> breadCrumbs = adminService.getSemesterBreadCrumbs("");
        model.addAttribute("breadCrumbs", breadCrumbs);
        return redirect != null ? redirect : "pages/admin/semester";
    }

    @GetMapping("/semester/create")
    public String createSemester(
            HttpServletRequest request,
            HttpServletResponse response,
            Model model
    ) {
        String redirect = adminService.requireAdminLogin(request);

        return redirect != null ? redirect : "pages/admin/semester/createSemester";
    }
}
