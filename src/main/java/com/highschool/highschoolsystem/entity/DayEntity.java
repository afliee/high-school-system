package com.highschool.highschoolsystem.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Collection;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "day")
@Data
public class DayEntity extends BaseEntity {
    private String dayName;

    @OneToMany(
            mappedBy = "day",
            targetEntity = LessonEntity.class
    )
    private Collection<LessonEntity> lessons;
}
