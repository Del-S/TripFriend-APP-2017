package com.tripfriend.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.tripfriend.utils.ArrayMapSeriabilize;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class PlanDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    final Calendar c = Calendar.getInstance();
    ArrayMapSeriabilize<Integer, String> list;
    Integer[] keySet;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        CharSequence[] cs = new CharSequence[0];
        int position;

        // Create dialog type based on type
        switch (getTargetRequestCode()) {
            case 0:
                return createDatePickerDialog();
            case 1:
                return createTimePickerDialog();
            case 2:
                list = (ArrayMapSeriabilize<Integer, String>) bundle.get("locations");
                position = (int) bundle.get("locations_selected");
                if( list != null ) {
                    keySet = list.keySet().toArray(new Integer[list.size()]);
                    cs = list.values().toArray(new CharSequence[list.size()]);
                    if( position != -1 ) position = getKeyIndex(position);
                }

                return createPickerDialog("Select Location", cs, position);
            case 3:
                list = (ArrayMapSeriabilize<Integer, String>) bundle.get("languages");
                position = (int) bundle.get("languages_selected");
                if( list != null ) {
                    keySet = list.keySet().toArray(new Integer[list.size()]);
                    cs = list.values().toArray(new CharSequence[list.size()]);
                    if( position != -1 ) position = getKeyIndex(position);
                }

                return createPickerDialog("Select Language", cs, position);
            case 4:
                list = (ArrayMapSeriabilize<Integer, String>) bundle.get("timespans");
                position = (int) bundle.get("timespans_selected");
                if( list != null ) {
                    keySet = list.keySet().toArray(new Integer[list.size()]);
                    cs = list.values().toArray(new CharSequence[list.size()]);
                    if( position != -1 ) position = getKeyIndex(position);
                }

                return createPickerDialog("Select Location", cs, position);
            default:
                return super.onCreateDialog(savedInstanceState);
        }
    }

    /**
     * Gets what position should be active
     *
     * @param position to check
     * @return position index
     */
    private int getKeyIndex(int position) {
        int index = 0;
        for(int key : keySet) {
            if(position == key) { return index; }
            ++index;
        }
        return -1;
    }

    /**
     * Creates picker for date
     *
     * @return Dialog instance
     */
    public Dialog createDatePickerDialog() {
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

        return datePickerDialog;
    }

    /**
     * Creates Time Picker
     *
     * @return Dialog instance
     */
    public Dialog createTimePickerDialog() {
        int hour = c.get(Calendar.HOUR);
        int minute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), this, hour, minute, true);
        return timePickerDialog;
    }

    /**
     * Creates radio button picker dialog (only one can be selected)
     *
     * @param title of the dialog
     * @param cs items to select from
     * @param position selected position
     * @return Dialog instance
     */
    public Dialog createPickerDialog(String title, CharSequence[] cs, int position) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle(title);
        dialog.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_CANCELED, getActivity().getIntent());
            }
        });

        dialog.setSingleChoiceItems(cs, position, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = getActivity().getIntent();
                i.putExtra("position", keySet[which]);
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, getActivity().getIntent());
                dialog.dismiss();
            }
        });
        return dialog.create();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Intent i = getActivity().getIntent();
        i.putExtra("year", year);
        i.putExtra("monthOfYear", monthOfYear);
        i.putExtra("dayOfMonth", dayOfMonth);
        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, getActivity().getIntent());
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Intent i = getActivity().getIntent();
        i.putExtra("hourOfDay", hourOfDay);
        i.putExtra("minute", minute);
        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, getActivity().getIntent());
    }
}
