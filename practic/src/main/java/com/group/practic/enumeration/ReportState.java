package com.group.practic.enumeration;

import java.util.Arrays;
import java.util.List;


public enum ReportState {

    CANCELLED(),

    FINISHED(),

    STARTED(FINISHED, CANCELLED),

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
