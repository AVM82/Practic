package com.group.practic.enumeration;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class ReportStateTest {

    @Test
    void testAnnounced() {
        assertTrue(ReportState.ANNOUNCED.changeAllowed(ReportState.STARTED));
        assertTrue(ReportState.ANNOUNCED.changeAllowed(ReportState.CANCELLED));
        
        assertFalse(ReportState.ANNOUNCED.changeAllowed(ReportState.ANNOUNCED));
        assertFalse(ReportState.ANNOUNCED.changeAllowed(ReportState.FINISHED));
        assertFalse(ReportState.ANNOUNCED.changeAllowed(ReportState.APPROVED));
    }
    
    
    @Test
    void testStarted() {
        assertTrue(ReportState.STARTED.changeAllowed(ReportState.FINISHED));
        assertTrue(ReportState.STARTED.changeAllowed(ReportState.CANCELLED));
        
        assertFalse(ReportState.STARTED.changeAllowed(ReportState.STARTED));
        assertFalse(ReportState.STARTED.changeAllowed(ReportState.APPROVED));
        assertFalse(ReportState.STARTED.changeAllowed(ReportState.ANNOUNCED));
    }
    
    
    @Test
    void testFinished() {
        assertTrue(ReportState.FINISHED.changeAllowed(ReportState.APPROVED));
        assertTrue(ReportState.FINISHED.changeAllowed(ReportState.CANCELLED));
        
        assertFalse(ReportState.FINISHED.changeAllowed(ReportState.FINISHED));
        assertFalse(ReportState.FINISHED.changeAllowed(ReportState.STARTED));
        assertFalse(ReportState.FINISHED.changeAllowed(ReportState.ANNOUNCED));
    }
    
    
    @Test
    void testApproved() {
        assertFalse(ReportState.APPROVED.changeAllowed(ReportState.ANNOUNCED));
        assertFalse(ReportState.APPROVED.changeAllowed(ReportState.STARTED));
        assertFalse(ReportState.APPROVED.changeAllowed(ReportState.FINISHED));
        assertFalse(ReportState.APPROVED.changeAllowed(ReportState.APPROVED));
        assertFalse(ReportState.APPROVED.changeAllowed(ReportState.CANCELLED));
    }
    
    
    @Test
    void testCancelled() {
        assertFalse(ReportState.CANCELLED.changeAllowed(ReportState.ANNOUNCED));
        assertFalse(ReportState.CANCELLED.changeAllowed(ReportState.STARTED));
        assertFalse(ReportState.CANCELLED.changeAllowed(ReportState.FINISHED));
        assertFalse(ReportState.CANCELLED.changeAllowed(ReportState.APPROVED));
        assertFalse(ReportState.CANCELLED.changeAllowed(ReportState.CANCELLED));
    }
    
}
