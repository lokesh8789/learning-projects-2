package com.r2dbc.demo.enums;

import java.util.Arrays;
import java.util.Optional;

public enum Status {
    INACTIVE(0), ACTIVE(1);
    private final int value;

    Status(int value) {
        this.value = value;
    }

    //    @JsonValue
    public int status() {
        return value;
    }

    public static Optional<Status> fromStatus(int status) {
        return Arrays.stream(Status.values())
                .filter(s -> s.value == status)
                .findFirst();
    }
}
