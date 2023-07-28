package com.highschool.highschoolsystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Collection;

@Entity
@Table(name = "schedule")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class ScheduleEntity extends BaseEntity<String> {
    private boolean isExpired;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate expiredDate;

    @ManyToOne(targetEntity = SemesterEntity.class)
    @JoinColumn(name = "semester_id")
    private SemesterEntity semester;

    @ManyToMany(
            mappedBy = "schedules",
            targetEntity = LessonEntity.class
    )
    private Collection<LessonEntity> lessons;

    @OneToOne(targetEntity = ClassEntity.class)
    @JoinColumn(name = "class_id")
    private ClassEntity classEntity;
}
