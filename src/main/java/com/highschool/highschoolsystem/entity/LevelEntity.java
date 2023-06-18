package com.highschool.highschoolsystem.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Collection;

@Entity
@Table(name = "level")
@Data
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
    private Collection<ClassEntity> classes;
}
