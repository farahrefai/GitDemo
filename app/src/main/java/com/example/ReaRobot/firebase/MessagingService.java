package com.example.ReaRobot.firebase;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.ReaRobot.Utilities.Constants;
import com.example.ReaRobot.activities.IncomingInvitationActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MessagingService extends FirebaseMessagingService {
    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        String type = remoteMessage.getData().get(Constants.REMOTE_MSG_TYPE);
        if (type != null) {
            if (type.equals(Constants.REMOTE_MSG_INVITATION)){
                Intent intent= new Intent(getApplicationContext(), IncomingInvitationActivity.class);
                intent.putExtra(Constants.REMOTE_MSG_MEETING_TYPE, remoteMessage.getData().get(Constants.REMOTE_MSG_MEETING_TYPE));
                intent.putExtra(Constants.KEY_FIRST_Name, remoteMessage.getData().get(Constants.KEY_FIRST_Name));
                intent.putExtra(Constants.KEY_LAST_Name, remoteMessage.getData().get(Constants.KEY_LAST_Name));
                intent.putExtra(Constants.KEY_EMAIL, remoteMessage.getData().get(Constants.KEY_EMAIL));
                intent.putExtra(Constants.KEY_PHONE_NUMBER, remoteMessage.getData().get(Constants.KEY_PHONE_NUMBER));
                intent.putExtra(Constants.KEY_SPECIALITY, remoteMessage.getData().get(Constants.KEY_SPECIALITY));
                intent.putExtra(Constants.REMOTE_MSG_INVITER_TOKEN, remoteMessage.getData().get(Constants.REMOTE_MSG_INVITER_TOKEN));
                intent.putExtra(Constants.REMOTE_MSG_MEETING_ROOM, remoteMessage.getData().get(Constants.REMOTE_MSG_MEETING_ROOM));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            } else if (type.equals(Constants.REMOTE_MSG_INVITATION_RESPONSE)){
                Intent intent= new Intent(Constants.REMOTE_MSG_INVITATION_RESPONSE);
                intent.putExtra(Constants.REMOTE_MSG_INVITATION_RESPONSE, remoteMessage.getData().get(Constants.REMOTE_MSG_INVITATION_RESPONSE));
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
            }

        }
    }
}
