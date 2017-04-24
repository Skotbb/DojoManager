package com.android.thompson.scott.dojomanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import dojomanager.main.models.Student;
import dojomanager.storage.models.DojoManager;
import dojomanager.storage.models.DojoStorageManager;


public class StudentListFragment extends Fragment {
	private RecyclerView mStudentRecyclerView;
	protected StudentAdapter mAdapter;
	private DojoStorageManager mDsm;

	public StudentListFragment() {
		// Required empty public constructor
	}


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_student_list, container, false);
		mStudentRecyclerView = (RecyclerView) view.findViewById(R.id.student_recycler_view);
		mStudentRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		mDsm = new DojoStorageManager(getContext());
		updateUI();

		FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.student_list_fab);
		fab.setOnClickListener(v -> {
			Student student = new Student();
			DojoManager.getInstance().addStudent(student);
			mDsm.writeStudents();

			Intent intent = StudentPagerActivity.newIntent(getActivity(), student.getId());
			startActivity(intent);
		});

		// Inflate the layout for this fragment
		return view;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onResume() {
		super.onResume();
		updateUI();
	}

	private void updateUI() {
		try {
			if(mDsm == null) {
				mDsm = new DojoStorageManager(getContext());
			}
			ArrayList<Student> students = mDsm.readStudents();
			if(students == null) {
				students = mDsm.getDojoManager().getStudents();
			}
//			if(mAdapter == null) {
				mAdapter = new StudentAdapter(students);
				mStudentRecyclerView.setAdapter(mAdapter);
//			} else {
					// Y U No Work?
//				mAdapter.notifyDataSetChanged();
//			}
		} catch (Exception e) {
			Log.e("Student_List_Frag", e.getMessage());
			e.printStackTrace();
		}
	}


	private class StudentHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		private TextView mStudentName,
				mStudentRank;
		private CheckBox mPaid;
		private Student mStudent;

		public StudentHolder(View itemView) {
			super(itemView);
			itemView.setOnClickListener(this);

			mStudentName = (TextView) itemView.findViewById(R.id.list_item_student_studentName);
			mStudentRank = (TextView) itemView.findViewById(R.id.list_item_student_rank);
			mPaid = (CheckBox) itemView.findViewById(R.id.list_item_isPaid_cb);


		}

		public void bindStudent(Student student) {
			mStudent = student;
			mStudentName.setText(mStudent.getFullName() +", "+ mStudent.getAge());
			mStudentRank.setText(mStudent.getRank().toString());
			mPaid.setChecked(mStudent.isPaidUp());

			mPaid.setOnClickListener(new CheckBox.OnClickListener() {
				@Override
				public void onClick(View v) {
					mStudent.setPaidUp(mPaid.isChecked());
				}
			});
		}

		@Override
		public void onClick(View v) {
//			Toast.makeText(getActivity(), mStudent.getFullName() +" clicked.", Toast.LENGTH_SHORT).show();

			if(mDsm != null) {
				mDsm.writeStudents();
			} else {
				Toast.makeText(getContext(), "Save Failed.", Toast.LENGTH_SHORT).show();
			}
			Intent intent = StudentPagerActivity.newIntent(getActivity(), mStudent.getId());
			startActivity(intent);
		}
	}

	private class StudentAdapter extends RecyclerView.Adapter<StudentHolder> {
		private ArrayList<Student> mStudents;

		public StudentAdapter(ArrayList<Student> students) {mStudents = students;}

		@Override
		public StudentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			LayoutInflater li = LayoutInflater.from(getActivity());
			View view = li.inflate(R.layout.list_item_student, parent, false);
			return new StudentHolder(view);
		}

		@Override
		public void onBindViewHolder(StudentHolder holder, int position) {
			Student student = mStudents.get(position);
			holder.bindStudent(student);
		}

		@Override
		public int getItemCount() {
			return mStudents.size();
		}
	}
}
