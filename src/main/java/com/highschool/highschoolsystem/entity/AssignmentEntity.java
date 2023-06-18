package com.highschool.highschoolsystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "assignment")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class AssignmentEntity extends BaseEntity<String> {
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
