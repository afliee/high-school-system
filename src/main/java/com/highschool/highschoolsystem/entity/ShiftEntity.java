package com.highschool.highschoolsystem.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;

@Entity
@Table(name = "shift")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@EqualsAndHashCode(exclude = {"startTime", "endTime"}, callSuper = false)
public class ShiftEntity extends BaseEntity<String> {
    @Column(unique = true)
    private String name;

    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime startTime;

    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime endTime;

    @OneToMany(
            mappedBy = "shift",
            targetEntity = LessonEntity.class
    )
    private Collection<LessonEntity> lessons;
}
