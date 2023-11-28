package com.group.practic.enumeration;

import java.util.Set;


public enum PracticeState implements StateCountable<PracticeState> {

    NOT_STARTED(false),

    IN_PROCESS(false, NOT_STARTED),

    PAUSE(true, IN_PROCESS),

    READY_TO_REVIEW(true, IN_PROCESS),

    APPROVED(true, READY_TO_REVIEW);


    private final Set<PracticeState> allowed;

    private final boolean backward;


    PracticeState(boolean backward, PracticeState... allowFrom) {
        this.backward = backward;
        allowed = Set.of(allowFrom);
    }


    public boolean changeAllowed(PracticeState newState) {
        return newState.allowed.contains(this) || (this.backward && allowed.contains(newState));
    }


    @Override
    public boolean isStartCountingState() {
        return this == IN_PROCESS;
    }


    @Override
    public boolean isStopCountingState() {
        return this == READY_TO_REVIEW || this == PAUSE || this == APPROVED;
    }


    public static PracticeState fromString(String value) {
        for (PracticeState state : values()) {
            if (state.name().equalsIgnoreCase(value)) {
                return state;
            }
        }
        throw new IllegalArgumentException("Unknown PracticeState: " + value);
    }


    @Override
    public boolean isPauseState() {
        return this == READY_TO_REVIEW || this == PAUSE;
    }

}
