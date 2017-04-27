package com.android.thompson.scott.dojomanager;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.Calendar;
import java.util.Date;

import dojomanager.storage.models.DojoStorageManager;


/**
 * A simple {@link Fragment} subclass.
 */
public class ClassCalendar extends Fragment {
	public static final String EXTRA_DATE_CLASS = "thompson.dojo.manager.dateClass";

	MaterialCalendarView mCalendar;
	DojoStorageManager mDsm;

	public ClassCalendar() {
		// Required empty public constructor
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_class_calendar, container, false);

		mDsm = new DojoStorageManager(getContext());
		mCalendar = (MaterialCalendarView) view.findViewById(R.id.class_calendar_calendarView);
		setCalendar();


		return view;
	}

	private void setCalendar() {
		mCalendar.setOnDateChangedListener(new OnDateSelectedListener() {
			@Override
			public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
				Toast.makeText(getActivity(), date.toString(), Toast.LENGTH_SHORT).show();
				Bundle args = new Bundle();
				args.putParcelable(EXTRA_DATE_CLASS, date);
				Intent intent = new Intent(getContext(), DateClassListActivity.class);
				intent.putExtra(EXTRA_DATE_CLASS, args);
				startActivity(intent);
			}
		});
		mCalendar.setCurrentDate(Calendar.getInstance());
		mCalendar.addDecorator(new DayViewDecorator() {
			@Override
			public boolean shouldDecorate(CalendarDay day) {
				Date temp = day.getDate();
				return mDsm.getDojoManager().getDates().contains(temp);
			}

			@Override
			public void decorate(DayViewFacade view) {
				view.setBackgroundDrawable(getResources().getDrawable(R.drawable.border));
			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();
		mDsm.readAllData();
		mCalendar.invalidateDecorators();
	}
}
