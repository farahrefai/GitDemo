package com.example.ReaRobot;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    private String fullNameUser;
    private String fullNameUser1;
    private String fullNameUser2;
    private String email;
    private String phoneNumberUser;
    private String speciality;

    public User() {
        // Required empty constructor for Firebase
    }
    protected User(Parcel in) {
        fullNameUser = in.readString();
        email = in.readString();
        phoneNumberUser = in.readString();
        speciality = in.readString();
    }
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(fullNameUser);
        dest.writeString(email);
        dest.writeString(phoneNumberUser);
        dest.writeString(speciality);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };


    public User(String fullName, String email, String phoneNumberUser, String speciality) {
        this.fullNameUser = fullName;
        this.email = email;
        this.phoneNumberUser = phoneNumberUser;
        this.speciality= speciality;
    }

    public String getFullNameUser() {
        return fullNameUser;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumberUser() {
        return phoneNumberUser;
    }
    public String getSpeciality(){return speciality;}
}

