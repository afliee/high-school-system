package com.highschool.highschoolsystem.config;

import com.highschool.highschoolsystem.entity.*;
import com.highschool.highschoolsystem.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


@Component
public class Initialize implements ApplicationListener<ApplicationReadyEvent> {
    @Autowired
    private DayRepository dayRepository;
    @Autowired
    private LevelRepository levelRepository;
    @Autowired
    private SemesterRepository semesterRepository;
    @Autowired
    private WeekRepository weekRepository;
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private ShiftRepository shiftRepository;

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
//        addDays();
//        var semesters = addSemesters();
////        var weeks = addWeeks(semesters);
//        addLevels();
//        addDepartments();
//        addShifts();
    }

    private void addShifts() {
        List<ShiftEntity> shifts = new ArrayList<>();

        shifts.add(
                ShiftEntity.builder()
                        .name("Shift 1")
                        .startTime("07:00")
                        .endTime("07:45")
                        .build()
        );

        shifts.add(
                ShiftEntity.builder()
                        .name("Shift 2")
                        .startTime("07:50")
                        .endTime("08:35")
                        .build()
        );

        shifts.add(
                ShiftEntity.builder()
                        .name("Shift 3")
                        .startTime("08:40")
                        .endTime("09:25")
                        .build()
        );

        shifts.add(
                ShiftEntity.builder()
                        .name("Shift 4")
                        .startTime("09:50")
                        .endTime("10:35")
                        .build()
        );

        shifts.add(
                ShiftEntity.builder()
                        .name("Shift 5")
                        .startTime("10:40")
                        .endTime("11:25")
                        .build()
        );

        shiftRepository.saveAll(shifts);
    }

    private void addDepartments() {
        List<DepartmentEntity> departments = new ArrayList<>();

        departments.add(
                DepartmentEntity.builder()
                        .name("Computer Science")
                        .description("Computer Science Department")
                        .foundedDate("2021-09-01")
                        .build()
        );

        departments.add(
                DepartmentEntity.builder()
                        .name("Mathematics")
                        .description("Mathematics Department")
                        .foundedDate("2021-09-01")
                        .build()
        );

        departments.add(
                DepartmentEntity.builder()
                        .name("Physics")
                        .description("Physics Department")
                        .foundedDate("2021-09-01")
                        .build()
        );

        departments.add(
                DepartmentEntity.builder()
                        .name("Chemistry")
                        .description("Chemistry Department")
                        .foundedDate("2021-09-01")
                        .build()
        );

        departmentRepository.saveAll(departments);
    }

    private List<WeekEntity> addWeeks(List<SemesterEntity> semesters) {
        List<WeekEntity> weeks = new ArrayList<>();

        weeks.add(
                WeekEntity.builder()
                        .name("First Week")
                        .startDate("2021-09-01")
                        .endDate("2021-09-07")
                        .weekIndex(1)
                        .semester(semesters.get(0))
                        .build()
        );
        weeks.add(
                WeekEntity.builder()
                        .name("Second Week")
                        .startDate("2021-09-08")
                        .endDate("2021-09-14")
                        .weekIndex(2)
                        .semester(semesters.get(0))
                        .build()
        );
        weeks.add(
                WeekEntity.builder()
                        .name("Third Week")
                        .startDate("2021-09-15")
                        .endDate("2021-09-21")
                        .weekIndex(3)
                        .semester(semesters.get(1))
                        .build()
        );
        weeks.add(
                WeekEntity.builder()
                        .name("Fourth Week")
                        .startDate("2021-09-22")
                        .endDate("2021-09-28")
                        .weekIndex(4)
                        .build()
        );
        weekRepository.saveAll(weeks);

        return weeks;
    }

    private List<SemesterEntity> addSemesters() {
        List<SemesterEntity> semesters = new ArrayList<>();
        semesters.add(
                SemesterEntity.builder()
                        .name("First Semester")
                        .startDate(LocalDate.parse("2023-07-04", formatter))
                        .endDate(LocalDate.parse("2023-10-01", formatter))
                        .build()
        );
        semesters.add(
                SemesterEntity.builder()
                        .name("Second Semester")
                        .startDate(LocalDate.parse("2023-10-02", formatter))
                        .endDate(LocalDate.parse("2023-12-01", formatter))
                        .build()
        );
        semesters.add(
                SemesterEntity.builder()
                        .name("Summer Semester")
                        .startDate(LocalDate.parse("2023-12-02", formatter))
                        .endDate(LocalDate.parse("2023-12-31", formatter))
                        .build()
        );
        semesterRepository.saveAll(semesters);
        return semesters;
    }

    private void addDays() {
        List<DayEntity> days = new ArrayList<>();
//        add seven day in week
        days.add(DayEntity.builder().dayName("Monday").build());
        days.add(DayEntity.builder().dayName("Tuesday").build());
        days.add(DayEntity.builder().dayName("Wednesday").build());
        days.add(DayEntity.builder().dayName("Thursday").build());
        days.add(DayEntity.builder().dayName("Friday").build());
        days.add(DayEntity.builder().dayName("Saturday").build());
        days.add(DayEntity.builder().dayName("Sunday").build());
        dayRepository.saveAll(days);
    }

    private void addLevels() {
        List<LevelEntity> levels = new ArrayList<>();
        levels.add(
                LevelEntity.builder()
                        .name("First")
                        .levelNumber(1)
                        .build()
        );
        levels.add(
                LevelEntity.builder()
                        .name("Second")
                        .levelNumber(2)
                        .build()
        );
        levels.add(
                LevelEntity.builder()
                        .name("Third")
                        .levelNumber(3)
                        .build()
        );
        levels.add(
                LevelEntity.builder()
                        .name("Fourth")
                        .levelNumber(4)
                        .build()
        );
        levelRepository.saveAll(levels);
    }
}
