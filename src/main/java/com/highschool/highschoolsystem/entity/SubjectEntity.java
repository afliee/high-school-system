package com.highschool.highschoolsystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Entity
@Table(name = "subject")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubjectEntity extends BaseEntity {
    private String name;
    private String description;

    @ManyToOne(targetEntity = TeacherEntity.class)
    @JoinColumn(name = "teached_by")
    private TeacherEntity teacher;

    @OneToMany(
            mappedBy = "subject",
            targetEntity = LessonEntity.class
    )
    private Collection<LessonEntity> lessons;
}
