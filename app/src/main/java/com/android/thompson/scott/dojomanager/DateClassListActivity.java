package com.android.thompson.scott.dojomanager;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.prolificinteractive.materialcalendarview.CalendarDay;

public class DateClassListActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_date_class_list);
		Toolbar toolbar = (Toolbar) findViewById(R.id.date_class_list_toolbar);
		setSupportActionBar(toolbar);


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
}
