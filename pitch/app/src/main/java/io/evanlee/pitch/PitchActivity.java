package io.evanlee.pitch;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;

import java.util.ArrayList;
import java.util.List;

import io.evanlee.pitch.Adapter.AudienceListAdapter;
import io.evanlee.pitch.Database.PitchFirebase;
import io.evanlee.pitch.Model.User;

/**
 * The activity responsible for browsing the pitching audience
 * Created by evan on 7/26/15.
 */
public class PitchActivity extends Activity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<User> mUserList = new ArrayList<>();

    public static Intent createIntent(Context context) {
        return new Intent(context, PitchActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pitch);

        mRecyclerView = (RecyclerView) findViewById(R.id.pitch_audience);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        //init user list from firebase
        PitchFirebase.usersFirebase.addChildEventListener(new ChildEventListener() {
            //retrieve new user
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                User user = dataSnapshot.getValue(User.class);
                mUserList.add(user);
                Log.d("evan", "user added:" + user.getmName());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        // specify an adapter (see also next example)
        mAdapter = new AudienceListAdapter(mUserList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }
}
