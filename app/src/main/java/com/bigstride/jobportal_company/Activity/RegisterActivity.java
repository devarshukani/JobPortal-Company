package com.bigstride.jobportal_company.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bigstride.jobportal_company.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    EditText ETregisterEmail , ETregisterPassword, ETregisterRePassword;
    Button BTNregisterButton;
    TextView TVloginLink;
    FirebaseAuth auth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ETregisterEmail = findViewById(R.id.ETregisterEmail);
        ETregisterPassword = findViewById(R.id.ETregisterPassword);
        ETregisterRePassword = findViewById(R.id.ETregisterRePassword);
        BTNregisterButton = findViewById(R.id.BTNregisterButton);
        TVloginLink = findViewById(R.id.TVloginLink);

        // Firebase Instances
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        BTNregisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = ETregisterEmail.getText().toString().trim();
                String password  = ETregisterPassword.getText().toString().trim();
                String repassword  = ETregisterRePassword.getText().toString().trim();

                if(email.isEmpty()){
                    ETregisterEmail.setError("Email cannot be empty");
                }
                else if (isCompanyEmail(email)) {
                    ETregisterEmail.setError("Enter Valid Company email");
                }
                else if(password.isEmpty()){
                    ETregisterPassword.setError("Password cannot be empty");
                }
                else if (repassword.isEmpty()) {
                    ETregisterRePassword.setError("Confirm Password cannot be empty");
                }
                else if (!repassword.equals(password)) {
                    ETregisterRePassword.setError("Values does not match");
                }
                else {

                    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                auth.getCurrentUser().sendEmailVerification();
                                view.performHapticFeedback(HapticFeedbackConstants.CONFIRM);

                                Toast.makeText(RegisterActivity.this, "verification link has been sent on your mail", Toast.LENGTH_LONG).show();

                                Map<String, Object> userPersonalDetails = new HashMap<>();
                                userPersonalDetails.put("user_type", "company");
                                userPersonalDetails.put("job_listing", 0);
                                userPersonalDetails.put("available_listings", 5);

                                db.collection("CompanyProfileDetails").document(auth.getCurrentUser().getUid())
                                        .set(userPersonalDetails)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {

//                                                Toast.makeText(RegisterActivity.this, "Personal Details Saved Successfully", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w("Error", "Error adding document", e);
//                                                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(intent);
                                finish();

                            } else {
                                Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                view.performHapticFeedback(HapticFeedbackConstants.REJECT);
                            }
                        }

                    });

                }

            }
        });


        TVloginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.performHapticFeedback(HapticFeedbackConstants.CONFIRM);
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    public static boolean isCompanyEmail(String email) {
        String[] parts = email.split("@");
        String domain = parts[1]; // The domain is the second part after the '@' symbol

        List<String> personalDomains = Arrays.asList(
                "gmail.com",
                "yahoo.com",
                "hotmail.com"
        );

        boolean isPersonalEmail = personalDomains.contains(domain);
        if (!isPersonalEmail) {
            return false;
        } else {
            return true;
        }
    }

}