package com.highschool.highschoolsystem.config;

import lombok.Getter;

@Getter
public enum RegisterStatus {
    SUBMITTED(0),
    APPROVED(1),
    REJECTED(2),
    CANCELLED(3),
    DELETED(4),
    EXPIRED(5),
    HANDLED(6);

    private final int value;
    public static RegisterStatus get(int value) {
        for (RegisterStatus registerStatus : RegisterStatus.values()) {
            if (registerStatus.getValue() == value) {
                return registerStatus;
            }
        }
        return null;
    }

    RegisterStatus(int value) {
        this.value = value;
    }
}
