package com.android.thompson.scott.dojomanager;

import android.content.Context;
import android.content.Intent;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.UUID;

import dojomanager.main.models.Student;
import dojomanager.storage.models.DojoStorageManager;

import static android.R.attr.fragment;

public class NotesListActivity extends AppCompatActivity {
	private static final String ARGS_STUDENTID = "args_studID_activity";

	private UUID id;

	public static Intent newInstance(Context packageContext, UUID studentId) {
		Intent intent = new Intent(packageContext, NotesListActivity.class);
		intent.putExtra(ARGS_STUDENTID, studentId);

		return intent;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_notes_list);
		Toolbar toooool = (Toolbar) findViewById(R.id.notes_list_toolbar);
		setSupportActionBar(toooool);

		id = (UUID) getIntent().getSerializableExtra(ARGS_STUDENTID);
		FragmentManager fm = getSupportFragmentManager();
		if(savedInstanceState != null) {
			NotesListFragment oldNLF = (NotesListFragment) fm.getFragment(savedInstanceState, "NotesListFragment");
			NoteFragment oldNF = (NoteFragment) fm.getFragment(savedInstanceState, "NoteFragment");
			if(oldNLF != null && oldNF != null) {
				String cheese;
			}
		}

			NotesListFragment frag = NotesListFragment.newInstance(id);
			fm.beginTransaction()
					.add(R.id.notes_list_fragment, frag)
					.commit();
	}

//	@Override
//	public void onSaveInstanceState(Bundle outState) {
//		super.onSaveInstanceState(outState);
//		outState.putSerializable(ARGS_STUDENTID, id);
//	}

//	@Override
//	protected void onRestoreInstanceState(Bundle savedInstanceState) {
//		super.onRestoreInstanceState(savedInstanceState);
//		id = (UUID) savedInstanceState.getSerializable(ARGS_STUDENTID);
//		FragmentManager fm = getSupportFragmentManager();
//		NotesListFragment frag = NotesListFragment.newInstance(id);
//		fm.beginTransaction()
//				.add(R.id.notes_list_fragment, frag)
//				.commit();
//	}
}
