package com.bigstride.jobportal_company;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity {

    EditText ETCompanyName, ETCompanyTagline, ETCompanyEmployeeCount, ETCompanyDescription;
    ImageView IVSaveCompanyDetails;
    FirebaseAuth auth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        ETCompanyName = findViewById(R.id.ETCompanyName);
        ETCompanyTagline = findViewById(R.id.ETCompanyTagline);
        ETCompanyEmployeeCount = findViewById(R.id.ETCompanyEmployeeCount);
        ETCompanyDescription = findViewById(R.id.ETCompanyDescription);
        IVSaveCompanyDetails = findViewById(R.id.IVSaveCompanyDetails);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        getSavedCompanyDetails();



        IVSaveCompanyDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String companyName = ETCompanyName.getText().toString().trim();
                String companyDescription = ETCompanyDescription.getText().toString().trim();
                String compantEmployeeCount = ETCompanyEmployeeCount.getText().toString().trim();
                String companyTagline = ETCompanyTagline.getText().toString().trim();

                if (companyName.isEmpty()) {
                    ETCompanyName.setError("Company Name cannot be Empty");
                }
                else if (companyTagline.isEmpty()) {
                    ETCompanyTagline.setError("Company Tagline cannot be Empty");
                }
                else if (compantEmployeeCount.isEmpty()) {
                    ETCompanyEmployeeCount.setError("Company Employee count cannot be Empty");
                }
                else if (companyDescription.isEmpty()){
                    ETCompanyDescription.setError("Company Description cannot be empty");
                }
                else {
                    Map<String, Object> userCompanyDetails = new HashMap<>();
                    userCompanyDetails.put("company_name", companyName);
                    userCompanyDetails.put("company_tagline", companyTagline);
                    userCompanyDetails.put("company_employee_count", compantEmployeeCount);
                    userCompanyDetails.put("company_description", companyDescription);
                    userCompanyDetails.put("user_type", "company");

                    db.collection("CompanyProfileDetails").document(auth.getCurrentUser().getUid())
                            .set(userCompanyDetails)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {

                                    Toast.makeText(EditProfileActivity.this, "Company Details Saved Successfully", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("Error", "Error adding document", e);
                                    Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });
    }

    private void getSavedCompanyDetails(){
        db.collection("CompanyProfileDetails").document(auth.getCurrentUser().getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            String name = documentSnapshot.getString("company_name");
                            String tagline = documentSnapshot.getString("company_tagline");
                            String count = documentSnapshot.getString("company_employee_count");
                            String desc = documentSnapshot.getString("company_description");

                            ETCompanyName.setText(name);
                            ETCompanyTagline.setText(tagline);
                            ETCompanyEmployeeCount.setText(count);
                            ETCompanyDescription.setText(desc);


                        } else {
                            Log.d("Error", "Document does not exist");
                            Toast.makeText(EditProfileActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
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