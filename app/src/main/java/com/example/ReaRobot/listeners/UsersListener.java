package com.example.ReaRobot.listeners;

import com.example.ReaRobot.models.User;

public interface UsersListener {
    void initiateVideoMeeting(User user);
    void initiateAudioMeeting(User user);
    void onMultipleUsersAction(Boolean isMultipleUsersSelected);
}
