package com.highschool.highschoolsystem.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Collection;

@Entity
@Table(name = "semester")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@EqualsAndHashCode(exclude = {"weeks", "classes", "schedules"}, callSuper = false)
@ToString(exclude = {"weeks", "classes", "schedules"})
public class SemesterEntity extends BaseEntity<String> implements Serializable {
    private String name;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @OneToMany(
            mappedBy = "semester",
            targetEntity = WeekEntity.class,
            cascade = CascadeType.ALL
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
