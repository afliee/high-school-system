package com.highschool.highschoolsystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Collection;

@Entity
@Table(name = "lession")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class LessonEntity extends BaseEntity<String> {
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

    @ManyToMany(targetEntity = ScheduleEntity.class)
    @JoinTable(
            name = "schedule_detail",
            joinColumns = @JoinColumn(name = "lession_id"),
            inverseJoinColumns = @JoinColumn(name = "schedule_id")
    )
    private Collection<ScheduleEntity> schedules;
}
