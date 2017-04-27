package com.android.thompson.scott.dojomanager;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.Date;
import java.util.HashSet;

import dojomanager.main.models.DojoClass;
import dojomanager.storage.models.DojoStorageManager;

/**
 * A placeholder fragment containing a simple view.
 */
public class DateClassListFragment extends Fragment {
	public static final String ARGS_CLASSID = "dojo.manager.classId";

	HashSet<DojoClass> mClasses;
	DojoClass mClass;
	DojoStorageManager mDsm;
	CalendarDay mDate;

	RecyclerView mRecycler;
	DojoClassAdapter mAdapter;

	public DateClassListFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_date_class_list, container, false);
		mRecycler = (RecyclerView) view.findViewById(R.id.date_class_list_recyclerView);
		mRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));

		mDate = getArguments().getParcelable(ClassCalendar.EXTRA_DATE_CLASS);
		mDsm = new DojoStorageManager(getContext());

		Date tempDate = mDate.getDate();
		if(mDsm.getDojoManager().getClasses().containsKey(tempDate.toString())) {
			mClasses = mDsm.getDojoManager().getClasses().get(tempDate.toString());
		}

		FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.date_class_newClass_fab);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				DojoClass newClass = new DojoClass(mDate.getDate());
				mDsm.getDojoManager().addClass(newClass);
				if(mDsm.writeClasses() && mDsm.writeClassDates()) {
					Toast.makeText(getContext(), R.string.save, Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getContext(), R.string.save_failed, Toast.LENGTH_SHORT).show();
				}
				ClassFragment frag = new ClassFragment();
				Bundle args = new Bundle();
				args.putSerializable(ARGS_CLASSID, newClass.getClassId());
				frag.setArguments(args);

				FragmentManager fm = getFragmentManager();
				fm.beginTransaction()
						.replace(R.id.date_class_list_fragmentHolder, frag)
						.addToBackStack(null)
						.commit();
			}
		});
		updateUI();
		return view;
	}

	private void updateUI() {
		if(mClasses != null) {
			DojoClass[] temp = mClasses.toArray(new DojoClass[mClasses.size()]);
			mAdapter = new DojoClassAdapter(temp);
			mRecycler.setAdapter(mAdapter);
		}
	}

	private class DojoClassHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		DojoClass mClass;

		TextView mLabel,
				mStudentCount,
				mDuration;

		public DojoClassHolder(View itemView) {
			super(itemView);
			itemView.setOnClickListener(this);

			mLabel = (TextView) itemView.findViewById(R.id.list_item_class_label);
			mStudentCount = (TextView) itemView.findViewById(R.id.list_item_class_students);
			mDuration = (TextView) itemView.findViewById(R.id.list_item_class_duration);
		}

		public void bindClass(DojoClass dojoClass) {
			mClass = dojoClass;
			mLabel.setText(mClass.getClassName());
			mStudentCount.setText(getString(R.string.dojoClass_studentCount) + " " + String.valueOf(mClass.getAttendance().size()));
			mDuration.setText(String.valueOf(mClass.getClassDuration()) + " hrs");
		}

		@Override
		public void onClick(View v) {
			ClassFragment frag = new ClassFragment();
			Bundle args = new Bundle();
			args.putSerializable(ARGS_CLASSID, mClass.getClassId());
			frag.setArguments(args);
			FragmentManager fm = getFragmentManager();
			fm.beginTransaction()
					.replace(R.id.date_class_list_fragmentHolder, frag)
					.addToBackStack(null)
					.commit();
		}
	}

	private class DojoClassAdapter extends RecyclerView.Adapter<DojoClassHolder> {
		private DojoClass[] mClasses;

		DojoClassAdapter(DojoClass[] classes) {mClasses = classes;}

		@Override
		public DojoClassHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			LayoutInflater li = LayoutInflater.from(getActivity());
			View view = li.inflate(R.layout.list_item_class, parent, false);
			return new DojoClassHolder(view);
		}

		@Override
		public void onBindViewHolder(DojoClassHolder holder, int position) {
			DojoClass thisClass = mClasses[position];
			holder.bindClass(thisClass);
		}

		@Override
		public int getItemCount() {
			return mClasses.length;
		}
	}
}
