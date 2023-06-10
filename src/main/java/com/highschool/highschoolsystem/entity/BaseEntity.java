package com.highschool.highschoolsystem.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@MappedSuperclass
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseEntity {

    @Id
    private String id;

    @Column
    private String createdDate;
    @Column
    private String updatedDate;
    @Column
    private String createdBy;
    @Column
    private String updatedBy;

}
