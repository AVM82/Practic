package com.group.practic.enumeration;

import java.util.Arrays;
import java.util.List;


public enum ReportState {

    ANNOUNCED(false),

    STARTED(false, ANNOUNCED),

    FINISHED(true, STARTED),

    CANCELLED(true, ANNOUNCED, STARTED);


    public final boolean isFinal;

    protected final List<ReportState> allowed;


    ReportState(boolean isFinal, ReportState... prevs) {
        this.isFinal = isFinal;
        allowed = Arrays.asList(prevs);
    }


    public boolean changeAllowed(ReportState newState) {
        return this == newState || (!isFinal && newState.allowed.contains(this));
    }

}
