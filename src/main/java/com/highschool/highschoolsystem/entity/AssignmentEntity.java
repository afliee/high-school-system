package com.highschool.highschoolsystem.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "assignment")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@EqualsAndHashCode(callSuper = false, exclude = {"startedDate", "closedDate", "teacher", "classEntity", "submitting"})
public class AssignmentEntity extends BaseEntity<String> {
    private String title;
    private String description;
    private String attachment;
    private double points;
    private boolean isDue;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startedDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime closedDate;

    @ManyToOne(targetEntity = TeacherEntity.class)
    @JoinColumn(name = "teacher_id")
    private TeacherEntity teacher;

//    @ManyToOne(targetEntity = ClassEntity.class)
//    @JoinColumn(name = "class_id")
//    private ClassEntity classEntity;
    @ManyToOne(targetEntity = SubjectEntity.class)
    @JoinColumn(name = "subject_id")
    private SubjectEntity subject;

    @OneToMany(mappedBy = "assignment", targetEntity = Submitting.class)
    private Set<Submitting> submitting = new HashSet<>();
}
