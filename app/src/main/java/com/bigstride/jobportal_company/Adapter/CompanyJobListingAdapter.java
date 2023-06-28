package com.bigstride.jobportal_company.Adapter;

import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bigstride.jobportal_company.Model.CompanyJobListingModel;
import com.bigstride.jobportal_company.R;
import java.util.List;

public class CompanyJobListingAdapter extends RecyclerView.Adapter<CompanyJobListingAdapter.ViewHolder> {

    private List<CompanyJobListingModel> companyJobListingList;
    private OnItemClickListener itemClickListener;
    private OnItemLongClickListener itemLongClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.itemLongClickListener = listener;
    }

    public CompanyJobListingAdapter(List<CompanyJobListingModel> companyJobListingList) {
        this.companyJobListingList = companyJobListingList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_company_job_listing, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CompanyJobListingModel jobListing = companyJobListingList.get(position);

        holder.jobPositionTextView.setText(jobListing.getJob_position());
        holder.startingDateTextView.setText(jobListing.getStarting_date());
        holder.applyBeforeDateTextView.setText(jobListing.getApply_before_date());
    }

    @Override
    public int getItemCount() {
        return companyJobListingList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView jobPositionTextView;
        TextView startingDateTextView;
        TextView applyBeforeDateTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            jobPositionTextView = itemView.findViewById(R.id.jobPositionTextView);
            startingDateTextView = itemView.findViewById(R.id.startingDateTextView);
            applyBeforeDateTextView = itemView.findViewById(R.id.applyBeforeDateTextView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.performHapticFeedback(HapticFeedbackConstants.CONFIRM);
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && itemClickListener != null) {
                        itemClickListener.onItemClick(position);
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && itemLongClickListener != null) {
                        itemLongClickListener.onItemLongClick(position);
                        return true;
                    }
                    return false;
                }
            });
        }
    }
}
