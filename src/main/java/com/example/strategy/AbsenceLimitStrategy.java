package com.example.strategy;

import java.util.Arrays;

public enum AbsenceLimitStrategy {
    LIMIT_3_FOR_30_HOURS(30, 3),
    LIMIT_5_FOR_60_HOURS(60, 5),
    LIMIT_7_FOR_90_HOURS(90, 7),
    LIMIT_11_FOR_120_HOURS(120, 11);

    private final int weeklyHours;
    private final int absenceLimit;

    AbsenceLimitStrategy(int weeklyHours, int absenceLimit) {
        this.weeklyHours = weeklyHours;
        this.absenceLimit = absenceLimit;
    }

    public int getWeeklyHours() {
        return weeklyHours;
    }

    public int getAbsenceLimit() {
        return absenceLimit;
    }

    /**
     * Calculate absence limit based on weekly hours
     * Returns the most appropriate absence limit for the given weekly hours
     */
    public static int calculateAbsenceLimit(int weeklyHours) {
        return Arrays.stream(values())
                .filter(s -> weeklyHours <= s.weeklyHours)
                .findFirst()
                .map(AbsenceLimitStrategy::getAbsenceLimit)
                .orElse(values()[values().length - 1].absenceLimit);
    }

    /**
     * Check if the absence count has exceeded the limit
     * Student fails at limit + 1
     */
    public static boolean hasExceededLimit(int absenceCount, int absenceLimit) {
        return absenceCount > absenceLimit;
    }
}

