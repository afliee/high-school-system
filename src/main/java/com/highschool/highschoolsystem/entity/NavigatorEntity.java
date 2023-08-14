package com.highschool.highschoolsystem.entity;

import com.highschool.highschoolsystem.config.RegisterStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Collection;

@EqualsAndHashCode(callSuper = true, exclude = {"attendances", "student", "createdAt", "closedAt", "position"})
@Entity
@Table(name = "navigator")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class NavigatorEntity extends BaseEntity<String> {
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate createdAt;
    private String closedAt;
    private String position;
    private RegisterStatus status;

    @OneToOne(targetEntity = StudentEntity.class)
    @JoinColumn(name = "student_id")
    private StudentEntity student;

    @OneToMany(
            mappedBy = "navigator",
            targetEntity = AttendanceEntity.class)
    private Collection<AttendanceEntity> attendances;
}
