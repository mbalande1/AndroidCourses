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

import android.os.AsyncTask;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

// (3) WaterReminderFirebaseJobService should extend from JobService
public class WaterReminderFirebaseJobService extends JobService {

    private AsyncTask mBackgroundTask;
    // (4) Override onStartJob
    @Override
    public boolean onStartJob(final JobParameters jobParameters) {
        // (5) By default, jobs are executed on the main thread. Typically you will be doing a time-consuming tasks in service e.g. uploading a file from internet
        // Make an anonymous class extending AsyncTask called mBackgroundTask.
        mBackgroundTask = new AsyncTask() {
            // (6) Override doInBackground
            @Override
            protected Object doInBackground(Object[] objects) {
                // (7) Use ReminderTasks to execute the new charging reminder task you made, use
                // this service as the context (WaterReminderFirebaseJobService.this) and return null
                // when finished.
                ReminderTasks.executeTask(WaterReminderFirebaseJobService.this, ReminderTasks.ACTION_CHARGING_REMINDER);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                // (8) Override onPostExecute and call jobFinished. Pass the job parameters
                // and false to jobFinished. This will inform the JobManager that your job is done
                // and that you do not want to reschedule the job.
                jobFinished(jobParameters, false);
            }
        };

        // (9) Execute the above defined async task
        mBackgroundTask.execute();
        // (10) Return true, because our job may not be finished yet, since we are running the above code in a separate thread i.e. AsyncTask
        return true;   // Answers the question: "Is there still work going on?"
    }

    // (11) Override onStopJob
    // onStopJob is called when the requirement of your job are no longer met.
    // For example: If you have a job that downloads a large video file only when your device is connected to a -
    // Wifi network. In this case onStopJob will be called, when the job has started on wifi, and in the middle of -
    // downloading this file the wifi connection gets lost, onStopJob will be triggered
    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        // (12) Cancel the Async task that is already running
        if (mBackgroundTask != null) mBackgroundTask.cancel(true);
        // (13) Return true. As soon as the conditions are re-met, the job should be re-tried again.
        return true;   // Answers the question: "Should this job be retried?"
    }

}
