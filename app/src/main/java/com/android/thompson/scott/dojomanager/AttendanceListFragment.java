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
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
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
	private HashMap<String, Student> mAttendance, newAttendance;

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
		newAttendance = new HashMap<>();

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
		// Running attendance.
		HashMap<String, Student> oldAttendance = new HashMap<>();
		oldAttendance = (HashMap<String, Student>) mAttendance.clone();
		for (String key : oldAttendance.keySet()) {
			Student curStud = mDsm.getDojoManager().getStudentById(oldAttendance.get(key).getId());
			Double duration = mClass.getClassDuration(),
					timeInRank = curStud.getTimeInRank();

			// If student is in old attendance, but not in new, remove them and decrement time.
			if(!newAttendance.containsKey(key)) {
				curStud.setTimeInRank(timeInRank - duration);
				mAttendance.remove(key);
			}
		}
		// New attendance.
		for(String key : newAttendance.keySet()) {
			Student curStud = newAttendance.get(key);
			Double duration = mClass.getClassDuration(),
					timeInRank = curStud.getTimeInRank();

			// If student is in new attendance and not in old, add them and increment time
			if(!mAttendance.containsKey(key)) {
				curStud.setTimeInRank(timeInRank + duration);
				mAttendance.put(key, curStud);
			}
		}
		mDsm.getDojoManager().getClassById(mClass.getClassId()).setAttendance(mAttendance);
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
			mIsPresent.setChecked(attendance.containsKey(student.getId().toString()));
			mIsPresent.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					CheckBox cb = (CheckBox) view;
					// If the student is present
					if (attendance.containsKey(student.getId().toString())) {
						// If the checkbox is still checked
						if(cb.isChecked()) {
							newAttendance.put(student.getId().toString(), student);
						}
					} else {	// If the student is not present
						// If the checkbox is checked
						if(cb.isChecked()) {
							// Add to attendance.
							newAttendance.put(student.getId().toString(), student);
						}
					}
				}
			});
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
