package com.highschool.highschoolsystem.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name = "level")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class LevelEntity extends BaseEntity<String> {
    private String name;
    private int levelNumber;

    @OneToMany(
            mappedBy = "level",
            targetEntity = ClassEntity.class
    )
    private Collection<ClassEntity> classes = new ArrayList<>();

    @OneToMany(
            mappedBy = "level",
            targetEntity = SubjectEntity.class
    )
    private Collection<SubjectEntity> subjects = new ArrayList<>();
}
