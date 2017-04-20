package com.android.thompson.scott.dojomanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import dojomanager.main.models.Student;
import dojomanager.storage.models.DojoStorageManager;


public class StudentListFragment extends Fragment {
	private RecyclerView mStudentRecyclerView;
	private StudentAdapter mAdapter;

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

		FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.student_list_fab);
		fab.setOnClickListener(v -> {
			Student student = new Student();
			DojoStorageManager.getInstance().addStudent(student);

			Intent intent = StudentPagerActivity.newIntent(getActivity(), student.getId());
			startActivity(intent);
		});

		updateUI();
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

	private void updateUI() {
		DojoStorageManager dsm = DojoStorageManager.getInstance();
		List<Student> students = dsm.getStudents();

		if(mAdapter == null) {
			mAdapter = new StudentAdapter(students);
			mStudentRecyclerView.setAdapter(mAdapter);
		} else {
			mAdapter.notifyDataSetChanged();
		}
	}


	private class StudentHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		private TextView mStudentName,
				mStudentRank;
		private Student mStudent;

		public StudentHolder(View itemView) {
			super(itemView);
			itemView.setOnClickListener(this);

			mStudentName = (TextView) itemView.findViewById(R.id.list_item_student_studentName);
			mStudentRank = (TextView) itemView.findViewById(R.id.list_item_student_rank);
		}

		public void bindStudent(Student student) {
			mStudent = student;
			mStudentName.setText(mStudent.getFullName() +", "+ mStudent.getAge());
			mStudentRank.setText(mStudent.getRank().toString());
		}

		@Override
		public void onClick(View v) {
			Toast.makeText(getActivity(), mStudent.getFullName() +" clicked.", Toast.LENGTH_SHORT).show();

			Intent intent = StudentPagerActivity.newIntent(getActivity(), mStudent.getId());
			startActivity(intent);
		}
	}

	private class StudentAdapter extends RecyclerView.Adapter<StudentHolder> {
		private List<Student> mStudents;

		public StudentAdapter(List<Student> students) {mStudents = students;}

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
