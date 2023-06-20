package com.highschool.highschoolsystem.entity;

import com.highschool.highschoolsystem.config.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Collection;


@Entity
@Table(name = "student")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class StudentEntity extends BaseEntity<String> {
    private String name;
    private String fullName;
    private String password;
    private String cardId;
    private boolean gender;
    private String location;
    private String enteredDate;
    private String expiredDate;
    private Role role;

    @ManyToOne(targetEntity = ClassEntity.class)
    @JoinColumn(name = "class_id")
    private ClassEntity classEntity;

    @ManyToMany(targetEntity = AttendanceEntity.class)
    @JoinTable(
            name = "attendance_detail",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "attendance_id")
    )
    private Collection<AttendanceEntity> attendances;

    @ManyToMany(targetEntity = FaultEntity.class)
    @JoinTable(
            name = "fault_detail",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "fault_id")
    )
    private Collection<FaultEntity> faults;
}
