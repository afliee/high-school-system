package com.highschool.highschoolsystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Collection;

@Entity
@Table(name = "fault")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class FaultEntity extends BaseEntity<String> {
    private String createdAt;

    @ManyToOne(targetEntity = ClassEntity.class)
    @JoinColumn(name = "class_id")
    private ClassEntity classEntity;

    @ManyToMany(
            mappedBy = "faults",
            targetEntity = StudentEntity.class
    )
    private Collection<StudentEntity> students;
}
