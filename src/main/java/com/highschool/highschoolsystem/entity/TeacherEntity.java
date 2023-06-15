package com.highschool.highschoolsystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Entity
@Table(name = "teacher")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeacherEntity extends BaseEntity {
    private String name;
    private String email;
    private String cardId;
    private boolean gender;
    private String phone;
    private String address;
    private String birthday;
    private Long salary;
    private String enteredDate;
    private String closedDate;

    //   reference to department
    @ManyToOne(targetEntity = DepartmentEntity.class)
    @JoinColumn(name = "department_id")
    private DepartmentEntity department;

    @OneToMany(
            mappedBy = "teacher",
            targetEntity = RatationalEntity.class
    )
    private Collection<RatationalEntity> ratationals;

    @OneToMany(
            mappedBy = "teacher",
            targetEntity = SubjectEntity.class
    )
    private Collection<SubjectEntity> subjects;

    @OneToMany(
            mappedBy = "teacher",
            targetEntity = AssignmentEntity.class
    )

    private Collection<AssignmentEntity> assignments;
}
