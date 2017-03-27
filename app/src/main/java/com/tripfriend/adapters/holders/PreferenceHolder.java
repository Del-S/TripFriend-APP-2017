package com.tripfriend.adapters.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.tripfriend.R;

public class PreferenceHolder extends RecyclerView.ViewHolder {

    public TextView mTitle;
    public CheckBox mSelected;

    public PreferenceHolder(View itemView) {
        super(itemView);
        mTitle = (TextView) itemView.findViewById(R.id.rip_title);
        mSelected = (CheckBox) itemView.findViewById(R.id.rip_checkbox);
    }
}
