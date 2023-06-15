package com.highschool.highschoolsystem.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Entity
@Table(name = "shift")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShiftEntity extends BaseEntity{
    private String name;
    private String startTime;
    private String endTime;

    @OneToMany(
            mappedBy = "shift",
            targetEntity = LessonEntity.class
    )
    private Collection<LessonEntity> lessons;
}
