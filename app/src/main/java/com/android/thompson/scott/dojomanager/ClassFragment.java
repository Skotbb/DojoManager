package com.android.thompson.scott.dojomanager;


import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.UUID;

import dojomanager.main.models.DojoClass;
import dojomanager.storage.models.DojoManager;
import dojomanager.storage.models.DojoStorageManager;


/**
 * A simple {@link Fragment} subclass.
 */
public class ClassFragment extends Fragment implements ImageView.OnClickListener {

	DojoStorageManager mDsm;
	DojoClass mClass;
	UUID mClassId;

	DateClassListActivity dad;

	View mView;
	EditText mLabel,
			mDuration;
	Button mAttendance;


	public ClassFragment() {
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
		mView = inflater.inflate(R.layout.fragment_class, container, false);
		mDsm = new DojoStorageManager(getContext());
		mClassId = (UUID) getArguments().getSerializable(DateClassListFragment.ARGS_CLASSID);
		mClass = mDsm.getDojoManager().getClassById(mClassId);
		dad = (DateClassListActivity) getActivity();

		setView();

		return mView;
	}

	private void setView() {
		mLabel = (EditText) mView.findViewById(R.id.class_classLabel_et);
		mDuration = (EditText) mView.findViewById(R.id.class_duration_et);
		mAttendance = (Button) mView.findViewById(R.id.class_takeAttendance_button);

		String tempLabel = mClass != null && mClass.getClassName() != null ? mClass.getClassName() : getString(R.string.class_label);
		dad.setToolBarColor(mClass.getColor());

		getActivity().setTitle(tempLabel);
		setColorViews();
		mLabel.setText(mClass.getClassName());
		mDuration.setText(String.valueOf(mClass.getClassDuration()));
		mLabel.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				getActivity().setTitle(s);
			}
			@Override
			public void afterTextChanged(Editable s) {}
		});
		mAttendance.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				updateClassFromView();
				mDsm.writeClasses();
				Bundle args = new Bundle();
				args.putSerializable(DateClassListFragment.ARGS_CLASSID, mClassId);
				AttendanceListFragment frag = new AttendanceListFragment();
				frag.setArguments(args);

				FragmentManager fm = getFragmentManager();
				fm.beginTransaction()
						.replace(R.id.date_class_list_fragmentHolder, frag)
						.addToBackStack(null)
						.commit();
			}
		});
	}

	private void setColorViews() {
		mView.findViewById(R.id.class_color_red).setOnClickListener(this);
		mView.findViewById(R.id.class_color_orange).setOnClickListener(this);
		mView.findViewById(R.id.class_color_yellow).setOnClickListener(this);
		mView.findViewById(R.id.class_color_green).setOnClickListener(this);
		mView.findViewById(R.id.class_color_blue).setOnClickListener(this);
		mView.findViewById(R.id.class_color_purple).setOnClickListener(this);
		mView.findViewById(R.id.class_color_white).setOnClickListener(this);
	}

	private void updateClassFromView() {
		String tempLabel,
				tempDuration;

		tempLabel = mLabel.getText().toString().trim();
		tempDuration = mDuration.getText().toString().trim();

		mClass.setClassName(tempLabel);
		try {
			mClass.setClassDuration(Double.valueOf(tempDuration));
		} catch (NumberFormatException e) {
			Log.e(this.getTag(), e.getMessage());
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
				updateClassFromView();
				if (mDsm.writeClasses()) {
					Toast.makeText(getContext(), R.string.save, Toast.LENGTH_SHORT).show();
					return true;
				} else {
					Toast.makeText(getContext(), R.string.save_failed, Toast.LENGTH_SHORT).show();
					return false;
				}
			}
			case R.id.menu_item_student_delete: {
				if (mManager.removeClassById(mClass.getClassId())) {
					Toast.makeText(getContext(), R.string.removed, Toast.LENGTH_SHORT).show();
					mDsm.writeClasses();
					mDsm.writeClassDates();
					getFragmentManager().popBackStack();
					return true;
				} else {
					Toast.makeText(getContext(), R.string.save_failed, Toast.LENGTH_SHORT).show();
				}
			}

			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onClick(View view) {
		String color;
		String[] colors = getResources().getStringArray(R.array.array_colorPicker);

		switch (view.getId()) {
			case R.id.class_color_red : {
				color = colors[0];
				mClass.setColor(color);
				dad.setToolBarColor(color);
			} break;
			case R.id.class_color_orange : {
				color = colors[1];
				mClass.setColor(color);
				dad.setToolBarColor(color);
			} break;
			case R.id.class_color_yellow : {
				color = colors[2];
				mClass.setColor(color);
				dad.setToolBarColor(color);
			} break;
			case R.id.class_color_green : {
				color = colors[3];
				mClass.setColor(color);
				dad.setToolBarColor(color);
			} break;
			case R.id.class_color_blue : {
				color = colors[4];
				mClass.setColor(color);
				dad.setToolBarColor(color);
			} break;
			case R.id.class_color_purple : {
				color = colors[5];
				mClass.setColor(color);
				dad.setToolBarColor(color);
			} break;
			case R.id.class_color_white : {
				color = colors[6];
				mClass.setColor(color);
				dad.setToolBarColor(color);
			} break;
		}
	}
}
