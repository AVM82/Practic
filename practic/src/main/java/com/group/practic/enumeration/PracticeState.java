package com.group.practic.enumeration;

import java.util.Set;

public enum PracticeState {
    NOT_STARTED(),

    IN_PROCESS(NOT_STARTED),

    PAUSE(IN_PROCESS),

    READY_TO_REVIEW(IN_PROCESS),

    APPROVED(READY_TO_REVIEW);

    private final Set<PracticeState> allowed;

    PracticeState(PracticeState ... next) {
        allowed = Set.of(next);
    }

    public boolean changeAllowed(PracticeState newState) {
        return this == newState || allowed.contains(newState);
    }

    public static PracticeState fromString(String value) {
        for (PracticeState state : values()) {
            if (state.name().equalsIgnoreCase(value)) {
                return state;
            }
        }
        throw new IllegalArgumentException("Unknown PracticeState: " + value);
    }
}