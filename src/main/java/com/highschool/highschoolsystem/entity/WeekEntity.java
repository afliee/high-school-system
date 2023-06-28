package com.highschool.highschoolsystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Collection;

@Entity
@Table(name = "week")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class WeekEntity extends BaseEntity<String> {
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
