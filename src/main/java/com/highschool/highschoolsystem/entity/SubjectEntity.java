package com.highschool.highschoolsystem.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "subject")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@EqualsAndHashCode(callSuper = false, exclude = {"level", "schedule", "teacher"})
public class SubjectEntity extends BaseEntity<String> {
    private String name;
    private String description;
    private String color;
    private String image;

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

    @ManyToOne(
            targetEntity = ScheduleEntity.class
    )
    @JoinColumn(name = "schedule_id")
    private ScheduleEntity schedule;

    @OneToMany(
            mappedBy = "subject",
            targetEntity = AssignmentEntity.class
    )
    private Set<AssignmentEntity> assignments = new HashSet<>();

//    @Override
//    public boolean equals(Object obj) {
//        if (obj instanceof SubjectEntity) {
//            SubjectEntity subjectEntity = (SubjectEntity) obj;
//            return subjectEntity.getId().equals(this.getId());
//        }
//        return false;
//    }
}
