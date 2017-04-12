package com.tripfriend.activities;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;

import com.tripfriend.BaseActivity;
import com.tripfriend.R;
import com.tripfriend.fragments.FindFriendS1Fragment;

/**
 * Find Friend activity using google map
 * - Loads google map in fragment
 */
public class FindFriendActivity extends BaseActivity {

    FindFriendS1Fragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Find Friend");    // Not needed

        // Creates a new fragment
        mFragment = new FindFriendS1Fragment();

        // Get fragment manager and inflate it
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.aff_fragment);
        if (fragment == null) {
            fragment = mFragment;
            fragmentManager.beginTransaction()
                    .add(R.id.aff_fragment, fragment)
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

    /**
     * Pass request permissions result into the current frame
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mFragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
