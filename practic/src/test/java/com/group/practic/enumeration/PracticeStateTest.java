package com.group.practic.enumeration;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;


class PracticeStateTest {

    @Test
    void testNotStarted() {
        assertTrue(PracticeState.NOT_STARTED.changeAllowed(PracticeState.IN_PROCESS));

        assertFalse(PracticeState.NOT_STARTED.changeAllowed(PracticeState.NOT_STARTED));
        assertFalse(PracticeState.NOT_STARTED.changeAllowed(PracticeState.PAUSE));
        assertFalse(PracticeState.NOT_STARTED.changeAllowed(PracticeState.READY_TO_REVIEW));
        assertFalse(PracticeState.NOT_STARTED.changeAllowed(PracticeState.APPROVED));
    }


    @Test
    void testInProcess() {
        assertTrue(PracticeState.IN_PROCESS.changeAllowed(PracticeState.PAUSE));
        assertTrue(PracticeState.IN_PROCESS.changeAllowed(PracticeState.READY_TO_REVIEW));

        assertFalse(PracticeState.IN_PROCESS.changeAllowed(PracticeState.IN_PROCESS));
        assertFalse(PracticeState.IN_PROCESS.changeAllowed(PracticeState.NOT_STARTED));
        assertFalse(PracticeState.IN_PROCESS.changeAllowed(PracticeState.APPROVED));
    }


    @Test
    void testPause() {
        assertTrue(PracticeState.PAUSE.changeAllowed(PracticeState.IN_PROCESS));

        assertFalse(PracticeState.PAUSE.changeAllowed(PracticeState.PAUSE));
        assertFalse(PracticeState.PAUSE.changeAllowed(PracticeState.NOT_STARTED));
        assertFalse(PracticeState.PAUSE.changeAllowed(PracticeState.READY_TO_REVIEW));
        assertFalse(PracticeState.PAUSE.changeAllowed(PracticeState.APPROVED));
    }


    @Test
    void testReadyToReview() {
        assertTrue(PracticeState.READY_TO_REVIEW.changeAllowed(PracticeState.IN_PROCESS));
        assertTrue(PracticeState.READY_TO_REVIEW.changeAllowed(PracticeState.APPROVED));

        assertFalse(PracticeState.READY_TO_REVIEW.changeAllowed(PracticeState.READY_TO_REVIEW));
        assertFalse(PracticeState.READY_TO_REVIEW.changeAllowed(PracticeState.NOT_STARTED));
        assertFalse(PracticeState.READY_TO_REVIEW.changeAllowed(PracticeState.PAUSE));
    }



    @Test
    void testDone() {
        assertTrue(PracticeState.APPROVED.changeAllowed(PracticeState.READY_TO_REVIEW));

        assertFalse(PracticeState.APPROVED.changeAllowed(PracticeState.APPROVED));
        assertFalse(PracticeState.APPROVED.changeAllowed(PracticeState.NOT_STARTED));
        assertFalse(PracticeState.APPROVED.changeAllowed(PracticeState.IN_PROCESS));
        assertFalse(PracticeState.APPROVED.changeAllowed(PracticeState.PAUSE));
    }

}
