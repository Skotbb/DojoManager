package com.android.thompson.scott.dojomanager;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.UUID;

import dojomanager.main.models.Student;
import dojomanager.storage.models.DojoStorageManager;

import static android.R.attr.fragment;

public class NotesListActivity extends AppCompatActivity {
	private static final String ARGS_STUDENTID = "args_studID_activity";

	public static Intent newInstance(Context packageContext, UUID studentId) {
		Intent intent = new Intent(packageContext, NotesListActivity.class);
		intent.putExtra(ARGS_STUDENTID, studentId);

		return intent;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notes_list);

		UUID id = (UUID) getIntent().getSerializableExtra(ARGS_STUDENTID);
		FragmentManager fm = getSupportFragmentManager();
			NotesListFragment frag = NotesListFragment.newInstance(id);
			fm.beginTransaction()
					.add(R.id.notes_list_fragment, frag)
					.commit();
	}
}