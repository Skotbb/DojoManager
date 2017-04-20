package com.android.thompson.scott.dojomanager;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.UUID;

import dojomanager.main.models.Student;
import dojomanager.storage.models.DojoStorageManager;


public class StudentFragment extends Fragment {
	private static final String ARG_STUDENT_ID = "student_id";

	private Student mStudent;

	private Spinner mRankLevel,
	mRankType;
	private EditText mFirstName, mLastName, mTimeRank;

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
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_student, container, false);

		mRankLevel = (Spinner) v.findViewById(R.id.student_rank_level_spinner);
		mRankLevel.setAdapter(getArrayAdapter(R.array.rank_levels));

		mRankType = (Spinner) v.findViewById(R.id.student_rank_type_spinner);
		mRankType.setAdapter(getArrayAdapter(R.array.rank_types));

		UUID studentId = (UUID) getArguments().getSerializable(ARG_STUDENT_ID);
		mStudent = DojoStorageManager.getInstance().getStudentById(studentId);
		fillInFieldsWithStudent(v, mStudent);

		// Inflate the layout for this fragment
		return v;
	}

	private void fillInFieldsWithStudent(View v, Student student) {
		mFirstName = (EditText) v.findViewById(R.id.student_firstName_et);
//		mFirstName.setHint("");
		mLastName = (EditText) v.findViewById(R.id.student_lastName_et);
//		mLastName.setHint("");
		mTimeRank = (EditText) v.findViewById(R.id.student_timeInRank_et);
//		mTimeRank.setHint("");

		mFirstName.setText(student.getFirstName());
		mLastName.setText(student.getLastName());
		mTimeRank.setText(String.valueOf(student.getRank().getTimeInRank()));
	}

	private ArrayAdapter<CharSequence> getArrayAdapter(int arrayResource) {
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
				arrayResource, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		return adapter;
	}

}
