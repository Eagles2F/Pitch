package io.evanlee.pitch;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * The activity responsible for browsing the pitching audience
 * Created by evan on 7/26/15.
 */
public class PitchActivity extends Activity {

    public static Intent createIntent(Context context) {
        return new Intent(context, PitchActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pitch);
    }
}
