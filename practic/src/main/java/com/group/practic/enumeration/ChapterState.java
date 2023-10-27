package com.group.practic.enumeration;

public enum ChapterState {
    NOT_STARTED,

    IN_PROCESS,

    PAUSE,

    DONE;


    public static ChapterState fromString(String value) {
        for (ChapterState state : values()) {
            if (state.name().equalsIgnoreCase(value)) {
                return state;
            }
        }
        throw new IllegalArgumentException("Unknown ChapterState: " + value);
    }

}
