package com.example.ReaRobot;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.ReaRobot.R;

import java.util.List;

public class ListViewAdapter extends BaseAdapter {
    private Context context;
    private List<Patient> patientList;

    public ListViewAdapter(Context context, List<Patient> patientList) {
        this.context = context;
        this.patientList = patientList;
    }

    @Override
    public int getCount() {
        return patientList.size();
    }

    @Override
    public Object getItem(int position) {
        return patientList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.list_item, parent, false);
        }

        TextView fullNameTextView = convertView.findViewById(R.id.fullNameTextView);
        TextView birthDateTextView = convertView.findViewById(R.id.birthDateTextView);
        TextView phoneNumberTextView= convertView.findViewById(R.id.phoneNumberTextView);

        Patient patient = patientList.get(position);

        fullNameTextView.setText("Full Name: " + patient.getFullName());
        birthDateTextView.setText("Birth Date: " + patient.getBirthDate());
        phoneNumberTextView.setText("Phone Number: " + patient.getPhoneNumber());

        return convertView;
    }
}
