package com.tripfriend.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.tripfriend.R;
import com.tripfriend.adapters.holders.FriendHolder;
import com.tripfriend.adapters.holders.PreferenceHolder;
import com.tripfriend.model.Friend;

import java.util.List;

/**
 * Adapter that displays Friends in order process
 */
public class FriendsAdapter extends BaseAdapter {
    private Context mContext;
    private List<Friend> mFriends;

    public FriendsAdapter(Context c, List<Friend> friends) {
        this.mContext = c;
        this.mFriends = friends;
    }

    @Override
    public int getCount() {
        return mFriends.size();
    }

    @Override
    public Object getItem(int i) {
        return mFriends.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        FriendHolder viewHolder;
        if(view == null){
            // Inflate View
            LayoutInflater layoutInflater = (LayoutInflater) mContext.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.row_item_friend, viewGroup, false);

            // Load Friend instance
            Friend f = (Friend) getItem(i);

            // Load ViewHolder and fill the data
            viewHolder = new FriendHolder(view);
            viewHolder.mName.setText( f.getName() );
            int imageId = mContext.getResources().getIdentifier("friend_"+f.getId(), "drawable", mContext.getPackageName());
            viewHolder.imageView.setImageResource(imageId);
            view.setTag(viewHolder);
        }
        else {
            viewHolder = (FriendHolder) view.getTag();
        }

        return view;
    }
}
