package com.highschool.highschoolsystem.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Collection;

@Entity
@Table(name = "attendance")
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class AttendanceEntity extends BaseEntity<String> {
    @ManyToOne(targetEntity = NavigatorEntity.class)
    private NavigatorEntity navigator;

    @ManyToOne(targetEntity = ClassEntity.class)
    @JoinColumn(name = "class_id")
    private ClassEntity classEntity;

    private int present;

    @ManyToMany(
            targetEntity = StudentEntity.class,
            cascade = CascadeType.ALL
    )
    @JoinTable(
            name = "attendance_detail",
            joinColumns = @JoinColumn(name = "attendance_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    private Collection<StudentEntity> students;
}
