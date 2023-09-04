package com.highschool.highschoolsystem.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name = "class")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@EqualsAndHashCode(callSuper = false, exclude = {"students", "attendances", "schedule", "semester", "level", "chairman"})
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
    private Collection<StudentEntity> students = new ArrayList<>();

    @OneToMany(
            mappedBy = "classEntity",
            targetEntity = AttendanceEntity.class
    )
    private Collection<AttendanceEntity> attendances = new ArrayList<>();

//    @OneToMany(
//            mappedBy = "classEntity",
//            targetEntity = FaultEntity.class
//    )
//    private Collection<FaultEntity> faults = new ArrayList<>();

//    @OneToMany(
//            mappedBy = "classEntity",
//            targetEntity = AssignmentEntity.class
//    )
//    private Collection<AssignmentEntity> assignments;

    @OneToOne(
            mappedBy = "classEntity",
            targetEntity = ScheduleEntity.class
    )
    private ScheduleEntity schedule;
}
