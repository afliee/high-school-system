package com.highschool.highschoolsystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Collection;

@Entity
@Table(name = "attendance")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class AttendanceEntity extends BaseEntity<String> {
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
