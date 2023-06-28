package com.highschool.highschoolsystem.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Collection;

@Entity
@Table(name = "shift")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class ShiftEntity extends BaseEntity<String> {
    private String name;
    private String startTime;
    private String endTime;

    @OneToMany(
            mappedBy = "shift",
            targetEntity = LessonEntity.class
    )
    private Collection<LessonEntity> lessons;
}
