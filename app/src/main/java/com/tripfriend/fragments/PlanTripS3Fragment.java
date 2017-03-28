package com.tripfriend.fragments;

import android.content.ContentUris;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.tripfriend.R;
import com.tripfriend.activities.MyTripsActivity;
import com.tripfriend.adapters.FriendsAdapter;
import com.tripfriend.model.Schedule;
import com.tripfriend.model.database.ScheduleProvider;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlanTripS3Fragment extends Fragment {

    final Schedule s = Schedule.getPlanInstance();
    FriendsAdapter mAdapter;
    Button mBook;
    EditText mName, mSurname, mEmail, mPhone, mPickup, mNotes;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_plan_trip_3, container, false);

        loadWidgets(view);

        mBook = (Button) view.findViewById(R.id.fpt3_book);
        mBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( !fieldHasErrors(mName) && !fieldHasErrors(mSurname) && !fieldHasErrors(mEmail)
                        && isEmailValid(mEmail) && !fieldHasErrors(mPhone) && !fieldHasErrors(mPickup) ) {
                    s.setName(mName.getText().toString());
                    s.setSurname(mSurname.getText().toString());
                    s.setEmail(mEmail.getText().toString());
                    s.setPhone_number(mPhone.getText().toString());
                    s.setPickup_location(mPickup.getText().toString());
                    s.setNotes(mNotes.getText().toString());
                    s.setState(1);

                    // Save Schedule
                    long idSchedule = s.getId();
                    if(idSchedule > 0) {
                        String mSelectionClause = Schedule.KEY_ID +  " LIKE ?";
                        String[] mSelectionArgs = { Long.toString(idSchedule) };

                        int mRowsUpdated = 0;

                        mRowsUpdated = getActivity().getContentResolver().update(
                                ScheduleProvider.CONTENT_URI,
                                s.getContentValues(),
                                mSelectionClause,
                                mSelectionArgs
                        );
                    } else {
                        Uri uriAdded = getActivity().getContentResolver().insert(
                                ScheduleProvider.CONTENT_URI,
                                s.getContentValues()
                        );

                        idSchedule = ContentUris.parseId(uriAdded);
                    }

                    // Reset schedule instance
                    Schedule.resetPlanInstance();

                    // Finish all fragments before this one
                    getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                    Intent i = new Intent(getActivity(), MyTripsActivity.class);
                    Log.d("P3Frag", String.valueOf(idSchedule));
                    i.putExtra("trip_id", idSchedule);
                    startActivity(i);
                }
            }
        });

        return view;
    }

    private void loadWidgets(View view) {
        mName = (EditText) view.findViewById(R.id.fpt3_name);
        mSurname = (EditText) view.findViewById(R.id.fpt3_surname);
        mEmail = (EditText) view.findViewById(R.id.fpt3_email);
        mPhone = (EditText) view.findViewById(R.id.fpt3_phone);
        mPickup = (EditText) view.findViewById(R.id.fpt3_pickup);
        mNotes = (EditText) view.findViewById(R.id.fpt3_notes);

        mName.setText(s.getName());
        mSurname.setText(s.getSurname());
        mEmail.setText(s.getEmail());
        mPhone.setText(s.getPhone_number());
        mPickup.setText(s.getPickup_location());
        mNotes.setText(s.getNotes());
    }

    private boolean fieldHasErrors(EditText editText) {
        if (editText.getText().toString().isEmpty()) {
            editText.setError("Cannot be empty!");
            return true;
        }
        return false;
    }

    public static boolean isEmailValid(EditText editText) {
        String email = editText.getText().toString();
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        } else {
            editText.setError("Must be email address!");
        }
        return isValid;
    }
}
