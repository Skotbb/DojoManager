package com.android.thompson.scott.dojomanager;

import android.preference.PreferenceActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SetupDojoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Setup SettingsFragment as main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SetupDojoFragment())
                .commit();
    }
}
