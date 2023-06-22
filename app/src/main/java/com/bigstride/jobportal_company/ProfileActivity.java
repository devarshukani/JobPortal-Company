package com.bigstride.jobportal_company;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class ProfileActivity extends AppCompatActivity {

    TextView TVEmailOrUserName;
    Button BTNlogoutButton;
    ImageView IVEditProfile;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        TVEmailOrUserName = findViewById(R.id.TVEmailOrUserName);
        BTNlogoutButton = findViewById(R.id.BTNlogoutButton);
        IVEditProfile = findViewById(R.id.IVEditProfile);

        auth = FirebaseAuth.getInstance();

        TVEmailOrUserName.setText(auth.getCurrentUser().getEmail());


        IVEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                startActivity(intent);
            }
        });



        BTNlogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ProfileActivity.this);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("userId", null);
                editor.apply();

                Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finishAffinity();
            }
        });


    }
}