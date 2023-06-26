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

public class JobTypeSpinnerAdapter extends ArrayAdapter<String> {

    private List<String> jobTypeList;
    private LayoutInflater inflater;

    public JobTypeSpinnerAdapter(@NonNull Context context, int resource, @NonNull List<String> jobTypeList) {
        super(context, resource, jobTypeList);
        this.jobTypeList = jobTypeList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return jobTypeList.size();
    }

    @Nullable
    @Override
    public String getItem(int position) {
        return jobTypeList.get(position);
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
            convertView = inflater.inflate(R.layout.item_job_type_spinner, parent, false);
            holder = new ViewHolder();
            holder.jobTypeTextView = convertView.findViewById(R.id.jobTypeTextView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String jobtype = jobTypeList.get(position);
        holder.jobTypeTextView.setText(jobtype);

        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_job_type_dropdown, parent, false);
            holder = new ViewHolder();
            holder.jobTypeTextView = convertView.findViewById(R.id.jobTypeTextView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String jobtype = jobTypeList.get(position);
        holder.jobTypeTextView.setText(jobtype);

        return convertView;
    }

    private static class ViewHolder {
        TextView jobTypeTextView;
    }
}
