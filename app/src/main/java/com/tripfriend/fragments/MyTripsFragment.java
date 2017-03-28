package com.tripfriend.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.tripfriend.R;
import com.tripfriend.adapters.MyTripsAdapter;
import com.tripfriend.model.database.ScheduleProvider;


public class MyTripsFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor> {

    MyTripsAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Prepare the loader. Either re-connect with an existing one,
        // or start a new one.
        Loader<Cursor> loader = getActivity().getSupportLoaderManager().getLoader(0);
        if (loader != null && !loader.isReset()) {
            getActivity().getSupportLoaderManager().restartLoader(0, null, this);
        } else {
            getActivity().getSupportLoaderManager().initLoader(0, null, this);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_trips, container, false);

        // Get id of trip that is expanded
        long selectedId = getActivity().getIntent().getLongExtra("trip_id", -1);

        final ExpandableListView expandableListView = (ExpandableListView) view.findViewById(R.id.fmt_trips);
        mAdapter = new MyTripsAdapter( null, getActivity(), selectedId );
        expandableListView.setAdapter(mAdapter);

        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            // Keep track of previous expanded parent
            int previousGroup = -1;

            @Override
            public void onGroupExpand(int groupPosition) {
                // Collapse previous parent if expanded.
                if ((previousGroup != -1) && (groupPosition != previousGroup)) {
                    expandableListView.collapseGroup(previousGroup);
                }
                previousGroup = groupPosition;
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getLoaderManager().restartLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), ScheduleProvider.CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mAdapter.setGroupCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // This is called when the last Cursor provided to onLoadFinished()
        // is about to be closed.
        int id = loader.getId();
        if (id != 0) {
            // child cursor
            try {
                mAdapter.setChildrenCursor(id, null);
            } catch (NullPointerException e) {
                Log.w("CursorLoader", "Adapter expired, try again on the next query: "
                        + e.getMessage());
            }
        } else {
            mAdapter.setGroupCursor(null);
        }
    }
}
