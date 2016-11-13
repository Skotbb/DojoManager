package com.android.thompson.scott.dojomanager;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.NumberPicker;

import static android.R.attr.x;
import static android.os.Build.VERSION_CODES.M;

/**
 * Created by Scott on 11/13/2016.
 */

public class NumberPickerPreference extends DialogPreference {

    private final int DEFAULT_MIN = 1;
    private final int DEFAULT_MAX = 10;
    public final int MAX_VALUE;
    public final int MIN_VALUE;

    public static final boolean WRAP_SELECTOR_WHEEL = true;
    private static final String npTag = "number picker";

    private NumberPicker picker;
    private int value;


    public NumberPickerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        // Set layout resource
        setDialogLayoutResource(R.layout.numberpicker_dialog);
        // Set positive button
        setPositiveButtonText(android.R.string.ok);
        // Set negative button
        setNegativeButtonText(android.R.string.cancel);

        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.NumberPickerPreference);
        MIN_VALUE = a.getInteger(R.styleable.NumberPickerPreference_minValue, DEFAULT_MIN);
        MAX_VALUE = a.getInteger(R.styleable.NumberPickerPreference_maxValue, DEFAULT_MAX);
        setDialogIcon(null);
    }

    @Override
    protected View onCreateDialogView() {
        return super.onCreateDialogView();

    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);

        picker = (NumberPicker) view.findViewById(R.id.pref_numberPicker);
        picker.setMaxValue(MAX_VALUE);
        picker.setMinValue(MIN_VALUE);
        picker.setWrapSelectorWheel(WRAP_SELECTOR_WHEEL);
        picker.setValue(getValue());
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        setValue(restorePersistedValue ? getPersistedInt(MIN_VALUE) : (Integer) defaultValue);
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getInt(index, MIN_VALUE);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        if(positiveResult){
            picker.clearFocus();
            int newVal = picker.getValue();
            Log.d(npTag, newVal + " was selected.");
            if (callChangeListener(newVal)){
                setValue(newVal);
            }
        }
    }

    private void setValue(int value) {
        this.value = value;
        persistInt(this.value);
    }

    private int getValue() {
        return this.value;
    }

}
