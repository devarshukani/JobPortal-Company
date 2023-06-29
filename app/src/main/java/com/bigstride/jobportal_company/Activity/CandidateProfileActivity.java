package com.bigstride.jobportal_company.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.bigstride.jobportal_company.R;

public class CandidateProfileActivity extends AppCompatActivity {

    TextView userIDTextViewCP, fullNameTextViewCP, dateOfBirthTextViewCP, genderTextViewCP, contactNoTextViewCP, emailTextViewCP, summaryTextViewCP, addressTextViewCP;

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

        userIDTextViewCP = findViewById(R.id.userIDTextViewCP);
        fullNameTextViewCP = findViewById(R.id.fullNameTextViewCP);
        dateOfBirthTextViewCP = findViewById(R.id.dateOfBirthTextViewCP);
        genderTextViewCP = findViewById(R.id.genderTextViewCP);
        contactNoTextViewCP = findViewById(R.id.contactNoTextViewCP);
        emailTextViewCP = findViewById(R.id.emailTextViewCP);
        summaryTextViewCP = findViewById(R.id.summaryTextViewCP);
        addressTextViewCP = findViewById(R.id.addressTextViewCP);


        userIDTextViewCP.setText(user_id);
        fullNameTextViewCP.setText(full_name);
        dateOfBirthTextViewCP.setText(date_of_birth);
        genderTextViewCP.setText(gender);
        contactNoTextViewCP.setText(contact_no);
        emailTextViewCP.setText(email);
        summaryTextViewCP.setText(summary);
        addressTextViewCP.setText(address);


    }
}