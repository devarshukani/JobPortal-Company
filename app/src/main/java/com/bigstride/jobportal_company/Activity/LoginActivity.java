package com.bigstride.jobportal_company.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bigstride.jobportal_company.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    EditText ETloginPassword, ETloginEmail;
    TextView TVregisterLink;
    Button BTNloginButton;
    FirebaseAuth auth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TVregisterLink = findViewById(R.id.TVregisterLink);
        ETloginEmail = findViewById(R.id.ETloginEmail);
        ETloginPassword = findViewById(R.id.ETloginPassword);
        BTNloginButton = findViewById(R.id.BTNloginButton);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        BTNloginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = ETloginEmail.getText().toString().trim();
                String password  = ETloginPassword.getText().toString().trim();

                if(!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    if(!password.isEmpty()){
                        auth.signInWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {

                                String uid = authResult.getUser().getUid();

                                if(auth.getCurrentUser().isEmailVerified())
                                {


                                    db.collection("CompanyProfileDetails").document(auth.getCurrentUser().getUid())
                                            .get()
                                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                @Override
                                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                    if (documentSnapshot.exists()) {
                                                        String userType = documentSnapshot.getString("user_type");

                                                        if(userType.equals("company")){

                                                            String userId = auth.getCurrentUser().getUid();
                                                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                                                            SharedPreferences.Editor editor = preferences.edit();
                                                            editor.putString("userId", userId);
                                                            editor.apply();

                                                            Intent intent = new Intent(LoginActivity.this, HomeScreenActivity.class);
                                                            startActivity(intent);
                                                            LoginActivity.this.finish();
                                                        }
                                                        else{
                                                            Toast.makeText(LoginActivity.this, "User Type is not valid", Toast.LENGTH_SHORT).show();
                                                        }


                                                    } else {
//                                                        Log.d("Error", "Document does not exist");
                                                        Toast.makeText(LoginActivity.this, "User Type is not valid", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.w("Error", "Error getting document", e);
                                                }
                                            });


//                                    String userId = auth.getCurrentUser().getUid();
//                                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
//                                    SharedPreferences.Editor editor = preferences.edit();
//                                    editor.putString("userId", userId);
//                                    editor.apply();
//
//                                    Intent intent = new Intent(LoginActivity.this, HomeScreenActivity.class);
//                                    startActivity(intent);
//                                    LoginActivity.this.finish();
                                } else{
                                    Toast.makeText(LoginActivity.this, "Email Is not verified", Toast.LENGTH_SHORT).show();
                                    auth.getCurrentUser().sendEmailVerification();
                                    Toast.makeText(LoginActivity.this, "verification link has been sent on your mail", Toast.LENGTH_LONG).show();
                                }


                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                    else {
                        ETloginPassword.setError("Password cannot be empty");
                    }
                }
                else if(email.isEmpty()){
                    ETloginEmail.setError("Email cannot be empty");
                }
                else{
                    ETloginEmail.setError("Invalid email format");
                }
            }
        });

        TVregisterLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}