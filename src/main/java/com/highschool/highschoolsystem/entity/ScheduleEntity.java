package com.highschool.highschoolsystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Collection;

@Entity
@Table(name = "schedule")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class ScheduleEntity extends BaseEntity<String> {
    private boolean isExpired;
    private String expiredDate;

    @ManyToOne(targetEntity = SemesterEntity.class)
    @JoinColumn(name = "semester_id")
    private SemesterEntity semester;

    @ManyToMany(
            mappedBy = "schedules",
            targetEntity = LessonEntity.class
    )
    private Collection<LessonEntity> lessons;
}
