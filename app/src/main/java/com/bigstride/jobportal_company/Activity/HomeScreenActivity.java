package com.bigstride.jobportal_company.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bigstride.jobportal_company.Adapter.CompanyJobListingAdapter;
import com.bigstride.jobportal_company.Adapter.MinimumQualificationSpinnerAdapter;
import com.bigstride.jobportal_company.Model.CompanyJobListingModel;
import com.bigstride.jobportal_company.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeScreenActivity extends AppCompatActivity {

    ImageView IVCompanyProfile;
    FloatingActionButton FABAddJobListing;
    Spinner ETMinimumQualificationRequired;

    RecyclerView RVCompanyJobListings;
    private CompanyJobListingAdapter companyJobListingAdapter;
    private List<CompanyJobListingModel> companyJobListingList;

    FirebaseAuth auth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        IVCompanyProfile = findViewById(R.id.IVCompanyProfile);
        FABAddJobListing = findViewById(R.id.FABAddJobListing);


        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        IVCompanyProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeScreenActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });


        FABAddJobListing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddJobListingSheet();
            }
        });


        // -----------------------------------------------------------------------------------------

        RVCompanyJobListings = findViewById(R.id.RVCompanyJobListings);
        RVCompanyJobListings.setLayoutManager(new LinearLayoutManager(this));
        companyJobListingList = new ArrayList<>();
        companyJobListingAdapter = new CompanyJobListingAdapter(companyJobListingList);
        RVCompanyJobListings.setAdapter(companyJobListingAdapter);

        RVCompanyJobListings.setAdapter(companyJobListingAdapter);

        loadJobListingData();


    }


    private void showAddJobListingSheet(){
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);

        // Set the layout for the bottom sheet
        View bottomSheetView = getLayoutInflater().inflate(R.layout.dialog_add_new_job_listing, null);
        bottomSheetDialog.setContentView(bottomSheetView);

        Button BTNSaveJobListing = bottomSheetView.findViewById(R.id.BTNSaveJobListing);
        EditText ETJobPosition = bottomSheetView.findViewById(R.id.ETJobPosition);
        EditText ETStartingDate = bottomSheetView.findViewById(R.id.ETStartingDate);
        EditText ETApplyBeforeDate = bottomSheetView.findViewById(R.id.ETApplyBeforeDate);

        EditText ETJobRequirement = bottomSheetView.findViewById(R.id.ETJobRequirement);
        EditText ETJobDescription = bottomSheetView.findViewById(R.id.ETJobDescription);

        ETMinimumQualificationRequired = bottomSheetView.findViewById(R.id.ETMinimumQualificationRequired);

        List<String> qualificationList = Arrays.asList("10th Grade", "12th Grade", "Diploma Degree","Bachelor Degree", "Master Degree", "Doctorate Degree");
        MinimumQualificationSpinnerAdapter adapter = new MinimumQualificationSpinnerAdapter(HomeScreenActivity.this, R.layout.item_minimum_qualification_spinner, qualificationList);
        ETMinimumQualificationRequired.setAdapter(adapter);

        ETStartingDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                // Create a DatePickerDialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        HomeScreenActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                                // Handle the selected date
                                // Update the EditText with the selected date
                                String dateOfBirth = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                                ETStartingDate.setText(dateOfBirth);
                            }
                        },
                        year,
                        month,
                        day
                );

                // Show the DatePickerDialog
                datePickerDialog.show();
            }
        });

        ETApplyBeforeDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                // Create a DatePickerDialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        HomeScreenActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                                // Handle the selected date
                                // Update the EditText with the selected date
                                String dateOfBirth = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                                ETApplyBeforeDate.setText(dateOfBirth);
                            }
                        },
                        year,
                        month,
                        day
                );

                // Show the DatePickerDialog
                datePickerDialog.show();
            }
        });

        BTNSaveJobListing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String jobPosition = ETJobPosition.getText().toString().trim();
                String startingDate = ETStartingDate.getText().toString().trim();
                String applyBeforeDate = ETApplyBeforeDate.getText().toString().trim();
                String minimumQualificationRequired = ETMinimumQualificationRequired.getSelectedItem().toString().trim();
                String jobRequirement = ETJobRequirement.getText().toString().trim();
                String jobDescription = ETJobDescription.getText().toString().trim();

                if (jobPosition.isEmpty()) {
                    ETJobPosition.setError("Job Position cannot be Empty");
                }
                else if (startingDate.isEmpty()) {
                    ETStartingDate.setError("Starting Date cannot be Empty");
                }
                else if (applyBeforeDate.isEmpty()) {
                    ETApplyBeforeDate.setError("Apply Before Date cannot be Empty");
                }
                else if (jobRequirement.isEmpty()){
                    ETJobRequirement.setError("Job Requirement cannot be Empty");
                }
                else if (jobDescription.isEmpty()){
                    ETJobDescription.setError("Job Description cannot be Empty");
                }
                else {

                    db.collection("CompanyProfileDetails").document(auth.getCurrentUser().getUid())
                            .get()
                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    if (documentSnapshot.exists()) {
                                        String companyName = documentSnapshot.getString("company_name");



                                        Map<String, Object> userEducationDetails = new HashMap<>();
                                        userEducationDetails.put("company_name", companyName);
                                        userEducationDetails.put("company_id", auth.getCurrentUser().getUid());
                                        userEducationDetails.put("job_position", jobPosition);
                                        userEducationDetails.put("starting_date", startingDate);
                                        userEducationDetails.put("apply_before_date", applyBeforeDate);
                                        userEducationDetails.put("minimum_qualification_required", minimumQualificationRequired);
                                        userEducationDetails.put("job_requirement", jobRequirement);
                                        userEducationDetails.put("job_description", jobDescription);

                                        db.collection("JobListing")
                                                .add(userEducationDetails)
                                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                    @Override
                                                    public void onSuccess(DocumentReference documentReference) {

                                                        Toast.makeText(HomeScreenActivity.this, "Job Listing Saved Successfully", Toast.LENGTH_SHORT).show();

                                                        loadJobListingData();

                                                        bottomSheetDialog.hide();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.w("Error", "Error adding document", e);
                                                        Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                                                    }
                                                });




                                    } else {
                                        Log.d("Error", "Document does not exist");
                                        Toast.makeText(HomeScreenActivity.this, "First Complete The Company Profile", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("Error", "Error getting document", e);
                                }
                            });






                }

            }
        });

        // Show the bottom sheet
        bottomSheetDialog.show();
    }

    // ---------------------------------------------------------------------------------------------

    private void loadJobListingData(){

        companyJobListingList.clear();

        db.collection("JobListing")
                .whereEqualTo("company_id", auth.getCurrentUser().getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {

                            String documentID = document.getId();
                            String job_position = document.getString("job_position");
                            String starting_date = document.getString("starting_date");
                            String apply_before_date = document.getString("apply_before_date");
                            String minimum_qualification_required = document.getString("minimum_qualification_required");
                            String job_requirement = document.getString("job_requirement");
                            String job_description = document.getString("job_description");

                            CompanyJobListingModel joblist = new CompanyJobListingModel(job_position, starting_date, apply_before_date, minimum_qualification_required, job_requirement,job_description, documentID);
                            companyJobListingList.add(joblist);
                        }
                        companyJobListingAdapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Error", "Error fetching education details", e);
                    }
                });

        companyJobListingAdapter.setOnItemClickListener(new CompanyJobListingAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                CompanyJobListingModel jl = companyJobListingList.get(position);
                Intent intent = new Intent(HomeScreenActivity.this, JobListingDetailActivity.class);
                intent.putExtra("job_position", jl.getJob_position());
                intent.putExtra("starting_date", jl.getStarting_date());
                intent.putExtra("apply_before_date", jl.getApply_before_date());
                intent.putExtra("minimum_qualification_required", jl.getMinimum_qualification_required());
                intent.putExtra("job_requirement", jl.getJob_requirement());
                intent.putExtra("job_description", jl.getJob_description());
                startActivity(intent);
            }
        });


        companyJobListingAdapter.setOnItemLongClickListener(new CompanyJobListingAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(int position) {

                Dialog dialog = new Dialog(HomeScreenActivity.this, R.style.CustomDialog);
                dialog.setContentView(R.layout.dialog_edit_or_delete_options);

                Button  BTNDialogCancel = dialog.findViewById(R.id.BTNDialogCancel);
                Button BTNDialogDelete = dialog.findViewById(R.id.BTNDialogDelete);

                BTNDialogCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                BTNDialogDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteJobListing(position);
                        dialog.dismiss();
                    }
                });

                dialog.show();

            }
        });
    }

    private void deleteJobListing(int position) {
        CompanyJobListingModel joblisitng = companyJobListingList.get(position);

        // Delete the education details from Firestore
        db.collection("JobListing")
                .document(joblisitng.getDocument_id())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Toast.makeText(HomeScreenActivity.this, "Job Listing deleted.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(HomeScreenActivity.this, "Failed to delete Job Listing", Toast.LENGTH_SHORT).show();
                    }
                });

        // Remove the education details from the list and notify the adapter
        companyJobListingList.remove(position);
        companyJobListingAdapter.notifyItemRemoved(position);

        companyJobListingAdapter.notifyItemRangeChanged(position, companyJobListingList.size());
    }

}