package com.infroid.samarpan;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DatePickerFragment extends DialogFragment {

    int year, month, day;
    DatePickerDialog.OnDateSetListener ondataSet;

    public DatePickerFragment() {

    }

    public void setCallback(DatePickerDialog.OnDateSetListener ondate) {
        ondataSet = ondate;
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        year = args.getInt("year");
        month = args.getInt("month");
        day = args.getInt("day");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        return new DatePickerDialog(getActivity(), ondataSet, year, month, day);
    }
}
