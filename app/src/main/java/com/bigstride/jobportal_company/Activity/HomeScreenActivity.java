package com.bigstride.jobportal_company.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bigstride.jobportal_company.Adapter.CompanyJobListingAdapter;
import com.bigstride.jobportal_company.Adapter.JobTypeSpinnerAdapter;
import com.bigstride.jobportal_company.Adapter.MinimumQualificationSpinnerAdapter;
import com.bigstride.jobportal_company.Adapter.SkillsAdapter;
import com.bigstride.jobportal_company.Model.CompanyJobListingModel;
import com.bigstride.jobportal_company.Model.SkillsModel;
import com.bigstride.jobportal_company.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
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
    Spinner ETMinimumQualificationRequired, ETJobType;

    RecyclerView RVCompanyJobListings;
    private CompanyJobListingAdapter companyJobListingAdapter;
    private List<CompanyJobListingModel> companyJobListingList;

    private List<SkillsModel> skills;
    private List<String> selectedSkills;
    private List<String> allSelectedSkills;
    private SkillsAdapter skillsAdapter;


    private RecyclerView skillsRecyclerView;
    private ChipGroup chipGroup;

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
        EditText ETExperienceRequired = bottomSheetView.findViewById(R.id.ETExperienceRequired);
        EditText ETJobRequirement = bottomSheetView.findViewById(R.id.ETJobRequirement);
        EditText ETJobDescription = bottomSheetView.findViewById(R.id.ETJobDescription);

        ETMinimumQualificationRequired = bottomSheetView.findViewById(R.id.ETMinimumQualificationRequired);
        ETJobType = bottomSheetDialog.findViewById(R.id.ETJobType);

        skillsRecyclerView = bottomSheetView.findViewById(R.id.skillsRecyclerView);
        chipGroup = bottomSheetView.findViewById(R.id.chipGroup);


        // -----------------------------------------------------------------------------------------

        List<String> qualificationList = Arrays.asList("10th Grade", "12th Grade", "Diploma Degree","Bachelor Degree", "Master Degree", "Doctorate Degree");
        MinimumQualificationSpinnerAdapter adapter = new MinimumQualificationSpinnerAdapter(HomeScreenActivity.this, R.layout.item_minimum_qualification_spinner, qualificationList);
        ETMinimumQualificationRequired.setAdapter(adapter);

        List<String> jobTypeList = Arrays.asList("Full Time", "Part Time", "Contract base","Internship");
        JobTypeSpinnerAdapter adapterJT = new JobTypeSpinnerAdapter(HomeScreenActivity.this, R.layout.item_job_type_spinner, jobTypeList);
        ETJobType.setAdapter(adapterJT);

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


        skills = new ArrayList<>();
        selectedSkills = new ArrayList<>();
        allSelectedSkills = new ArrayList<>();
        skillsAdapter = new SkillsAdapter(skills, selectedSkills, chipGroup);
        skillsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        skillsRecyclerView.setAdapter(skillsAdapter);

        // Load skills from Firestore
        loadSkills();

        // Set up ChipGroup for displaying selected skills
        ChipGroup chipGroup = bottomSheetView.findViewById(R.id.chipGroup);
        chipGroup.setOnCheckedChangeListener((group, checkedId) -> {
            Chip chip = findViewById(checkedId);
            if (chip != null) {
                String skillName = chip.getText().toString();
                if (chip.isChecked()) {
                    allSelectedSkills.add(skillName);
                } else {
                    allSelectedSkills.remove(skillName);
                }
            }
        });






        BTNSaveJobListing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String jobPosition = ETJobPosition.getText().toString().trim();
                String startingDate = ETStartingDate.getText().toString().trim();
                String applyBeforeDate = ETApplyBeforeDate.getText().toString().trim();
                String minimumQualificationRequired = ETMinimumQualificationRequired.getSelectedItem().toString().trim();
                String jobType = ETJobType.getSelectedItem().toString().trim();
                String experienceRequired = ETExperienceRequired.getText().toString().trim();
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
                else if (experienceRequired.isEmpty()){
                    ETExperienceRequired.setError("Experience Requirement cannot be Empty");
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
                                        userEducationDetails.put("experience_required", experienceRequired);
                                        userEducationDetails.put("job_type", jobType);

                                        db.collection("JobListing")
                                                .add(userEducationDetails)
                                                .addOnSuccessListener(documentReference -> {
                                                    String jobListingId = documentReference.getId();

                                                    addSkillsToFirebase(jobListingId);

                                                    loadJobListingData();

                                                    bottomSheetDialog.hide();
                                                })
                                                .addOnFailureListener(e -> {
                                                    Log.w("Error", "Error adding document", e);
                                                    Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
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
                            String job_type = document.getString("job_type");
                            String experience_required = document.getString("experience_required");
                            ArrayList<String> required_skills = new ArrayList<>();

                            CollectionReference skillsCollectionRef = document.getReference().collection("RequiredSkills");

                            skillsCollectionRef.get()
                                    .addOnSuccessListener(querySnapshot -> {

                                        for (QueryDocumentSnapshot skillDocument : querySnapshot) {
                                            String skillName = skillDocument.getString("skill_name");
                                            required_skills.add(skillName);
                                        }
                                    })
                                    .addOnFailureListener(e -> {
                                        Log.e("Error", "Error fetching skill details", e);
                                    });

                            CompanyJobListingModel joblist = new CompanyJobListingModel(job_position, starting_date, apply_before_date, minimum_qualification_required, job_requirement,job_description,job_type,experience_required, required_skills,  documentID);
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
                intent.putExtra("job_type", jl.getJob_type());
                intent.putExtra("experience_required", jl.getExperience_required());
                intent.putStringArrayListExtra("required_skills", jl.getRequired_skills());
                intent.putExtra("job_document_id", jl.getDocument_id());
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

    private void addSkillsToFirebase(String jobListingId){

        for (int i = 0; i < selectedSkills.size(); i++) {
            String skill = selectedSkills.get(i);

            Log.d("SKILLSPRINT", "onSuccess: "+ skill);

            Map<String, Object> skillData = new HashMap<>();
            skillData.put("skill_name", skill);

            db.collection("JobListing").document(jobListingId).collection("RequiredSkills")
                    .add(skillData)
                    .addOnSuccessListener(skillDocumentReference -> {
                        Toast.makeText(HomeScreenActivity.this, "Job Listing Saved Successfully", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                    });
        }

    }

    // ---------------------------------------------------------------------------------------------

    private void loadSkills() {
        // Replace "SkillList" with your actual Firestore collection name
        db.collection("SkillsList").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                        String skillName = document.getString("skill_name");
                        if (skillName != null) {
                            SkillsModel skill = new SkillsModel(skillName);
                            skills.add(skill);
                        }
                    }
                    skillsAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    // Handle any errors while loading skills
                });

    }


}