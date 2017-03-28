package com.tripfriend.activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.tripfriend.BaseActivity;
import com.tripfriend.R;
import com.tripfriend.fragments.FindFriendS1Fragment;
import com.tripfriend.fragments.MyTripsFragment;

public class FindFriendActivity extends BaseActivity {

    FindFriendS1Fragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Find Friend");

        mFragment = new FindFriendS1Fragment();

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.aff_frame);
        if (fragment == null) {
            fragment = mFragment;
            fragmentManager.beginTransaction()
                    .add(R.id.aff_frame, fragment)
                    .commit();
        }
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_find_friend;
    }

    @Override
    protected int getNavigationMenuItemId() {
        return R.id.action_find_friend;
    }
}
