package com.highschool.highschoolsystem.entity;

import com.highschool.highschoolsystem.config.SubmitStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity
@Table(name = "submitting")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = {"assignment"})
@EntityListeners(AuditingEntityListener.class)
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
    private SubmitStatus status;

    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime turnedAt;

}
