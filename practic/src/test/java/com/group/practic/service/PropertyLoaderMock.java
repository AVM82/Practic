package com.group.practic.service;

public class PropertyLoaderMock {
    public boolean isInitialized() {
        return true;
    }

    public String getProperty(String key, String defaultValue) {
        if (key.equals("reportDurationMinutes")) {
            return "30";
        } else if (key.equals("numberOfDays")) {
            return "5";
        } else if (key.equals("firstReportTime")) {
            return "16:00";
        } else if (key.equals("lastReportTime")) {
            return "22:00";
        } else {
            return defaultValue;
        }
    }
}