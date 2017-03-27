package com.tripfriend.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.tripfriend.R;
import com.tripfriend.adapters.holders.PreferenceHolder;

import java.util.ArrayList;
import java.util.List;

public class PreferencesAdapter extends BaseAdapter {

    private Context mContext;
    private List<String> mPreferences;
    private List<String> mPreferencesSelected;

    public PreferencesAdapter(Context c, List<String> preferences) {
        this.mContext = c;
        this.mPreferences = preferences;
        this.mPreferencesSelected = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return mPreferences.size();
    }

    @Override
    public Object getItem(int i) {
        return mPreferences.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        PreferenceHolder viewHolder;
        if(view == null){
            LayoutInflater layoutInflater = (LayoutInflater) mContext.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            view = layoutInflater.inflate(R.layout.row_item_preferences, viewGroup, false);
            viewHolder = new PreferenceHolder(view);

            final CheckBox ch = viewHolder.mSelected;
            final TextView title = viewHolder.mTitle;

            title.setText( getItem(i).toString() );
            ch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if ( ch.isChecked() ) {
                        mPreferencesSelected.add(title.getText().toString());
                    } else {
                        mPreferencesSelected.remove(title.getText().toString());
                    }
                }
            });
            view.setTag(viewHolder);
        }
        else {
            viewHolder = (PreferenceHolder) view.getTag();
        }

        return view;
    }

    public List<String> getPreferencesSelected() {
        return mPreferencesSelected;
    }
}
