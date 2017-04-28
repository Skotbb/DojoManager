package com.android.thompson.scott.dojomanager;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.prolificinteractive.materialcalendarview.CalendarDay;

public class DateClassListActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_date_class_list);
		Toolbar toolbar = (Toolbar) findViewById(R.id.date_class_list_toolbar);
		setSupportActionBar(toolbar);
		toolbar.setTitle(R.string.title_activity_date_class_list);
		toolbar.setBackground(new ColorDrawable());

		Bundle dateBundle = getIntent().getBundleExtra(ClassCalendar.EXTRA_DATE_CLASS);
		DateClassListFragment frag = new DateClassListFragment();
		frag.setArguments(dateBundle);
		FragmentManager fm = getSupportFragmentManager();
		fm.beginTransaction()
				.add(R.id.date_class_list_fragmentHolder, frag)
				.commit();

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public void setTitle(CharSequence title) {
		super.setTitle(title);
		getSupportActionBar().setTitle(title);
	}

	public void setToolBarColor(String color) {
		try {
			getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(color)));
		} catch(NullPointerException e) {
			Log.e(this.toString(), e.getMessage());
		}
	}
	public void setToolBarColor(int colorRes) {
		try {
			getSupportActionBar().setBackgroundDrawable(new ColorDrawable(colorRes));
		} catch(NullPointerException e) {
			Log.e(this.toString(), e.getMessage());
		}
	}
}
