package com.example.ReaRobot.Utilities;

import java.util.HashMap;

public class Constants {
    public static final String KEY_COLLECTION_USERS ="users";
    public static final String KEY_FIRST_Name = "first_name";
    public static final String KEY_LAST_Name = "last_name";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_SPECIALITY = "speciality";
    public static final String KEY_PHONE_NUMBER = "phoneNumber";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_PREFERENCE_NAME = "videoMeetingPreference";
    public static final String KEY_IS_SIGNED_IN = "isSignedIn";

    ////

    public static final String KEY_FCM_TOKEN = "fcm_token";
    public static final String REMOTE_MSG_AUTHORIZATION = "Authorization";
    public static final String REMOTE_MSG_CONTENT_TYPE = "Content-Type";

    public static final String REMOTE_MSG_TYPE = "type";
    public static final String REMOTE_MSG_INVITATION = "invitation";
    public static final String REMOTE_MSG_MEETING_TYPE = "meetingType";
    public static final String REMOTE_MSG_INVITER_TOKEN = "inviterToken";
    public static final String REMOTE_MSG_DATA = "data";
    public static final String REMOTE_MSG_REGISTRATION_IDS = "registration_ids";

    public static final String REMOTE_MSG_INVITATION_RESPONSE = "invitationResponse";
    public static final String REMOTE_MSG_INVITATION_ACCEPTED ="accepted";
    public static final String REMOTE_MSG_INVITATION_REJECTED = "rejected";
    public static final String REMOTE_MSG_INVITATION_CANCELLED = "cancelled";
    public static final String REMOTE_MSG_MEETING_ROOM = "meetingRoom";


    public static HashMap<String, String> getRemoteMessageHeader(){
        HashMap<String, String> headers= new HashMap<>();
        headers.put(Constants.REMOTE_MSG_AUTHORIZATION, "Key=AAAAnVCUamQ:APA91bGns_HcJUfxk2ZWHjP03sFzGkJTxucBfZj3dvKugcwQKCf5oCutgbfCQq-Prhg50T3jaQfkd6W604RBcMUU6DwD1noP-8AjmEZfFJA42e7XyFKQxVIPcgXoGuX2IVnqz6eENIyu");
        headers.put(Constants.REMOTE_MSG_CONTENT_TYPE, "application/json");
        return headers;
    }
}
