package com.highschool.highschoolsystem.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;

@Entity
@Table(name = "lession")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@EqualsAndHashCode(exclude = {"startDate", "endDate", "day", "subject", "shift", "week", "schedules"}, callSuper = true)
public class LessonEntity extends BaseEntity<String> implements Serializable {
    @ManyToOne(targetEntity = DayEntity.class)
    @JoinColumn(name = "day_id")
    private DayEntity day;

    @ManyToOne(targetEntity = SubjectEntity.class)
    @JoinColumn(name = "subject_id")
    private SubjectEntity subject;

    @ManyToOne(targetEntity = ShiftEntity.class)
    @JoinColumn(name = "shift_id")
    private ShiftEntity shift;

    @ManyToOne(targetEntity = WeekEntity.class)
    @JoinColumn(name = "week_id")
    private WeekEntity week;

    @ManyToMany(
            mappedBy = "lessons",
            targetEntity = ScheduleEntity.class
    )
    private Set<ScheduleEntity> schedules;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endDate;
    private boolean isAbsent;
}
