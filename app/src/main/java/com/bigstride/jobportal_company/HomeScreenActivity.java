package com.bigstride.jobportal_company;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class HomeScreenActivity extends AppCompatActivity {

    ImageView IVCompanyProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        IVCompanyProfile = findViewById(R.id.IVCompanyProfile);

        IVCompanyProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeScreenActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });
    }
}