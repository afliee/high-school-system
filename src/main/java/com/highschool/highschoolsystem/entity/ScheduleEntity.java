package com.highschool.highschoolsystem.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "schedule")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@EqualsAndHashCode(exclude = {"expiredDate", "semester", "lessons", "classEntity"}, callSuper = false)
public class ScheduleEntity extends BaseEntity<String> {
    private boolean isExpired;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate expiredDate;

    @ManyToOne(targetEntity = SemesterEntity.class)
    @JoinColumn(name = "semester_id")
    private SemesterEntity semester;

    @ManyToMany(
            targetEntity = LessonEntity.class
    )
    @JoinTable(
            name = "schedule_detail",
            joinColumns = @JoinColumn(name = "lession_id"),
            inverseJoinColumns = @JoinColumn(name = "schedule_id")
    )
    private Set<LessonEntity> lessons = new HashSet<>();

    @OneToOne(targetEntity = ClassEntity.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "class_id")
    private ClassEntity classEntity;

    @OneToMany(
            mappedBy = "schedule",
            targetEntity = SubjectEntity.class
    )
    private Set<SubjectEntity> subjects;
}
