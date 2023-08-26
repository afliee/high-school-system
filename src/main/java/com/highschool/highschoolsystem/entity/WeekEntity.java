package com.highschool.highschoolsystem.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Collection;

@Entity
@Table(name = "week")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@EqualsAndHashCode(exclude = {"startDate", "endDate"}, callSuper = false)
public class WeekEntity extends BaseEntity<String> {
    private String name;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    private int weekIndex;

//   reference to semester
    @ManyToOne
    @JoinColumn(name = "semester_id")
    private SemesterEntity semester;

    @OneToMany(
            mappedBy = "week",
            targetEntity = LessonEntity.class,
            cascade = CascadeType.ALL
    )
    private Collection<LessonEntity> lessons;
}
