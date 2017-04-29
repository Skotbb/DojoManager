package com.android.thompson.scott.dojomanager;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import java.util.UUID;

import dojomanager.main.models.Student;
import dojomanager.storage.models.DojoStorageManager;

import static android.R.string.no;


public class NoteFragment extends Fragment {
	DojoStorageManager mDsm;
	Student mStudent;
	int mNoteIndex;

	EditText mNoteText;

	public NoteFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Set view stuff
		View view = inflater.inflate(R.layout.fragment_note, container, false);
		mNoteText = (EditText) view.findViewById(R.id.note_noteInput_et);
		// Read args from bundle
		Bundle bundle = getArguments().getBundle(NotesListFragment.ARGS_STUDENTID);
		UUID studentId = (UUID) bundle.getSerializable(NotesListFragment.ARGS_STUDENTID);
		bundle = getArguments().getBundle(NotesListFragment.ARGS_NOTEINDEX);
		mNoteIndex = bundle.getInt(NotesListFragment.ARGS_NOTEINDEX, -1);
		// Construct supporting objects
		mDsm = new DojoStorageManager(getContext());
		mStudent = mDsm.getDojoManager().getStudentById(studentId);
		// Handle responses.
		if (mNoteIndex == -1) {
			String note = getResources().getString(R.string.student_note_addNote);
			mStudent.addNote(note);
			mNoteIndex = mStudent.getStudentNotes().size() - 1;
		} else {

		}
		setNoteText(view);

		return view;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.fragment_student, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_item_student_save: {
				saveStudents();
				getFragmentManager().popBackStack();
				return true;
			}
			case R.id.menu_item_student_delete: {
				mStudent.getStudentNotes().remove(mNoteIndex);
				saveStudents();
				getFragmentManager().popBackStack();
				return true;
			}
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	private void setNoteText(View view) {
		if (mNoteText == null) {
			mNoteText = (EditText) view.findViewById(R.id.note_noteInput_et);
		}
		mNoteText.setText(mStudent.getNoteAt(mNoteIndex));
	}

	private boolean saveStudents() {
		if (mNoteIndex < mStudent.getStudentNotes().size()) {
			String newNote = mNoteText.getText().toString();
			mStudent.getStudentNotes().set(mNoteIndex, newNote);
		}
		if (mDsm.writeStudents()) {
			Toast.makeText(getContext(), R.string.save, Toast.LENGTH_SHORT).show();
			return true;
		} else {
			Toast.makeText(getContext(), R.string.save_failed, Toast.LENGTH_SHORT).show();
			return false;
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		saveStudents();
	}
}
