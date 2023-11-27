package com.group.practic.util;

import com.group.practic.entity.DaysCountable;
import com.group.practic.enumeration.StateCountable;
import java.time.LocalDate;


public interface TimeCalculator {

    public static <T extends Enum<?> & StateCountable<T>> boolean setNewState(
            DaysCountable<T> entity, T newState) {
        if (entity.getState().changeAllowed(newState)) {
            entity.setState(newState);
            if (newState.isStartCountingState()) {
                entity.setStartCounting(LocalDate.now());
            } else if (newState.isStopCountingState()) {
                entity.setDaysSpent(entity.getStartCounting()
                        .until(LocalDate.now().plusDays(newState.isPauseState() ? 0 : 1)).getDays()
                        + entity.getDaysSpent());
            }
            return true;
        }
        return false;
    }


    public static int daysToWeeksWithRounding(int days) {
        return days / 7 + (days % 7 > 3 ? 1 : 0);
    }

}
