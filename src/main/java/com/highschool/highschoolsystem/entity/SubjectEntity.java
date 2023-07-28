package com.highschool.highschoolsystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Collection;

@Entity
@Table(name = "subject")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class SubjectEntity extends BaseEntity<String> {
    private String name;
    private String description;
    private String color;

    @ManyToOne(targetEntity = TeacherEntity.class)
    @JoinColumn(name = "teached_by")
    private TeacherEntity teacher;

    @OneToMany(
            mappedBy = "subject",
            targetEntity = LessonEntity.class
    )
    private Collection<LessonEntity> lessons;

    @ManyToOne(targetEntity = DepartmentEntity.class)
    @JoinColumn(name = "department_id")
    private DepartmentEntity department;

    @ManyToOne(
            targetEntity = LevelEntity.class
    )
    @JoinColumn(name = "level_id")
    private LevelEntity level;
}
