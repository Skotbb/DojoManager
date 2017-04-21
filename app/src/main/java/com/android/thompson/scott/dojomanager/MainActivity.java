package com.android.thompson.scott.dojomanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends AppCompatActivity {
    public final String PREFS_NAME = "MY_PREFS";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Get SharedPreferences and check if Dojo has been set up.
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        boolean isSetup = settings.getBoolean(getString(R.string.const_shared_pref_isSetup), false);

        if(isSetup){
            startStudentActivity();
        }
        else{
            // Set isSetup to true
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean(getString(R.string.const_shared_pref_isSetup), true);
            editor.commit();
            startSettingsActivity();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.settings){
            startSettingsActivity();

            return true;
        }
        return true;
    }

    private void startSettingsActivity(){
        // Start the Setup Activity
        Intent intent = new Intent(this, SetupDojoActivity.class);
        startActivity(intent);
    }
    private void startStudentActivity(){
        // Start the Setup Activity
        Intent intent = new Intent(this, StudentListActivity.class);
        startActivity(intent);
    }
}
