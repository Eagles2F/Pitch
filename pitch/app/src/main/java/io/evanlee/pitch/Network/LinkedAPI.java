package io.evanlee.pitch.Network;

/**
 * Created by evan on 7/26/15.
 */
public class LinkedAPI {
    public static final String JSON_ID = "id";
    public static final String JSON_NAME = "formattedName";

    public static final String host = "api.linkedin.com";
    public static final String basicUserInfoUrl = "https://" + host + "/v1/people/~:(id,formatted-name)";
    public static final String shareUrl = "https://" + host + "/v1/people/~/shares";
}
