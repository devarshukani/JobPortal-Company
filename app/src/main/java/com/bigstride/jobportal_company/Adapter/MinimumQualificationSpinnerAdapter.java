package com.bigstride.jobportal_company.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.bigstride.jobportal_company.R;

import java.util.List;

public class MinimumQualificationSpinnerAdapter extends ArrayAdapter<String> {

    private List<String> qualificationList;
    private LayoutInflater inflater;

    public MinimumQualificationSpinnerAdapter(@NonNull Context context, int resource, @NonNull List<String> qualificationList) {
        super(context, resource, qualificationList);
        this.qualificationList = qualificationList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return qualificationList.size();
    }

    @Nullable
    @Override
    public String getItem(int position) {
        return qualificationList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_minimum_qualification_spinner, parent, false);
            holder = new ViewHolder();
            holder.qualificationTextView = convertView.findViewById(R.id.qualificationTextView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String quali = qualificationList.get(position);
        holder.qualificationTextView.setText(quali);

        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_minimum_qualification_dropdown, parent, false);
            holder = new ViewHolder();
            holder.qualificationTextView = convertView.findViewById(R.id.qualificationTextView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String quali = qualificationList.get(position);
        holder.qualificationTextView.setText(quali);

        return convertView;
    }

    private static class ViewHolder {
        TextView qualificationTextView;
    }
}
