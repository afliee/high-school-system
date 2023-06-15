package com.highschool.highschoolsystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Entity
@Table(name = "navigator")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NavigatorEntity extends BaseEntity {
    private String createdAt;
    private String closedAt;
    private String position;

    @OneToOne(targetEntity = StudentEntity.class)
    @JoinColumn(name = "student_id")
    private StudentEntity student;

    @OneToMany(
            mappedBy = "navigator",
            targetEntity = AttendanceEntity.class)
    private Collection<AttendanceEntity> attendances;
}
