package com.group.practic.enumeration;

import java.util.Set;

public enum ChapterState {
    NOT_STARTED(),

    IN_PROCESS(NOT_STARTED),

    PAUSE(IN_PROCESS),

    DONE();

    private final Set<ChapterState> allowed;

    ChapterState(ChapterState ... next) {
        allowed = Set.of(next);
    }

    public boolean changeAllowed(ChapterState newState) {
        return this == newState || allowed.contains(newState);
    }


    public static ChapterState fromString(String value) {
        for (ChapterState state : values()) {
            if (state.name().equalsIgnoreCase(value)) {
                return state;
            }
        }
        throw new IllegalArgumentException("Unknown ChapterState: " + value);
    }
}
