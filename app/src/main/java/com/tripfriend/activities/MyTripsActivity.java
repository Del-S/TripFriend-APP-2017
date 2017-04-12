package com.tripfriend.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.tripfriend.BaseActivity;
import com.tripfriend.R;
import com.tripfriend.fragments.MyTripsFragment;

/**
 * Displays all user trips (past and future)
 */
public class MyTripsActivity extends BaseActivity {

    public Fragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("My Trips");       // Not needed

        // Creates a new fragment
        mFragment = new MyTripsFragment();

        // Get fragment manager and inflate it
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.amt_fragment);
        if (fragment == null) {
            fragment = mFragment;
            fragmentManager.beginTransaction()
                    .add(R.id.amt_fragment, fragment)
                    .commit();
        }
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_my_trips;
    }

    @Override
    protected int getNavigationMenuItemId() {
        return R.id.action_my_trips;
    }
}
