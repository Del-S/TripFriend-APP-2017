package com.tripfriend.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.widget.GridView;

import com.tripfriend.BaseActivity;
import com.tripfriend.R;
import com.tripfriend.adapters.PreferencesAdapter;
import com.tripfriend.fragments.PlanDialogFragment;
import com.tripfriend.fragments.PlanTripS1Fragment;
import com.tripfriend.fragments.PlanTripS2Fragment;
import com.tripfriend.model.Schedule;
import com.tripfriend.model.configuration.Configuration;

public class PlanTripActivity extends BaseActivity {

    PlanTripS1Fragment fragmentS1;
    PlanTripS2Fragment fragmentS2;
    Schedule s = Schedule.getPlanInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Plan Trip");

        switch( s.getState() ) {
            case 1:
                loadStep1Fragment();
                break;
            case 2:
                loadStep2Fragment();
                break;
        }
    }

    private void loadStep1Fragment() {
        fragmentS1 = new PlanTripS1Fragment();

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.apt_fragment);
        if (fragment == null) {
            fragment = fragmentS1;
            fragmentManager.beginTransaction()
                    .add(R.id.apt_fragment, fragment)
                    .commit();
        }
    }

    private void loadStep2Fragment() {
        fragmentS2 = new PlanTripS2Fragment();

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.apt_fragment);
        if (fragment == null) {
            fragment = fragmentS1;
            fragmentManager.beginTransaction()
                    .add(R.id.apt_fragment, fragment)
                    .commit();
        }
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_plan_trip;
    }

    @Override
    protected int getNavigationMenuItemId() {
        return R.id.action_plan_trip;
    }
}
