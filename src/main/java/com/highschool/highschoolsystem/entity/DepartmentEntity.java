package com.highschool.highschoolsystem.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Entity
@Table(name = "department")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentEntity extends BaseEntity {
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
