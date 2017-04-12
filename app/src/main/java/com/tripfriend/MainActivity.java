package com.tripfriend;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tripfriend.activities.FindFriendActivity;
import com.tripfriend.activities.PlanTripActivity;
import com.tripfriend.model.configuration.LoadConfiguration;

/**
 * Homescreen activity
 * - Generates configuration
 */
public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Loading dummy config data
        LoadConfiguration.loadConfigurationDummy();
        LoadConfiguration.getFriendsDummy();

        // Titles have different font (payed)
        Typeface tf = Typeface.createFromAsset(this.getAssets(),"fonts/corporativealt-bold.ttf");

        // Load title and set font
        TextView mTitle = (TextView) findViewById(R.id.am_title);
        mTitle.setTypeface(tf);

        // Buttons to redirect
        Button mFindFriend = (Button) findViewById(R.id.am_find_friend);
        mFindFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, FindFriendActivity.class));
            }
        });

        Button mPlanTrip = (Button) findViewById(R.id.am_plan_trip);
        mPlanTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, PlanTripActivity.class));
            }
        });
    }

    /**
     * Get layout content
     *
     * @return layout id
     */
    @Override
    protected int getContentViewId() {
        return R.layout.activity_main;
    }

    /**
     * Get navigation menu id
     *
     * @return
     */
    @Override
    protected int getNavigationMenuItemId() {
        return R.id.action_home;
    }
}
