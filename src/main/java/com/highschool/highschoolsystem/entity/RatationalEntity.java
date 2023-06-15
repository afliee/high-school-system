package com.highschool.highschoolsystem.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ratational")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RatationalEntity extends BaseEntity {
    private String content;

    @ManyToOne(targetEntity = TeacherEntity.class)
    @JoinColumn(name = "teacher_id")
    private TeacherEntity teacher;
}
