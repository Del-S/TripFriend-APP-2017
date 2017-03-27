package com.tripfriend.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.tripfriend.R;
import com.tripfriend.adapters.FriendsAdapter;
import com.tripfriend.model.Friend;
import com.tripfriend.model.Schedule;
import com.tripfriend.model.configuration.Configuration;

public class PlanTripS2Fragment extends Fragment {

    final Configuration c = Configuration.getInstance();
    final Schedule s = Schedule.getPlanInstance();
    FriendsAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_plan_trip_2, container, false);

        GridView gridview = (GridView) view.findViewById(R.id.fpt2_friends);
        mAdapter = new FriendsAdapter(getActivity(), c.getFriends());
        gridview.setAdapter(mAdapter);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Friend f = (Friend) mAdapter.getItem(i);
                s.setId_friend(f.getId());
                s.setState(3);

                Fragment newFragment = new PlanTripS3Fragment();
                android.support.v4.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();

                transaction.replace(R.id.apt_fragment, newFragment);
                transaction.addToBackStack(null);

                transaction.commit();
            }
        });

        return view;
    }

}
