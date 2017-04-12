package com.tripfriend.adapters.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tripfriend.R;

/**
 * Holder for Friend intance
 * - Image with face
 * - Name as text
 * - Language flags
 */
public class FriendHolder extends RecyclerView.ViewHolder {

    public TextView mName;
    public LinearLayout mLanguages;
    public ImageView imageView;

    public FriendHolder(View itemView) {
        super(itemView);
        mName = (TextView) itemView.findViewById(R.id.rif_friend_name);
        mLanguages = (LinearLayout) itemView.findViewById(R.id.rif_languages);
        imageView = (ImageView) itemView.findViewById(R.id.rif_friend_image);
    }
}
