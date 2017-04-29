package com.android.thompson.scott.dojomanager;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.UUID;

import dojomanager.main.models.Student;
import dojomanager.storage.models.DojoStorageManager;


/**
 * A simple {@link Fragment} subclass.
 */
public class NotesListFragment extends Fragment {
	public static final String ARGS_STUDENTID = "arg_studentId";
	public static final String ARGS_NOTEINDEX = "arg_noteIndexForStudent";
	public static final String ARGS_BUNDLE = "arg_bundleofbundles";

	RecyclerView mRecylerView;
	NoteAdapter mAdapter;
	DojoStorageManager mDsm;

	Student mStudent;
	int mNoteIndex = -1;

	public static NotesListFragment newInstance(UUID studentId) {
		Bundle args = new Bundle();
		args.putSerializable(ARGS_STUDENTID, studentId);

		NotesListFragment frag = new NotesListFragment();
		frag.setArguments(args);

		return frag;
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_notes_list, container, false);

		mRecylerView = (RecyclerView) view.findViewById(R.id.notes_recycler_view);
		mRecylerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		mDsm = new DojoStorageManager(getContext());
		try {
			UUID id = (UUID) getArguments().getSerializable(ARGS_STUDENTID);
			mStudent = mDsm.getDojoManager().getStudentById(id);
			updateUI();

			//	Floating action button for new Note.
			FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.note_newNote_fab);
			fab.setOnClickListener(v -> {
				NoteFragment frag = new NoteFragment();
				Bundle bundle = new Bundle();
				Bundle args_id = new Bundle();
				Bundle args_index = new Bundle();

				args_id.putSerializable(ARGS_STUDENTID, mStudent.getId());
				args_index.putInt(ARGS_NOTEINDEX, mNoteIndex);
				bundle.putBundle(ARGS_NOTEINDEX, args_index);
				bundle.putBundle(ARGS_STUDENTID, args_id);

				frag.setArguments(bundle);

				FragmentTransaction ft = getFragmentManager().beginTransaction();
				// Replace current (note list) fragment with note fragment
				ft.replace(R.id.notes_list_fragment, frag);
				// Put note list fragment on the back stack, so it can handle the back button.
				ft.addToBackStack(null);
				// Commit actions to transaction.
				ft.commit();
			});
		} catch (Exception e) {
			Log.e("NotesListFragment", e.getMessage());
		}

		return view;
	}

	private void updateUI() {
		ArrayList<String> mNotes;

		if(mStudent != null) {
			mNotes = mStudent.getStudentNotes();
			mAdapter = new NoteAdapter(mNotes);
			mRecylerView.setAdapter(mAdapter);
		} else {
			Toast.makeText(getContext(), "Uh oh.", Toast.LENGTH_SHORT).show();
		}
	}

	protected Bundle createPrettyBundle(UUID id, int index) {
		Bundle bundle = new Bundle();
		Bundle args_id = new Bundle();
		Bundle args_index = new Bundle();

		args_id.putSerializable(ARGS_STUDENTID, id);
		args_index.putInt(ARGS_NOTEINDEX, index);
		bundle.putBundle(ARGS_NOTEINDEX, args_index);
		bundle.putBundle(ARGS_STUDENTID, args_id);

		return bundle;
	}

//	@Override
//	public void onSaveInstanceState(Bundle outState) {
//		super.onSaveInstanceState(outState);
//
//		outState.putBundle("SAVEDINSTANCE", createPrettyBundle(mStudent.getId(), mNoteIndex));
//	}

	private class NoteHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		private TextView mNoteText;

		private String mNote;

		public NoteHolder(View itemView) {
			super(itemView);
			itemView.setOnClickListener(this);

			mNoteText = (TextView) itemView.findViewById(R.id.list_item_note_text);
		}

		public void bindNote(String note) {
			if(note.length() < 101) {
				mNote = note;
			} else {
				mNote = note.substring(0, 101);
			}

			mNoteText.setText(mNote);
		}

		@Override
		public void onClick(View v) {
			Toast.makeText(getActivity(), mNote, Toast.LENGTH_SHORT).show();

			NoteFragment frag = new NoteFragment();

			for(int i=0; i < mStudent.getStudentNotes().size(); i++) {
				if(mStudent.getNoteAt(i).contains(mNote)) {
					mNoteIndex = i;
					break;
				}
			}

			frag.setArguments(createPrettyBundle(mStudent.getId(), mNoteIndex));

			FragmentTransaction ft = getFragmentManager().beginTransaction();
			// Replace current (note list) fragment with note fragment
			ft.replace(R.id.notes_list_fragment, frag);
			// Put note list fragment on the back stack, so it can handle the back button.
			ft.addToBackStack(null);
			// Commit actions to transaction.
			ft.commit();
		}
	}

	private class NoteAdapter extends RecyclerView.Adapter<NoteHolder> {
		private ArrayList<String> mNotes;

		NoteAdapter(ArrayList<String> notes){mNotes = notes;}

		@Override
		public NoteHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			LayoutInflater li = LayoutInflater.from(getActivity());
			View view = li.inflate(R.layout.list_item_note, parent, false);
			return new NoteHolder(view);
		}

		@Override
		public void onBindViewHolder(NoteHolder holder, int position) {
			String note = mNotes.get(position);
			holder.bindNote(note);
		}

		@Override
		public int getItemCount() {return mNotes.size();}
	}
}
