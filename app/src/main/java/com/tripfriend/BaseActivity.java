package com.tripfriend;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.tripfriend.activities.FindFriendActivity;
import com.tripfriend.activities.MyTripsActivity;
import com.tripfriend.activities.PlanTripActivity;
import com.tripfriend.utils.BottomNavigationViewHelper;

import java.util.Locale;

public abstract class BaseActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    public static final Locale APP_LOCALE = new Locale("en");
    public static final String DATE_HOUR_FORMAT = "dd/MM/yyyy HH:mm";
    protected BottomNavigationView navigationView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());

        // Load bottom navigation and add this as a listener
        navigationView = (BottomNavigationView) findViewById(R.id.bottom_menu);
        BottomNavigationViewHelper.disableShiftMode(navigationView);
        navigationView.setOnNavigationItemSelectedListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Updates bottom menu and highlights the active menu item
        updateNavigationBarState();
    }

    @Override
    public void onPause() {
        super.onPause();
        // Remove inter-activity transition to avoid screen tossing on tapping bottom navigation items
        overridePendingTransition(0, 0);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull final MenuItem item) {
        final Context c = this;
        navigationView.postDelayed(new Runnable() {
            @Override
            public void run() {
                int itemId = item.getItemId();
                if( itemId == R.id.action_more ) {
                    Toast.makeText(c, R.string.g_not_implemented, Toast.LENGTH_SHORT).show();
                } else {
                    if (itemId == R.id.action_home) {
                        startActivity(new Intent(c, MainActivity.class));
                    } else if (itemId == R.id.action_find_friend) {
                        startActivity(new Intent(c, FindFriendActivity.class));
                    } else if (itemId == R.id.action_plan_trip) {
                        startActivity(new Intent(c, PlanTripActivity.class));
                    } else if (itemId == R.id.action_my_trips) {
                        startActivity(new Intent(c, MyTripsActivity.class));
                    }
                    finish();
                }
            }
        }, 100);
        return true;
    }

    /**
     * Updates bottom menu and highlights the active menu item
     */
    private void updateNavigationBarState(){
        int actionId = getNavigationMenuItemId();
        selectBottomNavigationBarItem(actionId);
    }

    /**
     * Sets which bottom menu item is active (checked)
     *
     * @param itemId item id in menu
     */
    void selectBottomNavigationBarItem(int itemId) {
        Menu menu = navigationView.getMenu();
        for (int i = 0, size = menu.size(); i < size; i++) {
            MenuItem item = menu.getItem(i);
            boolean shouldBeChecked = item.getItemId() == itemId;
            if (shouldBeChecked) {
                item.setChecked(true);
                break;
            }
        }
    }

    /**
     * Get view (layout) id from child activities
     *
     * @return int layout id
     */
    protected abstract int getContentViewId();

    /**
     * Get position id in the menu (of child activity)
     *
     * @return int menu position id
     */
    protected abstract int getNavigationMenuItemId();
}
