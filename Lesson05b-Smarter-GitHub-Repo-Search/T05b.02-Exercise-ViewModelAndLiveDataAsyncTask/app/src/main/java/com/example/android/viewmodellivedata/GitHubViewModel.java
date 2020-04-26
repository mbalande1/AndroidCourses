package com.example.android.viewmodellivedata;

import android.os.AsyncTask;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.android.viewmodellivedata.utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;

public class GitHubViewModel extends ViewModel {

    // Create a live data containing string
    private static MutableLiveData<String> currentQueryData;

    public MutableLiveData<String> getCurrentQueryData() {
        // Check if we have already created our liveData object - to cache the data for configuration changes
        if (currentQueryData == null) {
            currentQueryData = new MutableLiveData<>();
        }

        return currentQueryData;
    }

    public void searchGitHub(URL githubQueryUrl) {
        GithubQueryTask githubQueryTask = new GithubQueryTask();
        githubQueryTask.execute(githubQueryUrl);
    }

    private static class GithubQueryTask extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... urls) {

            URL searchUrl = urls[0];
            String githubSearchResults = null;
            try {
                githubSearchResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return githubSearchResults;
        }

        @Override
        protected void onPostExecute(String githubSearchResults) {
            currentQueryData.setValue(githubSearchResults);
        }
    }

}
