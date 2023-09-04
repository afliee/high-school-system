package com.highschool.highschoolsystem.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "fault")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false, exclude = {"faultDetails", "student"})
@EntityListeners(AuditingEntityListener.class)
public class FaultEntity extends BaseEntity<String> {
    private String createdAt;

    @ManyToOne(targetEntity = StudentEntity.class)
    @JoinColumn(name = "student_id")
    private StudentEntity student;

    private String subject;

    @OneToMany(
            mappedBy = "fault",
            cascade = CascadeType.ALL,
            targetEntity = FaultDetailEntity.class
    )
    private Set<FaultDetailEntity> faultDetails = new HashSet<>();

}
