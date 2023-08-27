package com.highschool.highschoolsystem.entity;

import com.highschool.highschoolsystem.config.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Collection;

@Entity
@Table(name = "teacher")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Builder
@EqualsAndHashCode(callSuper = true)
public class TeacherEntity extends BaseEntity<String> {
    @Column(unique = true)
    private String name;
    private String fullName;
    private String password;
    private String avatar;
    private String email;
    private String cardId;
    private boolean gender;
    private String phone;
    private String address;
    private String birthday;
    private Long salary;
    private String enteredDate;
    private String closedDate;
    private Role role;

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
