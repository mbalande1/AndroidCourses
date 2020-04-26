package android.example.com.visualizerpreferences;


import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.preference.*;

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        // Generate the Preference screen from the xml file
        setPreferencesFromResource(R.xml.preference_visualizer, s);

        // Get all the preferences in the PreferenceScreen
        PreferenceScreen preferenceScreen = getPreferenceScreen();
        SharedPreferences sharedPreferences = preferenceScreen.getSharedPreferences();
        // Go through all of the preferences, and set up their preference summary.
        for (int i = 0; i < preferenceScreen.getPreferenceCount(); i++) {
            Preference preference = preferenceScreen.getPreference(i);
            if (!(preference instanceof CheckBoxPreference)) {
                setPreferenceSummary(preference, sharedPreferences.getString(preference.getKey(), ""));
            }
            // We don't need to set up preference summaries for checkbox preferences because
            // they are already set up in xml using summaryOff and summary On
        }

    }

    private void setPreferenceSummary(Preference preference, String value) {

        if (preference instanceof ListPreference) {
            ListPreference listPreference = (ListPreference) preference;
            // We want to show the ListPreference option 'label' instead of the 'value' as values are not translatable
            // Find the index of the selected value
            int valueIndex = listPreference.findIndexOfValue(value);
            // Since each value has the corresponding label in the labels array (see arrays.xml)
            CharSequence[] labelArray = listPreference.getEntries();
            // Set the summary to the label of the selected value
            listPreference.setSummary(labelArray[valueIndex]);
        } else if (preference instanceof EditTextPreference) {
            // For EditTextPreference, set the Summary to the value's simple string representation
            preference.setSummary(value);
        }

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        // Figure out which preference was changed
        Preference preference = findPreference(key);
        // Check if the Preference changed, is the ListPreference
        if (preference instanceof ListPreference) {
            ListPreference listPreference = (ListPreference) preference;
            setPreferenceSummary(listPreference, sharedPreferences.getString(key, getString(R.string.pref_color_red_label)));
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Register the OnSharedPreferenceChangeListener to this
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }
}
