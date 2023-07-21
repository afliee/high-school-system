package com.highschool.highschoolsystem.config;

import com.highschool.highschoolsystem.entity.*;
import com.highschool.highschoolsystem.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cglib.core.Local;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
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
                        .startTime(LocalTime.parse("07:00"))
                        .endTime(LocalTime.parse("07:45"))
                        .build()
        );

        shifts.add(
                ShiftEntity.builder()
                        .name("Shift 2")
                        .startTime(LocalTime.parse("07:50"))
                        .endTime(LocalTime.parse("08:35"))
                        .build()
        );

        shifts.add(
                ShiftEntity.builder()
                        .name("Shift 3")
                        .startTime(LocalTime.parse("08:40"))
                        .endTime(LocalTime.parse("09:25"))
                        .build()
        );

        shifts.add(
                ShiftEntity.builder()
                        .name("Shift 4")
                        .startTime(LocalTime.parse("09:30"))
                        .endTime(LocalTime.parse("10:15"))
                        .build()
        );

        shifts.add(
                ShiftEntity.builder()
                        .name("Shift 5")
                        .startTime(LocalTime.parse("10:20"))
                        .endTime(LocalTime.parse("11:05"))
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
                        .foundedDate(LocalDate.parse("2021-09-01", formatter))
                        .build()
        );

        departments.add(
                DepartmentEntity.builder()
                        .name("Mathematics")
                        .description("Mathematics Department")
                        .foundedDate(LocalDate.parse("2021-09-01", formatter))
                        .build()
        );

        departments.add(
                DepartmentEntity.builder()
                        .name("Physics")
                        .description("Physics Department")
                        .foundedDate(LocalDate.parse("2021-09-01", formatter))
                        .build()
        );

        departments.add(
                DepartmentEntity.builder()
                        .name("Chemistry")
                        .description("Chemistry Department")
                        .foundedDate(LocalDate.parse("2021-09-01", formatter))
                        .build()
        );

        departmentRepository.saveAll(departments);
    }

    private List<WeekEntity> addWeeks(List<SemesterEntity> semesters) {
        List<WeekEntity> weeks = new ArrayList<>();

        weeks.add(
                WeekEntity.builder()
                        .name("First Week")
                        .startDate(LocalDate.parse("2021-09-01", formatter))
                        .endDate(LocalDate.parse("2021-09-07", formatter))
                        .weekIndex(1)
                        .semester(semesters.get(0))
                        .build()
        );
        weeks.add(
                WeekEntity.builder()
                        .name("Second Week")
                        .startDate(LocalDate.parse("2021-09-08", formatter))
                        .endDate(LocalDate.parse("2021-09-14", formatter))
                        .weekIndex(2)
                        .semester(semesters.get(0))
                        .build()
        );
        weeks.add(
                WeekEntity.builder()
                        .name("Third Week")
                        .startDate(LocalDate.parse("2021-09-15", formatter))
                        .endDate(LocalDate.parse("2021-09-21", formatter))
                        .weekIndex(3)
                        .semester(semesters.get(1))
                        .build()
        );
        weeks.add(
                WeekEntity.builder()
                        .name("Fourth Week")
                        .startDate(LocalDate.parse("2021-09-22", formatter))
                        .endDate(LocalDate.parse("2021-09-28", formatter))
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
        days.add(DayEntity.builder().name("Monday").build());
        days.add(DayEntity.builder().name("Tuesday").build());
        days.add(DayEntity.builder().name("Wednesday").build());
        days.add(DayEntity.builder().name("Thursday").build());
        days.add(DayEntity.builder().name("Friday").build());
        days.add(DayEntity.builder().name("Saturday").build());
        days.add(DayEntity.builder().name("Sunday").build());
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
