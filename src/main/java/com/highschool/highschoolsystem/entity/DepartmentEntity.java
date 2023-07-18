package com.highschool.highschoolsystem.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Collection;

@Entity
@Table(name = "department")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class DepartmentEntity extends BaseEntity<String> implements Serializable {
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate foundedDate;

    @OneToMany(
            mappedBy = "department",
            targetEntity = TeacherEntity.class
    )
    private Collection<TeacherEntity> teachers;

    @OneToMany(
            mappedBy = "department",
            targetEntity = SubjectEntity.class
    )
    private Collection<SubjectEntity> subjects;
}
