package com.highschool.highschoolsystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.util.Collection;

@Entity
@Table(name = "class")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class ClassEntity extends BaseEntity<String> implements Serializable {
    private String name;

    private int present;

    @ManyToOne(targetEntity = SemesterEntity.class)
    @JoinColumn(name = "semester_id")
    private SemesterEntity semester;

    @ManyToOne(targetEntity = LevelEntity.class)
    @JoinColumn(name = "level_id")
    private LevelEntity level;

    @OneToOne(targetEntity = TeacherEntity.class)
    @JoinColumn(name = "chairman_id")
    private TeacherEntity chairman;

    @OneToMany(
            mappedBy = "classEntity",
            targetEntity = StudentEntity.class
    )
    private Collection<StudentEntity> students;

    @OneToMany(
            mappedBy = "classEntity",
            targetEntity = AttendanceEntity.class
    )
    private Collection<AttendanceEntity> attendances;

    @OneToMany(
            mappedBy = "classEntity",
            targetEntity = FaultEntity.class
    )
    private Collection<FaultEntity> faults;

    @OneToMany(
            mappedBy = "classEntity",
            targetEntity = AssignmentEntity.class
    )
    private Collection<AssignmentEntity> assignments;

    @OneToOne(
            mappedBy = "classEntity",
            targetEntity = ScheduleEntity.class
    )
    private ScheduleEntity schedule;
}
