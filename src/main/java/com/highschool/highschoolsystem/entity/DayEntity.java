package com.highschool.highschoolsystem.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Collection;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "day")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class DayEntity extends BaseEntity<String> {
    private String dayName;

    @OneToMany(
            mappedBy = "day",
            targetEntity = LessonEntity.class
    )
    private Collection<LessonEntity> lessons;
}
