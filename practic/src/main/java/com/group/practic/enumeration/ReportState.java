package com.group.practic.enumeration;

import java.util.Arrays;
import java.util.List;
import org.springframework.core.annotation.Order;


public enum ReportState {
    @Order(0)
    CANCELLED(),
    @Order(1)
    APPROVED,
    @Order(2)
    FINISHED(APPROVED, CANCELLED),
    @Order(3)
    STARTED(FINISHED, CANCELLED),
    @Order(4)
    ANNOUNCED(STARTED, CANCELLED);


    protected final List<ReportState> allowed;


    ReportState(ReportState ... next) {
        allowed = Arrays.asList(next);
    }


    public boolean changeAllowed(ReportState newState) {
        return this == newState || allowed.contains(newState);
    }

    public static ReportState fromString(String value) {
        for (ReportState state : values()) {
            if (state.name().equalsIgnoreCase(value)) {
                return state;
            }
        }
        throw new IllegalArgumentException("Unknown ReportState: " + value);
    }

}
