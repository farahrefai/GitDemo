package com.example.ReaRobot;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class ListViewUsersAdapter extends BaseAdapter {
    private Context context;
    private List<User> userList;

    public ListViewUsersAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    @Override
    public int getCount() {
        return userList.size();
    }

    @Override
    public Object getItem(int position) {
        return userList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.item_user, parent, false);
        }

        TextView fullNameTextView = convertView.findViewById(R.id.fullNameUserTextView);
        TextView EmailTextView = convertView.findViewById(R.id.EmailTextView);
        TextView phoneNumberTextView= convertView.findViewById(R.id.phoneNumberUserTextView);

        User user = userList.get(position);

        fullNameTextView.setText("Full Name: " + user.getFullNameUser());
        EmailTextView.setText("Email: " + user.getEmail());
        phoneNumberTextView.setText("Phone Number: " + user.getPhoneNumberUser());

        return convertView;
    }
}
