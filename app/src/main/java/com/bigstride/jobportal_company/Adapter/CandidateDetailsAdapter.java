package com.bigstride.jobportal_company.Adapter;

import android.net.Uri;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bigstride.jobportal_company.Model.CandidateDetailsModel;
import com.bigstride.jobportal_company.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class CandidateDetailsAdapter extends RecyclerView.Adapter<CandidateDetailsAdapter.ViewHolder> {

    private List<CandidateDetailsModel> candidateDetailsList;
    private OnItemClickListener itemClickListener;
    private OnItemLongClickListener itemLongClickListener;
    private android.content.Context context;

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

    public CandidateDetailsAdapter(List<CandidateDetailsModel> candidateDetailsList, android.content.Context context) {
        this.candidateDetailsList = candidateDetailsList;
        this.context = context;
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
        holder.candidateStatusTextViewCD.setText(candidateDetails.getApplication_status());

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference imageRef = storageRef.child("ProfileImage/" + candidateDetails.getUser_id() + ".png");


        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri downloadUrl) {
                Glide.with(context)
                        .load(downloadUrl)
                        .apply(RequestOptions.circleCropTransform())
                        .into(holder.candidatePhotoImageViewCD);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Handle the failure to retrieve the image
            }
        });


    }

    @Override
    public int getItemCount() {
        return candidateDetailsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView candidateNameTextViewCD;
        TextView candidateStatusTextViewCD;
        ImageView candidatePhotoImageViewCD;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            candidateNameTextViewCD = itemView.findViewById(R.id.candidateNameTextViewCD);
            candidateStatusTextViewCD = itemView.findViewById(R.id.candidateStatusTextViewCD);
            candidatePhotoImageViewCD = itemView.findViewById(R.id.candidatePhotoImageViewCD);

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
