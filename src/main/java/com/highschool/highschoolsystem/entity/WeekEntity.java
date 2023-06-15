package com.highschool.highschoolsystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Entity
@Table(name = "week")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeekEntity extends BaseEntity {
    private String name;
    private String startDate;
    private String endDate;
    private int weekIndex;

//   reference to semester
    @ManyToOne
    @JoinColumn(name = "semester_id")
    private SemesterEntity semester;

    @OneToMany(
            mappedBy = "week",
            targetEntity = LessonEntity.class
    )
    private Collection<LessonEntity> lessons;
}
