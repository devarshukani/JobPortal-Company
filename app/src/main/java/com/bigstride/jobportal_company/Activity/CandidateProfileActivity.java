package com.bigstride.jobportal_company.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bigstride.jobportal_company.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class CandidateProfileActivity extends AppCompatActivity {

    TextView fullNameTextViewCP, summaryTextViewCP, TVApplicationStatusCP;
    ImageView candidatePhotoImageViewCP, phoneNoImageViewCP, emailImageViewCP;
    Button BTNReject, BTNNextStep;
    ProgressBar progressBar;

    FirebaseAuth auth;
    FirebaseFirestore db;
    String currentStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidate_profile);

        String user_id = getIntent().getStringExtra("user_id");
        String full_name = getIntent().getStringExtra("full_name");
        String date_of_birth = getIntent().getStringExtra("date_of_birth");
        String gender = getIntent().getStringExtra("gender");
        String contact_no = getIntent().getStringExtra("contact_no");
        String email = getIntent().getStringExtra("email");
        String summary = getIntent().getStringExtra("summary");
        String address = getIntent().getStringExtra("address");
        String job_document_id = getIntent().getStringExtra("job_document_id");

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        fullNameTextViewCP = findViewById(R.id.fullNameTextViewCP);
        summaryTextViewCP = findViewById(R.id.summaryTextViewCP);
        candidatePhotoImageViewCP = findViewById(R.id.candidatePhotoImageViewCP);
        phoneNoImageViewCP = findViewById(R.id.phoneNoImageViewCP);
        emailImageViewCP = findViewById(R.id.emailImageViewCP);
        progressBar = findViewById(R.id.progressBar);

        TVApplicationStatusCP = findViewById(R.id.TVApplicationStatusCP);
        BTNReject = findViewById(R.id.BTNReject);
        BTNNextStep = findViewById(R.id.BTNNextStep);

        updatePhoto(user_id);
        fullNameTextViewCP.setText(full_name);
        summaryTextViewCP.setText(summary);



        db.collection("AppliedJobs")
                .whereEqualTo("candidate_user_id", user_id)
                .whereEqualTo("job_document_id", job_document_id)
                .get().addOnSuccessListener(querySnapshot -> {
                    // Check if there are any matching documents
                    if (!querySnapshot.isEmpty()) {
                        // Get the reference to the first matching document
                        DocumentReference documentRef = querySnapshot.getDocuments().get(0).getReference();

                        // Update the application_status field to "shortlisted"
                        documentRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                currentStatus = documentSnapshot.getString("application_status");
                                TVApplicationStatusCP.setText(currentStatus);
                                if(currentStatus.equals("Applied")){
                                    BTNNextStep.setText("Consider Candidate");
                                }
                                else if(currentStatus.equals("Under Consideration")){
                                    BTNNextStep.setText("Shortlisted");
                                }
                                else if(currentStatus.equals("Shortlisted")){
                                    BTNNextStep.setText("Schedule Interview");
                                }
                                else if(currentStatus.equals("Interview")){
                                    BTNNextStep.setText("Offer Job");
                                }
                                else if(currentStatus.equals("Job Offered")){
                                    BTNNextStep.setVisibility(View.GONE);
                                }
                                else if(currentStatus.equals("Accepted")){
                                    BTNNextStep.setVisibility(View.GONE);
                                }
                                else if(currentStatus.equals("Rejected")){
                                    BTNNextStep.setVisibility(View.GONE);
                                    BTNReject.setVisibility(View.GONE);
                                }
                                progressBar.setVisibility(View.GONE);

                            }
                        });
                    } else {
                        // No matching documents found
                        // Handle this case accordingly
                    }
                })
                .addOnFailureListener(e -> {
                    // Query failed
                    // Handle any error here
                });



        phoneNoImageViewCP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.performHapticFeedback(HapticFeedbackConstants.CONFIRM);
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + contact_no));
                startActivity(intent);
            }
        });

        emailImageViewCP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.performHapticFeedback(HapticFeedbackConstants.CONFIRM);
                Intent intent=new Intent(Intent.ACTION_SEND);
                String[] recipients={email};
                intent.putExtra(Intent.EXTRA_EMAIL, recipients);
                intent.setType("text/html");
                intent.setPackage("com.google.android.gm");
                startActivity(Intent.createChooser(intent, "Send mail"));
            }
        });

        BTNReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.performHapticFeedback(HapticFeedbackConstants.REJECT);
                updateApplicationStatus(user_id, job_document_id, "Rejected");
            }
        });

        BTNNextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.performHapticFeedback(HapticFeedbackConstants.CONFIRM);

                if(currentStatus.equals("Applied")){
                    updateApplicationStatus(user_id, job_document_id, "Under Consideration");
                }
                else if(currentStatus.equals("Under Consideration")){
                    updateApplicationStatus(user_id, job_document_id, "Shortlisted");
                }
                else if(currentStatus.equals("Shortlisted")){
                    updateApplicationStatus(user_id, job_document_id, "Interview");
                }
                else if(currentStatus.equals("Interview")){
                    updateApplicationStatus(user_id, job_document_id, "Job Offered");
                }

            }
        });

    }

    void updatePhoto(String user_id){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference imageRef = storageRef.child("ProfileImage/" + user_id + ".png");


        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri downloadUrl) {
                Glide.with(CandidateProfileActivity.this)
                        .load(downloadUrl)
                        .apply(RequestOptions.circleCropTransform())
                        .into(candidatePhotoImageViewCP);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Handle the failure to retrieve the image
            }
        });
    }

    void updateApplicationStatus(String user_id, String job_document_id, String status){
        db.collection("AppliedJobs")
                .whereEqualTo("candidate_user_id", user_id)
                .whereEqualTo("job_document_id", job_document_id)
                .get().addOnSuccessListener(querySnapshot -> {
                    // Check if there are any matching documents
                    if (!querySnapshot.isEmpty()) {
                        // Get the reference to the first matching document
                        DocumentReference documentRef = querySnapshot.getDocuments().get(0).getReference();

                        // Update the application_status field to "shortlisted"
                        documentRef.update("application_status", status)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(this, "Status Updated to : "+status, Toast.LENGTH_SHORT).show();
                                    recreate();
                                })
                                .addOnFailureListener(e -> {
                                    // Update failed
                                    // Handle any error here
                                });
                    } else {
                        // No matching documents found
                        // Handle this case accordingly
                    }
                })
                .addOnFailureListener(e -> {
                    // Query failed
                    // Handle any error here
                });
    }
}