package com.android.thompson.scott.dojomanager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

public class StudentListActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_student_list);


		FragmentManager fm = getSupportFragmentManager();
		Fragment frag = fm.findFragmentById(R.id.student_list_fragment);

		if(frag == null) {
			frag = new StudentListFragment();
			fm.beginTransaction()
					.add(R.id.student_list_fragment, frag)
					.commit();
		}
	}

}
