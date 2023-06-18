package com.highschool.highschoolsystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@MappedSuperclass
@Data
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity<T> {
//    generate id with uuid
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(
            name = "id",
            columnDefinition = "VARCHAR(255)"
    )
    private String id;
    @Column
    @CreatedDate
    private Date createdDate;
    @Column
    @LastModifiedDate
    private Date updatedDate;
    @Column
    @CreatedBy
    private T createdBy;
    @Column
    @LastModifiedBy
    private T updatedBy;

}
