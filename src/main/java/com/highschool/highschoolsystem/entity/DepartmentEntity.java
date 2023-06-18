package com.highschool.highschoolsystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Collection;

@Entity
@Table(name = "department")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class DepartmentEntity extends BaseEntity<String> {
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String foundedDate;

    @OneToMany(
            mappedBy = "department",
            targetEntity = TeacherEntity.class
    )
    private Collection<TeacherEntity> teachers;
}
