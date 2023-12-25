package com.group.practic.enumeration;

import java.util.Set;


public enum ChapterState implements StateCountable<ChapterState> {

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
        return newState.allowed.contains(this) || (this.backward && allowed.contains(newState));
    }


    public static ChapterState fromString(String value) {
        for (ChapterState state : values()) {
            if (state.name().equalsIgnoreCase(value)) {
                return state;
            }
        }
        throw new IllegalArgumentException("Unknown ChapterState: " + value);
    }


    @Override
    public boolean isStartCountingState() {
        return this == IN_PROCESS;
    }


    @Override
    public boolean isStopCountingState() {
        return this == DONE || this == PAUSE;
    }


    @Override
    public boolean isPauseState() {
        return this == PAUSE;
    }

}
