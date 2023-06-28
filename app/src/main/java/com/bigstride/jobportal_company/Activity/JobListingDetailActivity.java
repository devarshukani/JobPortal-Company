package com.bigstride.jobportal_company.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bigstride.jobportal_company.AppliedCandidateListActivity;
import com.bigstride.jobportal_company.R;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;

public class JobListingDetailActivity extends AppCompatActivity {

    TextView TVJobPosition, TVStartingDate, TVApplyBeforeDate, TVMinimumQualification, TVJobRequirement, TVJobDescription, TVJobType, TVExperienceRequired;
    ChipGroup TVRequiredSkills;
    Button BTNAppliedCandidates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_listing_detail);

        String job_position = getIntent().getStringExtra("job_position");
        String starting_date = getIntent().getStringExtra("starting_date");
        String apply_before_date = getIntent().getStringExtra("apply_before_date");
        String minimum_qualification_required = getIntent().getStringExtra("minimum_qualification_required");
        String job_requirement = getIntent().getStringExtra("job_requirement");
        String job_description = getIntent().getStringExtra("job_description");
        String job_type = getIntent().getStringExtra("job_type");
        String experience_required = getIntent().getStringExtra("experience_required");
        ArrayList<String> required_skills = getIntent().getStringArrayListExtra("required_skills");
        String job_document_id = getIntent().getStringExtra("job_document_id");

        TVJobPosition = findViewById(R.id.TVJobPosition);
        TVStartingDate = findViewById(R.id.TVStartingDate);
        TVApplyBeforeDate = findViewById(R.id.TVApplyBeforeDate);
        TVMinimumQualification = findViewById(R.id.TVMinimumQualification);
        TVJobRequirement = findViewById(R.id.TVJobRequirement);
        TVJobDescription = findViewById(R.id.TVJobDescription);
        TVJobType = findViewById(R.id.TVJobType);
        TVExperienceRequired = findViewById(R.id.TVExperienceRequired);
        TVRequiredSkills = findViewById(R.id.TVRequiredSkills);

        BTNAppliedCandidates = findViewById(R.id.BTNAppliedCandidates);

        for (String skill : required_skills) {
            Chip chip = new Chip(this);
            chip.setText(skill);
            chip.setClickable(false); // Disable click for display purposes
            chip.setCheckable(false); // Disable check for display purposes
            chip.setBackgroundResource(R.drawable.button_secondary_background);
            TVRequiredSkills.addView(chip);
        }

        TVJobPosition.setText(job_position);
        TVStartingDate.setText(starting_date);
        TVApplyBeforeDate.setText(apply_before_date);
        TVMinimumQualification.setText(minimum_qualification_required);
        TVJobRequirement.setText(job_requirement);
        TVJobDescription.setText(job_description);
        TVJobType.setText(job_type);
        TVExperienceRequired.setText(experience_required.equals("1") || experience_required.equals("0") ? experience_required +" Year": experience_required +" Years");


        BTNAppliedCandidates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(JobListingDetailActivity.this, AppliedCandidateListActivity.class);
                intent.putExtra("job_document_id", job_document_id);
                startActivity(intent);
            }
        });



    }
}