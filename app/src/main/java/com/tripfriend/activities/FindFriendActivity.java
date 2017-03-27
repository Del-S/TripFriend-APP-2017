package com.tripfriend.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.tripfriend.BaseActivity;
import com.tripfriend.R;

public class FindFriendActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Find Friend");
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
