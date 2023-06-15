package com.highschool.highschoolsystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Entity
@Table(name = "fault")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FaultEntity extends BaseEntity {
    private String createdAt;

    @ManyToOne(targetEntity = ClassEntity.class)
    @JoinColumn(name = "class_id")
    private ClassEntity classEntity;

    @ManyToMany(
            mappedBy = "faults",
            targetEntity = StudentEntity.class
    )
    private Collection<StudentEntity> students;
}
