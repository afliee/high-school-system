package com.highschool.highschoolsystem.entity;

import com.highschool.highschoolsystem.config.FaultType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "fault_detail")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@EqualsAndHashCode(callSuper = false)
public class FaultDetailEntity extends BaseEntity<String> {
    private String faultDescription;
    @Enumerated(EnumType.STRING)
    private FaultType faultType;

    @ManyToOne(targetEntity = FaultEntity.class)
    @JoinColumn(name = "fault_id")
    private FaultEntity fault;
}
