package com.highschool.highschoolsystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Entity
@Table(name = "attendance")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceEntity extends BaseEntity {
    @ManyToOne(targetEntity = NavigatorEntity.class)
    private NavigatorEntity navigator;

    @ManyToOne(targetEntity = ClassEntity.class)
    @JoinColumn(name = "class_id")
    private ClassEntity classEntity;

    @ManyToMany(
            targetEntity = StudentEntity.class,
            mappedBy = "attendances"
    )
    private Collection<StudentEntity> students;
}
