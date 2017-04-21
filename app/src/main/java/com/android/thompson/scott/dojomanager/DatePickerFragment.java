package com.android.thompson.scott.dojomanager;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Scott on 4/21/2017.
 */

public class DatePickerFragment extends DialogFragment {
	public static final String EXTRA_DATE = "dojomanager.datePicker.dialog";
	private static final String ARG_DATE = "date";

	private DatePicker mDPicker;

	public static DatePickerFragment newInstance(Date date) {
		Bundle args = new Bundle();
		args.putSerializable(ARG_DATE, date);

		DatePickerFragment frag = new DatePickerFragment();
		frag.setArguments(args);

		return frag;
	}

	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Date date = (Date) getArguments().getSerializable(ARG_DATE);

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);

		View v = LayoutInflater.from(getActivity())
				.inflate(R.layout.dialog_datepicker, null);

		mDPicker = (DatePicker) v.findViewById(R.id.dialog_datePicker_datepicker);
		mDPicker.init(year, month, day, null);

		return new AlertDialog.Builder(getActivity())
				.setView(v)
				.setTitle(R.string.student_birthday)
				.setPositiveButton(android.R.string.ok, (new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						int newYear = mDPicker.getYear();
						int newMonth = mDPicker.getMonth();
						int newDay = mDPicker.getDayOfMonth();
						Date newDate = new GregorianCalendar(newYear, newMonth, newDay).getTime();
						sendResult(Activity.RESULT_OK, newDate);
					}
				}))
				.create();
	}

	private void sendResult(int resultCode, Date date) {
		if(getTargetFragment() == null) {
			return;
		}

		Intent intent = new Intent();
		intent.putExtra(EXTRA_DATE, date);
		getTargetFragment()
				.onActivityResult(getTargetRequestCode(), resultCode, intent);
	}
}
