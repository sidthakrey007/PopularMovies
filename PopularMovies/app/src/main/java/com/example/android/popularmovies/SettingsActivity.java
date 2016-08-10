package com.example.android.popularmovies;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class SettingsActivity extends AppCompatActivity {


    SettingsActivityFragment s = null;
    String FRAGMENT_KEY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FRAGMENT_KEY = getString(R.string.settings_fragment_key);
        if(savedInstanceState==null) {
            setContentView(R.layout.activity_settings);
            s = new SettingsActivityFragment();
            getFragmentManager().beginTransaction().replace(android.R.id.content, s).commit();
        }
        else
        {
            s = (SettingsActivityFragment) getFragmentManager().getFragment(savedInstanceState, FRAGMENT_KEY);
            getFragmentManager().beginTransaction().replace(android.R.id.content, s).commit();
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        getFragmentManager().putFragment(outState, FRAGMENT_KEY, s);
        super.onSaveInstanceState(outState);
    }
}
