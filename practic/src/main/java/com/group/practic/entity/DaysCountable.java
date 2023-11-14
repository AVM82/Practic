package com.group.practic.entity;

import com.group.practic.enumeration.StateCountable;
import java.time.LocalDate;


public interface DaysCountable<T extends Enum<?> & StateCountable<T>> {

    T getState();
    
    void setState(T newState);
    
    int getDaysSpent();
    
    void setDaysSpent(int days);
    
    LocalDate getStartCounting();
    
    void setStartCounting(LocalDate start);

}
