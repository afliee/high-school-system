package com.highschool.highschoolsystem.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "assignment")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssignmentEntity extends BaseEntity {
    private String title;
    private String description;
    private boolean isDue;
    private String startedDate;
    private String closedDate;

    @ManyToOne(targetEntity = TeacherEntity.class)
    @JoinColumn(name = "teacher_id")
    private TeacherEntity teacher;

    @ManyToOne(targetEntity = ClassEntity.class)
    @JoinColumn(name = "class_id")
    private ClassEntity classEntity;
}
