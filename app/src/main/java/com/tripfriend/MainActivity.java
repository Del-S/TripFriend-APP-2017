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

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Dummy config data
        LoadConfiguration.loadConfigurationDummy();
        LoadConfiguration.getFriendsDummy();

        Typeface tf = Typeface.createFromAsset(this.getAssets(),"fonts/corporativealt-bold.ttf");

        TextView mTitle = (TextView) findViewById(R.id.am_title);
        mTitle.setTypeface(tf);

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

    @Override
    protected int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected int getNavigationMenuItemId() {
        return R.id.action_home;
    }
}
