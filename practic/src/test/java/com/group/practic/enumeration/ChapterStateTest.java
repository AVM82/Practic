package com.group.practic.enumeration;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;


class ChapterStateTest {

    @Test
    void testNotStarted() {
        assertTrue(ChapterState.NOT_STARTED.changeAllowed(ChapterState.NOT_STARTED));
        assertTrue(ChapterState.NOT_STARTED.changeAllowed(ChapterState.IN_PROCESS));

        assertFalse(ChapterState.NOT_STARTED.changeAllowed(ChapterState.PAUSE));
        assertFalse(ChapterState.NOT_STARTED.changeAllowed(ChapterState.DONE));
    }
    
    
    @Test
    void testInProcess() {
        assertTrue(ChapterState.IN_PROCESS.changeAllowed(ChapterState.IN_PROCESS));
        assertTrue(ChapterState.IN_PROCESS.changeAllowed(ChapterState.PAUSE));
        assertTrue(ChapterState.IN_PROCESS.changeAllowed(ChapterState.DONE));

        assertFalse(ChapterState.IN_PROCESS.changeAllowed(ChapterState.NOT_STARTED));
    }


    @Test
    void testPause() {
        assertTrue(ChapterState.PAUSE.changeAllowed(ChapterState.PAUSE));
        assertTrue(ChapterState.PAUSE.changeAllowed(ChapterState.IN_PROCESS));

        assertFalse(ChapterState.PAUSE.changeAllowed(ChapterState.NOT_STARTED));
        assertFalse(ChapterState.PAUSE.changeAllowed(ChapterState.DONE));
    }
    

    @Test
    void testDone() {
        assertTrue(ChapterState.DONE.changeAllowed(ChapterState.DONE));

        assertFalse(ChapterState.DONE.changeAllowed(ChapterState.NOT_STARTED));
        assertFalse(ChapterState.DONE.changeAllowed(ChapterState.IN_PROCESS));
        assertFalse(ChapterState.DONE.changeAllowed(ChapterState.PAUSE));
    }
    
}
