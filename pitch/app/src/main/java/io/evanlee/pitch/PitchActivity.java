package io.evanlee.pitch;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.linkedin.platform.DeepLinkHelper;
import com.linkedin.platform.errors.LIDeepLinkError;
import com.linkedin.platform.listeners.DeepLinkListener;

import java.util.ArrayList;
import java.util.List;

import io.evanlee.pitch.Adapter.AudienceListAdapter;
import io.evanlee.pitch.Database.PitchFirebase;
import io.evanlee.pitch.Model.User;
import io.evanlee.pitch.Utils.Session;

/**
 * The activity responsible for browsing the pitching audience
 * Created by evan on 7/26/15.
 */
public class PitchActivity extends Activity implements AudienceListAdapter.peopleClickListener,
                                                    AudienceListAdapter.pitchButtonClickListener {
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
        final Switch audienceSwitch = ((Switch) findViewById(R.id.audience_switch));
        audienceSwitch.setChecked(Session.getInstance().getmCurrentUser().ismPitchable());
        audienceSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                final User user = Session.getInstance().getmCurrentUser();
                user.setmPitchable(b);
                PitchFirebase.usersFirebase.child(user.getmId()).setValue(user);
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.pitch_audience);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        //init user list from firebase
        PitchFirebase.usersFirebase.addChildEventListener(new ChildEventListener() {
            //retrieve new user
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                User user = dataSnapshot.getValue(User.class);
                mUserList.add(user);
                mAdapter.notifyItemInserted(mAdapter.getItemCount()-1);
                Log.d("evan", "user added:" + user.getmName());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.d("evan", "user change");
                User user = dataSnapshot.getValue(User.class);
                if (user.ismPitchable()) {
                    if (!existUser(user)) {
                        mUserList.add(user);
                        mAdapter.notifyItemInserted(mAdapter.getItemCount()-1);
                    }
                } else {
                    if (existUser(user)) {
                        int index =0;
                        for (User oldUser:mUserList) {
                            if (oldUser.getmId().equals(user.getmId())) {
                                index = mUserList.indexOf(oldUser);
                            }
                        }
                        mUserList.remove(index);
                        mAdapter.notifyItemRemoved(index);
                    }
                }
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
        mAdapter = new AudienceListAdapter(mUserList, this, this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }


    @Override
    public void onPeopleClicked(int position) {
        final String id = mUserList.get(position).getmId();
        DeepLinkHelper deepLinkHelper = DeepLinkHelper.getInstance();

        deepLinkHelper.openOtherProfile(this, id, new DeepLinkListener() {
            @Override
            public void onDeepLinkSuccess() {
                Toast.makeText(getApplicationContext(),
                        "Check out the profile, Decide whether to pitch or not!", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onDeepLinkError(LIDeepLinkError error) {
                Toast.makeText(getApplicationContext(),
                        "Having problem with opening the profile!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRealTimePitchClicked(int position) {
        //start the chat room
        startActivity(new Intent(this, ChatActivity.class));
    }

    @Override
    public void onOffLinePitchClicked(int position) {
        //start an email or snap chat or something
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{mUserList.get(position).getmEmail()});
        i.putExtra(Intent.EXTRA_SUBJECT, "A Pitch");
        i.putExtra(Intent.EXTRA_TEXT   , "body of Pitch");
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(PitchActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean existUser(User user) {
        for (User oldUser:mUserList) {
            if (oldUser.getmId().equals(user.getmId())) {
                return true;
            }
        }
        return false;
    }
}
