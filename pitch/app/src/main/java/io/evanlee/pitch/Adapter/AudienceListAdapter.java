package io.evanlee.pitch.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.evanlee.pitch.Model.User;
import io.evanlee.pitch.R;

/**
 * Created by evan on 7/26/15.
 */
public class AudienceListAdapter extends RecyclerView.Adapter<AudienceListAdapter.ViewHolder> {
    private List<User> audienceList;

    public AudienceListAdapter(List<User> userList) {
        audienceList = userList;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public AudienceListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        // create a new view
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.pitch_list_item, viewGroup, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        viewHolder.mTextView.setText(audienceList.get(i).getmName());
    }

    @Override
    public int getItemCount() {
        return audienceList.size();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;
        public ViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.name);
        }
    }
}
