package com.bigstride.jobportal_company.Activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bigstride.jobportal_company.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.kittinunf.fuel.Fuel;
import com.github.kittinunf.fuel.core.FuelError;
import com.github.kittinunf.fuel.core.Handler;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.stripe.android.*;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ProfileActivity extends AppCompatActivity {

    TextView TVEmailProfile, TVCompanyProfile, TVJobListedProfile, TVAvailableListingProfile;
    Button BTNlogoutButton, BTNPackagesOffers;
    ImageView IVEditProfile, IVCompanyLogo;
    FirebaseAuth auth;
    FirebaseFirestore db;
    private Uri selectedImageUri;

    ProgressBar progressBar;
    private ActivityResultLauncher<Intent> imagePickerLauncher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        TVCompanyProfile = findViewById(R.id.TVCompanyProfile);
        TVEmailProfile = findViewById(R.id.TVEmailProfile);
        BTNlogoutButton = findViewById(R.id.BTNlogoutButton);
        IVEditProfile = findViewById(R.id.IVEditProfile);
        IVCompanyLogo = findViewById(R.id.IVCompanyLogo);
        progressBar = findViewById(R.id.progressBar);
        BTNPackagesOffers = findViewById(R.id.BTNPackagesOffers);

        TVJobListedProfile = findViewById(R.id.TVJobListedProfile);
        TVAvailableListingProfile = findViewById(R.id.TVAvailableListingProfile);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        progressBar.setVisibility(View.VISIBLE);


        TVEmailProfile.setText(auth.getCurrentUser().getEmail());

        getProfileData();

        updatePhoto();

        IVEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.performHapticFeedback(HapticFeedbackConstants.CONFIRM);
                Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                startActivity(intent);
            }
        });

        BTNPackagesOffers.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("HandlerLeak")
            @Override
            public void onClick(View v) {
                v.performHapticFeedback(HapticFeedbackConstants.CONFIRM);

                Intent intent = new Intent(ProfileActivity.this, PaymentsActivity.class);
                intent.putExtra("available_listings",TVAvailableListingProfile.getText());
                startActivity(intent);

            }
        });


        BTNlogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                view.performHapticFeedback(HapticFeedbackConstants.CONFIRM);

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

        IVCompanyLogo.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                view.performHapticFeedback(HapticFeedbackConstants.CONFIRM);
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                imagePickerLauncher.launch(intent);

                return false;
            }
        });


        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            if (data != null) {
                                // Get the selected image Uri
                                selectedImageUri = data.getData();

                                // Upload the image to Firebase Storage
                                if (selectedImageUri != null) {
                                    uploadImageToStorage(selectedImageUri);
                                } else {
                                    // Handle the case when no image was selected
                                }
                            }
                        }
                    }
                }
        );


    }

    private void getProfileData() {
        db.collection("CompanyProfileDetails").document(auth.getCurrentUser().getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        TVCompanyProfile.setText(documentSnapshot.getString("company_name"));
                        TVJobListedProfile.setText(String.valueOf(documentSnapshot.getLong("job_listed").intValue()));
                        TVAvailableListingProfile.setText(String.valueOf(documentSnapshot.getLong("available_listings").intValue()));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        TVCompanyProfile.setVisibility(View.GONE);
                    }
                });

    }

    private void uploadImageToStorage(Uri imageUri) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);

            // Calculate the desired dimensions for the compressed image
            int maxWidth = 256; // Maximum width of the compressed image
            int maxHeight = 256; // Maximum height of the compressed image
            int originalWidth = bitmap.getWidth();
            int originalHeight = bitmap.getHeight();

            // Calculate the scale factor to resize the image while maintaining aspect ratio
            float scaleFactor = Math.min(((float) maxWidth / originalWidth), ((float) maxHeight / originalHeight));

            // Resize the bitmap
            int scaledWidth = Math.round(originalWidth * scaleFactor);
            int scaledHeight = Math.round(originalHeight * scaleFactor);
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, true);

            // Compress the resized bitmap
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);

            byte[] imageData = baos.toByteArray();

            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            StorageReference imageRef = storageRef.child("ProfileImage/" + auth.getCurrentUser().getUid() + ".png");

            UploadTask uploadTask = imageRef.putBytes(imageData);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // Image uploaded successfully. You can store the image URL or download URL in the user's profile.
                    imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri downloadUrl) {
                            updatePhoto();
                            // Save the download URL to the user's profile in Firebase Authentication or a separate user database.
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // Handle the image upload failure.
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    void updatePhoto(){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference imageRef = storageRef.child("ProfileImage/" + auth.getCurrentUser().getUid() + ".png");


        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri downloadUrl) {
                Glide.with(ProfileActivity.this)
                        .load(downloadUrl)
                        .apply(RequestOptions.circleCropTransform())
                        .into(IVCompanyLogo);
                progressBar.setVisibility(View.GONE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Handle the failure to retrieve the image
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}