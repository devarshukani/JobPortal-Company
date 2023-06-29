package com.bigstride.jobportal_company.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigstride.jobportal_company.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class CandidateProfileActivity extends AppCompatActivity {

    TextView fullNameTextViewCP, summaryTextViewCP;
    ImageView candidatePhotoImageViewCP, phoneNoImageViewCP, emailImageViewCP;
    Button BTNReject, BTNShortList;

    FirebaseAuth auth;
    FirebaseFirestore db;

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

        BTNReject = findViewById(R.id.BTNReject);
        BTNShortList = findViewById(R.id.BTNShortList);

        updatePhoto(user_id);
        fullNameTextViewCP.setText(full_name);
        summaryTextViewCP.setText(summary);

        phoneNoImageViewCP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + contact_no));
                startActivity(intent);
            }
        });

        emailImageViewCP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                db.collection("AppliedJobs")
                        .whereEqualTo("candidate_user_id", user_id)
                        .whereEqualTo("job_document_id", job_document_id)
                        .get().addOnSuccessListener(querySnapshot -> {
                            // Check if there are any matching documents
                            if (!querySnapshot.isEmpty()) {
                                // Get the reference to the first matching document
                                DocumentReference documentRef = querySnapshot.getDocuments().get(0).getReference();

                                // Update the application_status field to "shortlisted"
                                documentRef.update("application_status", "Rejected")
                                        .addOnSuccessListener(aVoid -> {
                                            // Update successful
                                            // Handle any additional logic here
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
        });

        BTNShortList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("AppliedJobs")
                        .whereEqualTo("candidate_user_id", user_id)
                        .whereEqualTo("job_document_id", job_document_id)
                        .get().addOnSuccessListener(querySnapshot -> {
                            // Check if there are any matching documents
                            if (!querySnapshot.isEmpty()) {
                                // Get the reference to the first matching document
                                DocumentReference documentRef = querySnapshot.getDocuments().get(0).getReference();

                                // Update the application_status field to "shortlisted"
                                documentRef.update("application_status", "Shortlisted")
                                        .addOnSuccessListener(aVoid -> {
                                            // Update successful
                                            // Handle any additional logic here
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
}