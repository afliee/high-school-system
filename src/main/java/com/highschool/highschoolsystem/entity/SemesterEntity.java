package com.highschool.highschoolsystem.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Entity
@Table(name = "semester")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SemesterEntity extends BaseEntity {
    private String name;
    private String startDate;
    private String endDate;

    @OneToMany(
            mappedBy = "semester",
            targetEntity = WeekEntity.class
    )
    private Collection<WeekEntity> weeks;

    @OneToMany(
            mappedBy = "semester",
            targetEntity = ClassEntity.class
    )
    private Collection<ClassEntity> classes;

    @OneToMany(
            mappedBy = "semester",
            targetEntity = ScheduleEntity.class
    )
    private Collection<ScheduleEntity> schedules;
}
