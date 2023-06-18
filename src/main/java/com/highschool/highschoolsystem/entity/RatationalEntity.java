package com.highschool.highschoolsystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "ratational")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class RatationalEntity extends BaseEntity<String> {
    private String content;

    @ManyToOne(targetEntity = TeacherEntity.class)
    @JoinColumn(name = "teacher_id")
    private TeacherEntity teacher;
}
