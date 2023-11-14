package com.group.practic.enumeration;


public interface StateCountable<T> {

    boolean isStartCountingState();

    boolean isStopCountingState();
    
    boolean changeAllowed(T newState);

}
