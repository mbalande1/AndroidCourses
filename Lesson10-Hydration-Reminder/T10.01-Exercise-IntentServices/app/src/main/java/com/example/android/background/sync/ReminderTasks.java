package com.example.android.background.sync;

import android.content.Context;
import com.example.android.background.utilities.PreferenceUtilities;

// 1. Create a class called ReminderTasks
public class ReminderTasks {
    // 2. Create a public static constant String called ACTION_INCREMENT_WATER_COUNT
    public static final String ACTION_INCREMENT_WATER_COUNT = "increment-water-count";

    // Create a public static void method called executeTask that will execute the main logic
    public static void executeTask(Context context, String action) {
        // If the action equals ACTION_INCREMENT_WATER_COUNT, call this class's incrementWaterCount
        if (action.equals(ACTION_INCREMENT_WATER_COUNT)) {
            incrementWaterCount(context);
        }
    }

    // Create a private static void method called incrementWaterCount
    private static void incrementWaterCount(Context context) {
        // Increment the water count using the preference utility
        PreferenceUtilities.incrementWaterCount(context);
    }

}



