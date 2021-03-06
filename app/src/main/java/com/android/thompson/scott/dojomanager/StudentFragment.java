package com.android.thompson.scott.dojomanager;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import dojomanager.main.models.Rank;
import dojomanager.main.models.Student;
import dojomanager.storage.models.DojoManager;
import dojomanager.storage.models.DojoStorageManager;

import static android.R.attr.data;


public class StudentFragment extends Fragment implements Button.OnClickListener{
	private static final String ARG_STUDENT_ID = "student_id";
	private static final String DIALOG_DATE = "DialogDate";
	private static final int REQUEST_DATE = 666;

	private DojoStorageManager dsm;
	private Student mStudent;
	private UUID mId;

	private View mMainView;
	private Spinner mRankLevel,
	mRankType;
	private EditText mFirstName, mLastName, mTimeRank;
	private TextView mAge;
	private Button mPromote, mBday, mViewNotes;
	private CheckBox mPaid;

	public StudentFragment() {
		// Required empty public constructor
	}

	public static StudentFragment newInstance(UUID studentId) {
		Bundle args = new Bundle();
		args.putSerializable(ARG_STUDENT_ID, studentId);

		StudentFragment frag = new StudentFragment();
		frag.setArguments(args);
		return frag;
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		mMainView = inflater.inflate(R.layout.fragment_student, container, false);

		mRankLevel = (Spinner) mMainView.findViewById(R.id.student_rank_level_spinner);
		mRankLevel.setAdapter(getArrayAdapter(R.array.rank_levels));

		mRankType = (Spinner) mMainView.findViewById(R.id.student_rank_type_spinner);
		mRankType.setAdapter(getArrayAdapter(R.array.rank_types));

		mPromote = (Button) mMainView.findViewById(R.id.student_promote_button);
		mPromote.setOnClickListener(this);
		mBday = (Button) mMainView.findViewById(R.id.student_changeBday_button);
		mBday.setOnClickListener(this);
		mViewNotes = (Button) mMainView.findViewById(R.id.student_viewNotes_button);
		mViewNotes.setOnClickListener(this);

		mId = (UUID) getArguments().getSerializable(ARG_STUDENT_ID);
			// Data managers
		dsm = new DojoStorageManager(getContext());
		mStudent = dsm.getDojoManager().getStudentById(mId);

		fillInFieldsWithStudent(mMainView);

		// Inflate the layout for this fragment
		return mMainView;
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
				saveStudent();
				return true;
			}
			case R.id.menu_item_student_delete : {
				if(dsm.getDojoManager().getStudents().remove(mStudent)) {
					Toast.makeText(getContext(), R.string.removed, Toast.LENGTH_SHORT).show();
					dsm.writeStudents();
					getActivity().finish();
					return true;
				} else {
					Toast.makeText(getContext(), R.string.save_failed, Toast.LENGTH_SHORT).show();
				}
			}

			default: return super.onOptionsItemSelected(item);
		}
	}

	private void fillInFieldsWithStudent(View v) {
		mFirstName = (EditText) v.findViewById(R.id.student_firstName_et);
		mLastName = (EditText) v.findViewById(R.id.student_lastName_et);
		mTimeRank = (EditText) v.findViewById(R.id.student_timeInRank_et);
		mAge = (TextView) v.findViewById(R.id.student_age_tv);
		mPaid = (CheckBox) v.findViewById(R.id.student_isPaid_cb);
		String temp = "New Student";


		mFirstName.setText(mStudent.getFirstName());
		mLastName.setText(mStudent.getLastName());
		getActivity().setTitle(mStudent.getFirstName() + " " + mStudent.getLastName());
		mFirstName.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
				getActivity().setTitle(charSequence + " " + mStudent.getLastName());
			}
			@Override
			public void afterTextChanged(Editable editable) {}
		});
		mLastName.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
				getActivity().setTitle(mStudent.getFirstName() + " " + charSequence);
			}
			@Override
			public void afterTextChanged(Editable editable) {}
		});

		mTimeRank.setText(String.valueOf(mStudent.getRank().getTimeInRank()));
		mRankLevel.setSelection(getArrayAdapter(R.array.rank_levels).getPosition(String.valueOf(mStudent.getRank().getRankLevel())));
		mRankType.setSelection(getArrayAdapter(R.array.rank_types).getPosition(String.valueOf(mStudent.getRank().getRank())));
		updateBirthday();
		mPaid.setChecked(mStudent.isPaidUp());
	}

	private ArrayAdapter<CharSequence> getArrayAdapter(int arrayResource) {
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
				arrayResource, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		return adapter;
	}

	private void updateBirthday() {
		SimpleDateFormat df = new SimpleDateFormat("MMM dd, yyyy");
		mBday.setText(df.format(mStudent.getBirthDate().getTime()));
		mAge.setText(String.valueOf(mStudent.getAge()));
	}

	public void getStudentFromFields(View v) {
		Rank.RankType rankType;
		String fName, lName, temp;
		int rankLevel;
		double timeInRank;
		mFirstName = (EditText) v.findViewById(R.id.student_firstName_et);
		mLastName = (EditText) v.findViewById(R.id.student_lastName_et);
		mTimeRank = (EditText) v.findViewById(R.id.student_timeInRank_et);
		mPaid = (CheckBox) v.findViewById(R.id.student_isPaid_cb);

		mStudent.setFirstName(mFirstName.getText().toString().trim());
		mStudent.setLastName(mLastName.getText().toString().trim());
//		temp = mRankLevel.getSelectedItem().toString();
//		rankLevel = Integer.valueOf(temp);
//		mStudent.getRank().setRankLevel(rankLevel);
		mStudent.getRank().setRankLevel(Integer.valueOf(mRankLevel.getSelectedItem().toString()));
		timeInRank = checkDoubleForRange(-1.0, 6000.0, Double.valueOf(mTimeRank.getText().toString()));
		if(timeInRank != -1){
			mStudent.setTimeInRank(timeInRank);
		}
		mStudent.setRankType(mRankType.getSelectedItem().toString().equals(Rank.RankType.Dan.toString()) ? Rank.RankType.Dan : Rank.RankType.Kyu);
		mStudent.setPaidUp(mPaid.isChecked());
	}

	private void saveStudent() {
		if(mMainView != null) {
			getStudentFromFields(mMainView);
			dsm.getDojoManager().replaceStudent(mStudent);
		}
		if (dsm.writeStudents()) {
			Toast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(getContext(), R.string.save_failed, Toast.LENGTH_SHORT).show();
		}
	}

	private Double checkDoubleForRange(Double min, Double max, double input) {
		if(input > min && input < max) {
			return input;
		} else {
			if(input < min) {
				Toast.makeText(getContext(), getString(R.string.toast_inputTooLow) + String.valueOf(min), Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(getContext(), getString(R.string.toast_inputTooHigh) + String.valueOf(max), Toast.LENGTH_SHORT).show();
			}
			return -1.0;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.student_promote_button : {
				if(mStudent != null) {
					mStudent.getRank().promote();
					fillInFieldsWithStudent(v.getRootView());
				}
			} break;
			case R.id.student_changeBday_button : {
				FragmentManager fm = getFragmentManager();
				DatePickerFragment dialog = DatePickerFragment.newInstance(mStudent.getBirthDate().getTime());
				dialog.setTargetFragment(StudentFragment.this, REQUEST_DATE);
				dialog.show(fm, DIALOG_DATE);
			} break;
			case R.id.student_viewNotes_button : {
				saveStudent();
				Intent intent = NotesListActivity.newInstance(getContext(), mStudent.getId());
				startActivity(intent);
			} break;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode != Activity.RESULT_OK) {
			return;
		}

		if(requestCode == REQUEST_DATE) {
			Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
			mStudent.setBirthDate(date);
			updateBirthday();
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		if(dsm == null) {
			Toast.makeText(getContext(), R.string.save_failed, Toast.LENGTH_SHORT).show();
		}
//		if(!dsm.writeStudents()){
//			Toast.makeText(getContext(), R.string.save_failed, Toast.LENGTH_SHORT).show();
//		}
		saveStudent();
	}
}
