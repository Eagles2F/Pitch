package io.evanlee.pitch;

import android.app.Application;

import com.firebase.client.Firebase;

/**
 * Created by evan on 8/2/15.
 */
public class PitchApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //set up firebase
        Firebase.setAndroidContext(this);
    }
}
