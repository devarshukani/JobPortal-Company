package com.bigstride.jobportal_company.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Parcelable;
import android.widget.TextView;
import android.widget.Toast;

import com.bigstride.jobportal_company.Model.CompanyJobListingModel;
import com.bigstride.jobportal_company.R;

import java.util.ArrayList;
import java.util.List;

public class JobListingDetailActivity extends AppCompatActivity {

    TextView TVJobPosition, TVStartingDate, TVApplyBeforeDate, TVMinimumQualification, TVJobRequirement, TVJobDescription, TVJobType, TVExperienceRequired;

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

        TVJobPosition = findViewById(R.id.TVJobPosition);
        TVStartingDate = findViewById(R.id.TVStartingDate);
        TVApplyBeforeDate = findViewById(R.id.TVApplyBeforeDate);
        TVMinimumQualification = findViewById(R.id.TVMinimumQualification);
        TVJobRequirement = findViewById(R.id.TVJobRequirement);
        TVJobDescription = findViewById(R.id.TVJobDescription);
        TVJobType = findViewById(R.id.TVJobType);
        TVExperienceRequired = findViewById(R.id.TVExperienceRequired);

        TVJobPosition.setText(job_position);
        TVStartingDate.setText(starting_date);
        TVApplyBeforeDate.setText(apply_before_date);
        TVMinimumQualification.setText(minimum_qualification_required);
        TVJobRequirement.setText(job_requirement);
        TVJobDescription.setText(job_description);
        TVJobType.setText(job_type);
        TVExperienceRequired.setText(experience_required);



    }
}