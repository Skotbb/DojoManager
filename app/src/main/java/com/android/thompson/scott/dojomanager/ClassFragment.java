package com.android.thompson.scott.dojomanager;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.UUID;

import dojomanager.main.models.DojoClass;
import dojomanager.storage.models.DojoManager;
import dojomanager.storage.models.DojoStorageManager;


/**
 * A simple {@link Fragment} subclass.
 */
public class ClassFragment extends Fragment {

	DojoStorageManager mDsm;
	DojoClass mClass;
	UUID mClassId;

	View mView;
	EditText mLabel,
			mDuration;

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

		setView();

		return mView;
	}

	private void setView() {
		mLabel = (EditText) mView.findViewById(R.id.class_classLabel_et);
		mDuration = (EditText) mView.findViewById(R.id.class_duration_et);
		String tempLabel = mClass != null && mClass.getClassName() != null ? mClass.getClassName() : getString(R.string.class_label);

		getActivity().setTitle(tempLabel);
		mLabel.setText(mClass.getClassName());
		mLabel.setOnEditorActionListener(new EditText.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				getActivity().setTitle(v.getText());
				return true;
			}
		});
		mDuration.setText(String.valueOf(mClass.getClassDuration()));
	}

	private void updateClassFromView() {
		String tempLabel,
				tempDuration;

		tempLabel = mLabel.getText().toString();
		tempDuration = mDuration.getText().toString();

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
				if(mDsm.writeClasses()) {
					Toast.makeText(getContext(), R.string.save, Toast.LENGTH_SHORT).show();
					return true;
				} else {
					Toast.makeText(getContext(), R.string.save_failed, Toast.LENGTH_SHORT).show();
					return false;
				}
			}
			case R.id.menu_item_student_delete : {
				if(mManager.removeClassById(mClass.getClassId())) {
					Toast.makeText(getContext(), R.string.removed, Toast.LENGTH_SHORT).show();
					mDsm.writeClasses();
					mDsm.writeClassDates();
					getFragmentManager().popBackStack();
					return true;
				} else {
					Toast.makeText(getContext(), R.string.save_failed, Toast.LENGTH_SHORT).show();
				}
			}

			default: return super.onOptionsItemSelected(item);
		}
	}
}
