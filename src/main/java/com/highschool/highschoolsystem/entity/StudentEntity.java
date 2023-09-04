package com.highschool.highschoolsystem.entity;

import com.highschool.highschoolsystem.config.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "student")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@EqualsAndHashCode(callSuper = false, exclude = {"birthday", "classEntity", "attendances", "faults", "submittingSet"})
public class StudentEntity extends BaseEntity<String> implements Serializable {
    private String name;
    private String fullName;
    private String password;
    private String cardId;
    private String email;
    private String phone;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;
    private String avatar;
    private boolean gender;
    private String location;
    private String enteredDate;
    private String expiredDate;

    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToOne(targetEntity = ClassEntity.class)
    @JoinColumn(name = "class_id")
    private ClassEntity classEntity;

    @ManyToMany(targetEntity = AttendanceEntity.class, mappedBy = "students")
    private Collection<AttendanceEntity> attendances = new ArrayList<>();

    @OneToMany(mappedBy = "student", targetEntity = FaultEntity.class)
    private Set<FaultEntity> faults = new HashSet<>();

    @OneToMany(mappedBy = "student", targetEntity = Submitting.class)
    private Set<Submitting> submittingSet = new HashSet<>();
}
