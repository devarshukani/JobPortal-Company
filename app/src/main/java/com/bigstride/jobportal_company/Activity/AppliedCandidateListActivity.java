package com.bigstride.jobportal_company.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bigstride.jobportal_company.Adapter.CandidateDetailsAdapter;
import com.bigstride.jobportal_company.Model.CandidateDetailsModel;
import com.bigstride.jobportal_company.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AppliedCandidateListActivity extends AppCompatActivity {


    private RecyclerView RVCandidateDetailsList;
    private CandidateDetailsAdapter candidateDetailsAdapter;
    private List<CandidateDetailsModel> candidateDetailsList;
    private ProgressBar progressBar;
    FirebaseAuth auth;
    FirebaseFirestore db;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applied_candidate_list);



        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


        RVCandidateDetailsList = findViewById(R.id.RVCandidateDetailsList);
        progressBar = findViewById(R.id.progressBar);

        RVCandidateDetailsList.setLayoutManager(new LinearLayoutManager(this));
        candidateDetailsList = new ArrayList<>();
        candidateDetailsAdapter = new CandidateDetailsAdapter(candidateDetailsList, this);
        RVCandidateDetailsList.setAdapter(candidateDetailsAdapter);

        RVCandidateDetailsList.setAdapter(candidateDetailsAdapter);


    }

    @Override
    protected void onResume() {
        super.onResume();

        String job_document_id = getIntent().getStringExtra("job_document_id");
        progressBar.setVisibility(View.VISIBLE);
        loadJobListingData(job_document_id);
    }

    private void loadJobListingData(String job_document_id){
        candidateDetailsList.clear();


        db.collection("AppliedJobs")
                .whereEqualTo("job_document_id",job_document_id)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(queryDocumentSnapshots.isEmpty()){
//                            IVNoAppliedJobs.setVisibility(View.VISIBLE);
                        }
                        else{
                            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                String candidate_user_id = document.getString("candidate_user_id");

                                db.collection("CandidateProfileDetails").document(candidate_user_id)
                                        .get()
                                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot doc) {

                                                if (doc.exists()) {
                                                    // Access the jobListing document data
                                                    String user_id = doc.getId();
                                                    String full_name = doc.getString("full_name");
                                                    String date_of_birth = doc.getString("date_of_birth");
                                                    String gender = doc.getString("gender");
                                                    String contact_no = doc.getString("contact_no");
                                                    String email = doc.getString("email_no");
                                                    String summary = doc.getString("summary");
                                                    String address = doc.getString("address");



                                                    CandidateDetailsModel candidate = new CandidateDetailsModel(user_id, full_name, date_of_birth, gender, contact_no, email, summary, address);
                                                    candidateDetailsList.add(candidate);

                                                    // Notify the adapter after all data is added
                                                    candidateDetailsAdapter.notifyDataSetChanged();

                                                } else {
                                                    System.out.println("Job Listing document does not exist.");
                                                }
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.e("Error", "Error fetching job listing details", e);
                                            }
                                        });

                            }
                        }

                        progressBar.setVisibility(View.GONE);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Error", "Error fetching saved jobs", e);
                    }
                });




        candidateDetailsAdapter.setOnItemClickListener(new CandidateDetailsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                CandidateDetailsModel cp = candidateDetailsList.get(position);
                Intent intent = new Intent(AppliedCandidateListActivity.this, CandidateProfileActivity.class);

                intent.putExtra("job_document_id", job_document_id);
                intent.putExtra("user_id", cp.getUser_id());
                intent.putExtra("full_name", cp.getFull_name());
                intent.putExtra("date_of_birth", cp.getDate_of_birth());
                intent.putExtra("gender", cp.getGender());
                intent.putExtra("contact_no", cp.getContact_no());
                intent.putExtra("email", cp.getEmail());
                intent.putExtra("summary", cp.getSummary());
                intent.putExtra("address", cp.getAddress());

                startActivity(intent);
            }
        });


    }
}