package com.group.practic.enumeration;

import java.util.Set;

public enum ChapterState {

    NOT_STARTED(false),

    IN_PROCESS(false, NOT_STARTED),

    PAUSE(true, IN_PROCESS),

    DONE(false, IN_PROCESS);


    private final Set<ChapterState> allowed;

    private final boolean backward;


    ChapterState(boolean backward, ChapterState... allowFrom) {
        this.backward = backward;
        this.allowed = Set.of(allowFrom);
    }


    public boolean changeAllowed(ChapterState newState) {
        return this == newState || newState.allowed.contains(this)
                || (this.backward && allowed.contains(newState));
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
