package com.highschool.highschoolsystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Entity
@Table(name = "student")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentEntity extends BaseEntity {
    private String name;
    private String cardId;
    private boolean gender;
    private String location;
    private String enteredDate;
    private String expiredDate;

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
