package com.highschool.highschoolsystem.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "submitting")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = {"assignment"})
public class Submitting extends BaseEntity<String> {
    @ManyToOne(targetEntity = AssignmentEntity.class)
    @JoinColumn(name = "assignment_id")
    private AssignmentEntity assignment;

    @ManyToOne(targetEntity = StudentEntity.class)
    @JoinColumn(name = "student_id")
    private StudentEntity student;

    private String file;
    private double score;
    private String comment;
    private boolean isTurnedLate;
}
