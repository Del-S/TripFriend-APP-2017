package com.tripfriend.adapters.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tripfriend.R;

public class FriendHolder extends RecyclerView.ViewHolder {

    public TextView mName;
    public LinearLayout mLanguages;

    public FriendHolder(View itemView) {
        super(itemView);
        mName = (TextView) itemView.findViewById(R.id.rif_friend_name);
        mLanguages = (LinearLayout) itemView.findViewById(R.id.rif_languages);
    }
}
