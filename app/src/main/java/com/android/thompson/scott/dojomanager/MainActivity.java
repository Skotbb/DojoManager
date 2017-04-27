package com.android.thompson.scott.dojomanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
	public final String PREFS_NAME = "MY_PREFS";

	private String[] mDrawerItems;
	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;
	private ListView mDrawerList;
	private CharSequence mTitle, mDrawerTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Toolbar mTool = (Toolbar) findViewById(R.id.main_activity_toolbar);
		setSupportActionBar(mTool);

		mTitle = mDrawerTitle = getTitle();
		mDrawerItems = getResources().getStringArray(R.array.main_drawer_items);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.activity_main_drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.activity_main_left_drawer);
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.string.drawer_open, R.string.drawer_close) {

			// When drawer gets closed
			@Override
			public void onDrawerClosed(View drawerView) {
				super.onDrawerClosed(drawerView);
				getSupportActionBar().setTitle(mTitle);
				invalidateOptionsMenu();
			}

			// When drawer gets opened
			@Override
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				getSupportActionBar().setTitle(mDrawerTitle);
				invalidateOptionsMenu();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);

		// Set adapter for List view
		mDrawerList.setAdapter(new ArrayAdapter<String>(this,
				R.layout.drawer_list_item, mDrawerItems));
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		// Default to class calendar.
		selectItem(0);

//        //Get SharedPreferences and check if Dojo has been set up.
//        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
//        boolean isSetup = settings.getBoolean(getString(R.string.const_shared_pref_isSetup), false);
//
//        if(isSetup){
//            startStudentActivity();
//        }
//        else{
//            // Set isSetup to true
//            SharedPreferences.Editor editor = settings.edit();
//            editor.putBoolean(getString(R.string.const_shared_pref_isSetup), true);
//            editor.commit();
//            startSettingsActivity();
//        }
	}

	@Override
	protected void onPostCreate(@Nullable Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		if(mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}

		if (id == R.id.settings) {
			Toast.makeText(this, "Fuck off.", Toast.LENGTH_SHORT).show();
			return true;
		}
		return true;
	}

	//
//    private void startSettingsActivity(){
//        // Start the Setup Activity
//        Intent intent = new Intent(this, SetupDojoActivity.class);
//        startActivity(intent);
//    }
//    private void startStudentActivity(){
//        // Start the Setup Activity
//        Intent intent = new Intent(this, StudentListActivity.class);
//        startActivity(intent);
//    }
	private void selectItem(int position) {
		switch (position) {
			case 0: {
				// Class calendar
				ClassCalendar frag = new ClassCalendar();
				FragmentManager fm = getSupportFragmentManager();
				fm.beginTransaction()
						.replace(R.id.activity_main_content_frame, frag)
						.addToBackStack(null)
						.commit();
			}
			break;
			case 1: {
				// Student management
				// Setup fragment
				StudentListFragment frag = new StudentListFragment();
				// Ready FragManager for replacement.
				FragmentManager fm = getSupportFragmentManager();
				fm.beginTransaction()
						.replace(R.id.activity_main_content_frame, frag)
						.commit();
			}
			break;
			case 2: {
				// Dojo Settings
				Toast.makeText(this, "Not implemented, yet", Toast.LENGTH_SHORT).show();
			}
			break;
		}
		// Highlight item, update title, and close drawer.
		mDrawerList.setItemChecked(position, true);
		setTitle(mDrawerItems[position]);
		mDrawerLayout.closeDrawer(mDrawerList);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// If drawer is open, hide action items.
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		menu.findItem(R.id.settings).setVisible(!drawerOpen);

		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getSupportActionBar().setTitle(mTitle);
	}

	private class DrawerItemClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			selectItem(position);
		}
	}
}
