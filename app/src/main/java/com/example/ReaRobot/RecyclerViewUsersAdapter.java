package com.example.ReaRobot;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ReaRobot.R;

import java.util.List;

public class RecyclerViewUsersAdapter extends RecyclerView.Adapter<RecyclerViewUsersAdapter.UserViewHolder> {

    private List<User> userList;

    public RecyclerViewUsersAdapter(List<User> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);
        holder.fullNameUserTextView.setText("Full Name: " + user.getFullNameUser());
        holder.EmailTextView.setText("Email: " + user.getEmail());
        holder.phoneNumberUserTextView.setText("Phone Number: " + user.getPhoneNumberUser());
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView fullNameUserTextView;
        TextView EmailTextView;
        TextView phoneNumberUserTextView;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            fullNameUserTextView = itemView.findViewById(R.id.fullNameUserTextView);
            EmailTextView = itemView.findViewById(R.id.EmailTextView);
            phoneNumberUserTextView = itemView.findViewById(R.id.phoneNumberUserTextView);
        }
    }
}
