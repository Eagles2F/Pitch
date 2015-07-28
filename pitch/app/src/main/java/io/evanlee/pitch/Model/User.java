package io.evanlee.pitch.Model;

import org.json.JSONException;
import org.json.JSONObject;

import io.evanlee.pitch.Network.LinkedAPI;

/**
 * Created by evan on 7/26/15.
 */
public class User {
    private String mId;
    private String mName;

    public User() {

    }

    public User(String id, String name) {
        mId = id;
        mName = name;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public static User parse(JSONObject jsonObject) {
        try {
            return new User(jsonObject.getString(LinkedAPI.JSON_ID),
                    jsonObject.getString(LinkedAPI.JSON_NAME));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
