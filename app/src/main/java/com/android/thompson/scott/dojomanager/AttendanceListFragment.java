package com.android.thompson.scott.dojomanager;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import dojomanager.main.models.DojoClass;
import dojomanager.main.models.Student;
import dojomanager.storage.models.DojoManager;
import dojomanager.storage.models.DojoStorageManager;

import static com.android.thompson.scott.dojomanager.DateClassListFragment.ARGS_CLASSID;


/**
 * A simple {@link Fragment} subclass.
 */
public class AttendanceListFragment extends Fragment {

	private RecyclerView mRecycler;
	protected AttendanceAdapter mAdapter;
	View mView;

	private DojoStorageManager mDsm;
	private DojoClass mClass;
	private HashMap<String, Student> mAttendance;

	public AttendanceListFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_attendance_list, container, false);
		UUID id = (UUID) getArguments().getSerializable(ARGS_CLASSID);
		mDsm = new DojoStorageManager(getContext());
		mClass = mDsm.getDojoManager().getClassById(id);
		mAttendance = mClass.getAttendance();

		mRecycler = (RecyclerView) mView.findViewById(R.id.attendance_list_recyclerView);
		mRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));

		updateUI();

		return mView;
	}

	private void updateUI() {
		if(mDsm.getDojoManager().getStudents() != null) {
			mAdapter = new AttendanceAdapter(mDsm.getDojoManager().getStudents(), mClass.getAttendance());
			mRecycler.setAdapter(mAdapter);
		}
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.fragment_student, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		DojoManager mManager = mDsm.getDojoManager();
		switch (item.getItemId()) {
			case R.id.menu_item_student_save: {
				if (figureAttendance()) {
					getFragmentManager().popBackStack();
					return true;
				} else {
					return false;
				}
			}
			case R.id.menu_item_student_delete: {
				getFragmentManager().popBackStack();
				return true;
			}

			default:
				return super.onOptionsItemSelected(item);
		}
	}

	private boolean figureAttendance() {
		for(int i=0 ; i < mRecycler.getAdapter().getItemCount(); i++) {
			View view = mRecycler.findViewHolderForLayoutPosition(i).itemView;
			TextView idView = (TextView) view.findViewById(R.id.list_item_attendance_id);
			CheckBox isPresent = (CheckBox) view.findViewById(R.id.list_item_attendance_present_cb);
			String uuid = (String) idView.getText();

			Student student = mDsm.getDojoManager().getStudentById(uuid);
			Double currentTimeInRank = student.getRank().getTimeInRank();
			Double classDuration = mClass.getClassDuration();
			// Check if student is marked present or not
			if(isPresent.isChecked()) { // If student is present
				// If student is not part of attendance, add them.
				if(!mAttendance.containsKey(student.getId().toString())) {
					mAttendance.put(student.getId().toString(), student);
					// Add new time in rank to student.
					currentTimeInRank = classDuration > 0 ? currentTimeInRank + classDuration : currentTimeInRank;
					student.getRank().setTimeInRank(currentTimeInRank);
				}
			} else {	// If student is not checked.
				// Check if they are in attendance list
				if(mAttendance.containsKey(student.getId().toString())) {	// They were part of attendance, but are being unchecked.
					// Remove time from student.
					currentTimeInRank = classDuration > 0 ? currentTimeInRank - classDuration : currentTimeInRank;
					student.getRank().setTimeInRank(currentTimeInRank);
					// Remove student from attendance.
					mAttendance.remove(student.getId().toString());
				}
			}
		}
		if(mDsm.writeClasses() && mDsm.writeStudents()) {
			Toast.makeText(getContext(), R.string.attendance_updated, Toast.LENGTH_SHORT).show();
			return true;
		}
		Toast.makeText(getContext(), R.string.attendance_failed, Toast.LENGTH_SHORT).show();
		return false;
	}

	private class AttendanceHolder extends RecyclerView.ViewHolder { //implements View.OnClickListener {
		DojoClass mClass;
		Student mStudent;

		TextView mName, mId;
		CheckBox mIsPresent;

		public AttendanceHolder(View view) {
			super(view);
//			view.setOnClickListener(this);

			mName = (TextView) view.findViewById(R.id.list_item_attendance_name);
			mIsPresent = (CheckBox) view.findViewById(R.id.list_item_attendance_present_cb);
			mId = (TextView) view.findViewById(R.id.list_item_attendance_id);
		}

		public void bindAttendance(HashMap<String, Student> attendance, Student student) {

			mName.setText(student.getFullName());
			mIsPresent.setChecked(mAttendance.containsKey(student.getId().toString()));
			mId.setText(student.getId().toString());
		}

//		@Override
//		public void onClick(View v) {
//			ClassFragment frag = new ClassFragment();
//			Bundle args = new Bundle();
//			args.putSerializable(ARGS_CLASSID, mClass.getClassId());
//			frag.setArguments(args);
//			FragmentManager fm = getFragmentManager();
//			fm.beginTransaction()
//					.replace(R.id.date_class_list_fragmentHolder, frag)
//					.addToBackStack(null)
//					.commit();
//		}
	}

	private class AttendanceAdapter extends RecyclerView.Adapter<AttendanceHolder> {
		private ArrayList<Student> mRoster;
		HashMap<String, Student> mAttendance;

		AttendanceAdapter(ArrayList<Student> students, HashMap<String, Student> attendance) {
			mRoster = students;
			mAttendance = attendance;
		}

		@Override
		public AttendanceHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			LayoutInflater li = LayoutInflater.from(getActivity());
			View view = li.inflate(R.layout.list_item_attendance, parent, false);
			return new AttendanceHolder(view);
		}

		@Override
		public void onBindViewHolder(AttendanceHolder holder, int position) {
			Student thisStudent = mRoster.get(position);
			holder.bindAttendance(mAttendance, thisStudent);
		}

		@Override
		public int getItemCount() {
			return mRoster.size();
		}
	}
}
