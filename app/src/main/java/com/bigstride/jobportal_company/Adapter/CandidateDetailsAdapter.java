package com.bigstride.jobportal_company.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bigstride.jobportal_company.Model.CandidateDetailsModel;
import com.bigstride.jobportal_company.R;
import java.util.List;

public class CandidateDetailsAdapter extends RecyclerView.Adapter<CandidateDetailsAdapter.ViewHolder> {

    private List<CandidateDetailsModel> candidateDetailsList;
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

    public CandidateDetailsAdapter(List<CandidateDetailsModel> candidateDetailsList) {
        this.candidateDetailsList = candidateDetailsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_canidate_details_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CandidateDetailsModel candidateDetails = candidateDetailsList.get(position);

        holder.candidateNameTextViewCD.setText(candidateDetails.getFull_name());
        holder.candidateContactTextViewCD.setText(candidateDetails.getContact_no());
        holder.candidateEmailTextViewCD.setText(candidateDetails.getEmail());
    }

    @Override
    public int getItemCount() {
        return candidateDetailsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView candidateNameTextViewCD;
        TextView candidateContactTextViewCD;
        TextView candidateEmailTextViewCD;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            candidateNameTextViewCD = itemView.findViewById(R.id.candidateNameTextViewCD);
            candidateContactTextViewCD = itemView.findViewById(R.id.candidateContactTextViewCD);
            candidateEmailTextViewCD = itemView.findViewById(R.id.candidateEmailTextViewCD);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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
