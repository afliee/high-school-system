package com.highschool.highschoolsystem.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;

@Entity
@Table(name = "semester")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class SemesterEntity extends BaseEntity<String> {
    private String name;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate endDate;

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
