/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.background.sync;


import android.content.Context;
import com.firebase.jobdispatcher.*;

import java.util.concurrent.TimeUnit;

// Class which allows us to schedule a job service
public class ReminderUtilities {
    private static final int REMINDER_INTERVAL_MINUTES = 15;
    // (15) Create three constants and one variable:
    // REMINDER_INTERVAL_SECONDS should be an integer constant storing the number of seconds in 15 minutes
    // Number of seconds we want the scheduler to wait before it starts our job
    private static final int REMINDER_INTERVAL_SECONDS = (int) TimeUnit.MINUTES.toSeconds(REMINDER_INTERVAL_MINUTES);
    // SYNC_FLEXTIME_SECONDS should also be an integer constant storing the number of seconds in 15 minutes
    private static final int SYNC_FLEXTIME_SECONDS = REMINDER_INTERVAL_SECONDS;
    // REMINDER_JOB_TAG should be a String constant, storing something like "hydration_reminder_tag"
    // A unique tag that identifies our Job
    private static final String REMINDER_JOB_TAG = "hydration_reminder_tag";
    //  - sInitialized should be a private static boolean variable which will store whether the job
    //    has been activated or not
    private static boolean sInitialized;

    // (16) Create a synchronized, public static method called scheduleChargingReminder that takes
    // in a context. This method will use FirebaseJobDispatcher to schedule a job that repeats roughly
    // every REMINDER_INTERVAL_SECONDS when the phone is charging. It will trigger WaterReminderFirebaseJobService
    // Note: The method is synchronized, because we do not want this method to execute more than once at a time
    synchronized public static void scheduleChargingReminder(Context context) {
        // (17) If the job has already been initialized, return
        if (sInitialized) {
            return;
        }

        // (18) Create a new GooglePlayDriver
        Driver googlePlayDriver = new GooglePlayDriver(context);
        // (19) Create a new FirebaseJobDispatcher with the driver
        FirebaseJobDispatcher firebaseJobDispatcher = new FirebaseJobDispatcher(googlePlayDriver);
        // (20) Use FirebaseJobDispatcher's newJobBuilder method to build a job which:
        // - has WaterReminderFirebaseJobService as it's service
        // - has the tag REMINDER_JOB_TAG
        // - only triggers if the device is charging
        // - has the lifetime of the job as forever
        // - has the job recur
        // - occurs every 15 minutes with a window of 15 minutes. You can do this using a
        //   setTrigger, passing in a Trigger.executionWindow
        // - replaces the current job if it's already running
        // Finally, you should build the job.
        Job waterReminderJob = firebaseJobDispatcher.newJobBuilder()
                // the JobService that will be called
                .setService(WaterReminderFirebaseJobService.class)
                // uniquely identifies the job
                .setTag(REMINDER_JOB_TAG)
                // recur the job
                .setRecurring(true)
                // has the Lifetime of the job as Forever i.e. Continue executing the job even after device reboots
                .setLifetime(Lifetime.FOREVER)
                // should execute every 15 minutes with 15 minute window. i.e. This will wait at least 15 minutes, and then will execute within 15 minutes window
                .setTrigger(Trigger.executionWindow(REMINDER_INTERVAL_SECONDS, REMINDER_INTERVAL_SECONDS + SYNC_FLEXTIME_SECONDS))
                // Overwrite/replace the existing job with the same tag, if this already running
                .setReplaceCurrent(true)
                // constraints that need to be satisfied for the job to run
                .setConstraints(
                        // Only trigger this job, while the device is charging
                        Constraint.DEVICE_CHARGING
                )
                // build the job
                .build();

        // (21) Use dispatcher's schedule method to schedule the job
        firebaseJobDispatcher.schedule(waterReminderJob);
        // (22) Set sInitialized to true to mark that we're done setting up the job
        sInitialized = true;
    }
}
