package com.highschool.highschoolsystem.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Collection;

@Entity
@Table(name = "semester")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class SemesterEntity extends BaseEntity<String> {
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
