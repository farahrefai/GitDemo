package com.example.ReaRobot;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ReaRobot.R;

import java.util.List;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserViewHolder> {

    private List<User> userList;

    public UserListAdapter(List<User> userList) {
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
        holder.fullNameUserTextView.setText(user.getFullNameUser());
        holder.emailTextView.setText(user.getEmail());
        holder.phoneNumberUserTextView.setText(user.getPhoneNumberUser());
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        private TextView fullNameUserTextView;
        private TextView emailTextView;
        private TextView phoneNumberUserTextView;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            fullNameUserTextView = itemView.findViewById(R.id.fullNameUserTextView);
            emailTextView = itemView.findViewById(R.id.EmailTextView);
            phoneNumberUserTextView = itemView.findViewById(R.id.phoneNumberUserTextView);
        }
    }
}

