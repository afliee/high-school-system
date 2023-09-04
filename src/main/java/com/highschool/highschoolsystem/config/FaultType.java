package com.highschool.highschoolsystem.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum FaultType {
    LATE("Go to school late"),
    ABSENT("Absent"),
    NOISY("Noisy in class"),
    UNIFORM("Not wearing uniform"),
    NOT_DO_HOMEWORK("Not do homework"),
    NOT_COMPLETE_HOMEWORK("Not complete homework"),
    NOT_CLEAN_CLASS("Not clean class"),
    OTHER("Other");

    private String faultDescription;
}
