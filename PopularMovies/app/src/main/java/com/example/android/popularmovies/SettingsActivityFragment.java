package com.example.android.popularmovies;

import android.content.SharedPreferences;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.os.Bundle;
/**
 * A placeholder fragment containing a simple view.
 */
public class SettingsActivityFragment extends PreferenceFragment {

    public SettingsActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String SORT_PREF_KEY= getString(R.string.sort_preference_key);
        addPreferencesFromResource(R.xml.pref_general);
        bindPreferenceSummaryToValues(findPreference(SORT_PREF_KEY));
    }


    @Override
    public void onStart() {
        String SORT_PREF_KEY= getString(R.string.sort_preference_key);
        ListPreference sort_preference = (ListPreference) findPreference(SORT_PREF_KEY);
        if(sort_preference.getEntry()!=null)
        {
            sort_preference.setDefaultValue(sort_preference.getEntry());
            sort_preference.setSummary(sort_preference.getEntry());
        }
        else
        sort_preference.setValueIndex(0);
        super.onStart();
    }


    void bindPreferenceSummaryToValues(final Preference pref)
    {

        Preference.OnPreferenceChangeListener lp =new Preference.OnPreferenceChangeListener() {
           @Override
           public boolean onPreferenceChange(Preference preference, Object newValue) {
               if ((preference instanceof ListPreference) == true) {

                   SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
                   boolean isChanged = pref.getBoolean(getString(R.string.preference_change_key),true);
                   if(!newValue.equals(pref.getString(getString(R.string.sort_preference_key),getString(R.string.popular_preference_key))))
                   pref.edit().putBoolean(getString(R.string.preference_change_key),!isChanged).commit();
                   SharedPreferences.Editor editor = preference.getEditor();
                   editor.putString(preference.getKey(), newValue.toString()).commit();
                   ListPreference lp = (ListPreference) preference;
                   int i = lp.findIndexOfValue(newValue.toString());
                   if (i < 0)
                       return false;

                   CharSequence[] arr = lp.getEntries();
                   CharSequence str = arr[i];
                   preference.setSummary(str);

               }
               return true;
           }
       };

       pref.setOnPreferenceChangeListener(lp);
      }


}
