package com.group.practic.enumeration;


public interface StateCountable<T> {

    boolean isStartCountingState();

    boolean isStopCountingState();

    boolean isPauseState();

    boolean changeAllowed(T newState);

}
