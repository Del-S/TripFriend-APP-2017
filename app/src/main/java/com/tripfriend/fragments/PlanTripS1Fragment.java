package com.tripfriend.fragments;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.tripfriend.R;
import com.tripfriend.adapters.PreferencesAdapter;
import com.tripfriend.model.Schedule;
import com.tripfriend.model.configuration.Configuration;
import com.tripfriend.model.configuration.LoadConfiguration;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class PlanTripS1Fragment extends Fragment {

    final Configuration c = Configuration.getInstance();
    final Schedule s = Schedule.getPlanInstance();
    LoadConfiguration loadConfiguration;
    Button buttonDate, buttonTime, findTripFriend;
    Calendar scheduledDate;
    PlanDialogFragment dialogFragment;
    TextView mLocation, mLanguage, mTimeSpan;
    Boolean sDate, sTime, sLocation, sLanguage, sTimespan;
    List<String> mPreferences;
    DateFormat df;
    PreferencesAdapter mAdapter;

    int mStackLevel = 0;
    public static final int RC_DIALOG_DATE = 0;
    public static final int RC_DIALOG_TIME = 1;
    public static final int RC_DIALOG_LOCATION = 2;
    public static final int RC_DIALOG_LANGUAGE = 3;
    public static final int RC_DIALOG_TIMESPAN = 4;

    public static PlanTripS1Fragment newInstance() {
        return new PlanTripS1Fragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            mStackLevel = savedInstanceState.getInt("level");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("level", mStackLevel);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_plan_trip, container, false);

        scheduledDate = s.getCalendar_start();
        dialogFragment = new PlanDialogFragment();
        df = new SimpleDateFormat("dd/MM/yyyy");
        mPreferences = c.getPreferences();
        //loadConfiguration = LoadConfiguration.getInstance(getActivity());

        sDate = false;
        sTime = false;
        sLocation = false;
        sLanguage = false;
        sTimespan = false;

        if(scheduledDate == null) {
            scheduledDate = Calendar.getInstance();
        } else {
            sDate = true;
            sTime = true;
        }
        if(s.getLocation() != -1) {
            sLocation = true;
        }
        if(s.getLanguage() != -1) {
            sLanguage = true;
        }
        if(s.getTime_span() != -1) {
            sTimespan = true;
        }

        loadSelects(view);
        loadDateTime(view);

        GridView gridview = (GridView) view.findViewById(R.id.fpt_preferences);
        mAdapter = new PreferencesAdapter(getActivity(), c.getPreferences());
        gridview.setAdapter(mAdapter);

        findTripFriend = (Button) view.findViewById(R.id.fpt_find_friend);
        findTripFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( checkInputs() ) {
                    LoadConfiguration.getFriendsDummy();
                    s.setState(2);

                    // Create new fragment and transaction
                    Fragment newFragment = new PlanTripS2Fragment();
                    android.support.v4.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();

                    transaction.replace(R.id.apt_fragment, newFragment);
                    transaction.addToBackStack(null);

                    transaction.commit();

                    /*try {
                        LoadConfiguration.getFriendsDummy();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }*/
                } else {
                    Toast.makeText(getActivity(), "Error you don't have all data filled (parametrize this)", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private void loadSelects(View view) {
        ConstraintLayout location = (ConstraintLayout) view.findViewById(R.id.fpt_location_wrapper);
        mLocation = (TextView) view.findViewById(R.id.fpt_location);
        if(s.getLocation() != -1) {
            mLocation.setText(c.getLocations().get(s.getLocation()));
        }

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("locations", c.getLocations());
                bundle.putInt("locations_selected", s.getLocation());

                showDialog(RC_DIALOG_LOCATION, bundle);
            }
        });

        ConstraintLayout language = (ConstraintLayout) view.findViewById(R.id.fpt_language_wrapper);
        mLanguage = (TextView) view.findViewById(R.id.fpt_language);
        if(s.getLanguage() != -1) {
            mLanguage.setText(c.getLanguages().get(s.getLanguage()));
        }

        language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("languages", c.getLanguages());
                bundle.putInt("languages_selected", s.getLanguage());

                showDialog(RC_DIALOG_LANGUAGE, bundle);
            }
        });

        ConstraintLayout timeSpan = (ConstraintLayout) view.findViewById(R.id.fpt_timespan_wrapper);
        mTimeSpan = (TextView) view.findViewById(R.id.fpt_timespan);
        if(s.getTime_span() != -1) {
            mTimeSpan.setText(c.getTime_spans().get(s.getTime_span()));
        }

        timeSpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("timespans", c.getTime_spans());
                bundle.putInt("timespans_selected", s.getTime_span());

                showDialog(RC_DIALOG_TIMESPAN, bundle);
            }
        });
    }

    private void loadDateTime(View view) {
        buttonDate = (Button) view.findViewById(R.id.fpt_date);
        buttonTime = (Button) view.findViewById(R.id.fpt_time);
        if(s.getCalendar_start() != null) {
            Integer minute = scheduledDate.get(Calendar.MINUTE);
            Integer hour = scheduledDate.get(Calendar.HOUR);
            String bText = String.format("%02d",hour) + ":" + String.format("%02d",minute);
            buttonTime.setText(bText);
            buttonDate.setText(df.format(scheduledDate.getTime()));
        }

        buttonDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(RC_DIALOG_DATE, null);
            }
        });

        buttonTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(RC_DIALOG_TIME, null);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            Bundle b = data.getExtras();
            int position;
            switch(requestCode) {
                case RC_DIALOG_DATE:
                    scheduledDate.set(Calendar.YEAR, (int) b.get("year"));
                    scheduledDate.set(Calendar.MONTH, (int) b.get("monthOfYear"));
                    scheduledDate.set(Calendar.DAY_OF_MONTH, (int) b.get("dayOfMonth"));
                    s.setCalendar_start(scheduledDate);
                    sDate = true;

                    buttonDate.setText(df.format(scheduledDate.getTime()));
                    break;
                case RC_DIALOG_TIME:
                    int hour = (int) b.get("hourOfDay");
                    int minute = (int) b.get("minute");

                    scheduledDate.set(Calendar.HOUR, hour);
                    scheduledDate.set(Calendar.MINUTE, minute);
                    s.setCalendar_start(scheduledDate);
                    sTime = true;

                    String bText = String.format("%02d", hour) + ":" + String.format("%02d", minute);
                    buttonTime.setText(bText);
                    break;
                case RC_DIALOG_LOCATION:
                    position = (int) b.get("position");
                    s.setLocation(position);
                    sLocation = true;
                    mLocation.setText(c.getLocations().get(position));
                    break;
                case RC_DIALOG_LANGUAGE:
                    position = (int) b.get("position");
                    s.setLanguage(position);
                    sLanguage = true;
                    mLanguage.setText(c.getLanguages().get(position));
                    break;
                case RC_DIALOG_TIMESPAN:
                    position = (int) b.get("position");
                    s.setTime_span(position);
                    sTimespan = true;
                    mTimeSpan.setText(c.getTime_spans().get(position));
                    break;
            }
        }
    }

    private void showDialog(int requestCode, Bundle b) {
        FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
        Fragment prev = getActivity().getSupportFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            //ft.remove(prev);
        }
        ft.addToBackStack(null);

        if( b != null ) {
            dialogFragment.setArguments(b);
        }

        dialogFragment.setTargetFragment(PlanTripS1Fragment.this, requestCode);
        dialogFragment.show(getFragmentManager(), "timePicker");
    }

    private boolean checkInputs() {
        /* Check all inputs */
        if( sDate && sTime && sLocation && sLanguage && sTimespan ) {
            s.setPreferences( mAdapter.getPreferencesSelected() );
            return true;
        }
        if (!sDate) { /* styling error in date */ }
        if (!sTime) { /* styling error in date */ }
        if (!sLocation) { /* styling error in date */ }
        if (!sLanguage) { /* styling error in date */ }
        if (!sTimespan) { /* styling error in date */ }
        return false;
    }

    /*public void completeActivity(JSONObject availableFriendsObject) {
        try {
            JSONArray friendIDArray = availableFriendsObject.getJSONArray("available_friends");
            List<String> availableFriends = loadConfiguration.parseSingleArrayNoID( friendIDArray );

            if(!availableFriends.isEmpty()) {
                schedule.setAvailableFriends(availableFriends);

                Intent intent = new Intent(OrderActivity.this, OrderPickFriendActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(OrderActivity.this, "No friends available for this schedule. (parametrize this)", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }*/

}
