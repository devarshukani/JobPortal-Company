package com.bigstride.jobportal_company.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bigstride.jobportal_company.R;
import com.github.kittinunf.fuel.Fuel;
import com.github.kittinunf.fuel.core.FuelError;
import com.github.kittinunf.fuel.core.Handler;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.PaymentSession;
import com.stripe.android.PaymentSessionConfig;
import com.stripe.android.PaymentSessionData;
import com.stripe.android.Stripe;
import com.stripe.android.model.Card;
import com.stripe.android.model.ConfirmPaymentIntentParams;
import com.stripe.android.model.PaymentMethodCreateParams;
import com.stripe.android.model.ShippingInformation;
import com.stripe.android.model.ShippingMethod;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaymentsActivity extends AppCompatActivity {

    PaymentSheet paymentSheet;
    String paymentIntentClientSecret;
    PaymentSheet.CustomerConfiguration customerConfig;

    Button BTNGetRegular, BTNGetPremium;
    ProgressBar progressBar;

    FirebaseAuth auth;
    FirebaseFirestore db;
    int packagePressed = 0;
    int available_listings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payments);

        BTNGetRegular = findViewById(R.id.BTNGetRegular);
        BTNGetPremium = findViewById(R.id.BTNGetPremium);
        progressBar = findViewById(R.id.progressBar);


        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        available_listings = Integer.parseInt(getIntent().getStringExtra("available_listings"));

        paymentSheet = new PaymentSheet(this, this::onPaymentSheetResult);





        BTNGetRegular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                getDetailsForRegular();

            }
        });

        BTNGetPremium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                getDetailsForPremium();

            }
        });



    }

    void getDetailsForRegular(){

        Fuel.INSTANCE.post("https://us-central1-bigstride-cloudfunctions.cloudfunctions.net/createPaymentSheet", null).responseString(new Handler<String>() {
            @Override
            public void success(String s) {
                try {
                    final JSONObject result = new JSONObject(s);
                    customerConfig = new PaymentSheet.CustomerConfiguration(
                            result.getString("customer"),
                            result.getString("ephemeralKey")
                    );
                    paymentIntentClientSecret = result.getString("paymentIntent");
                    PaymentConfiguration.init(getApplicationContext(), result.getString("publishableKey"));

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showStripePaymentSheet();
                            packagePressed = 1;
                            progressBar.setVisibility(View.GONE);


                        }
                    });


                } catch (JSONException e) { /* handle error */ }
            }

            @Override
            public void failure(@NonNull FuelError fuelError) {

            }
        });
    }

    void getDetailsForPremium(){
        Fuel.INSTANCE.post("https://us-central1-bigstride-cloudfunctions.cloudfunctions.net/createPaymentSheetPro", null).responseString(new Handler<String>() {
            @Override
            public void success(String s) {
                try {
                    final JSONObject result = new JSONObject(s);
                    customerConfig = new PaymentSheet.CustomerConfiguration(
                            result.getString("customer"),
                            result.getString("ephemeralKey")
                    );
                    paymentIntentClientSecret = result.getString("paymentIntent");
                    PaymentConfiguration.init(getApplicationContext(), result.getString("publishableKey"));

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showStripePaymentSheet();
                            packagePressed = 2;
                            progressBar.setVisibility(View.GONE);
                        }
                    });

                } catch (JSONException e) { /* handle error */ }
            }

            @Override
            public void failure(@NonNull FuelError fuelError) {

            }
        });
    }

    void showStripePaymentSheet(){
        final PaymentSheet.Configuration configuration = new PaymentSheet.Configuration.Builder("BigStride")
                .customer(customerConfig)
                .allowsDelayedPaymentMethods(true)
                .build();
        paymentSheet.presentWithPaymentIntent(
                paymentIntentClientSecret,
                configuration
        );
    }

    void onPaymentSheetResult(final PaymentSheetResult paymentSheetResult) {
        try{
            if (paymentSheetResult instanceof PaymentSheetResult.Canceled) {
                Log.d("STRIPE", "Canceled");
                Toast.makeText(this, "Payment Canceled", Toast.LENGTH_SHORT).show();
            } else if (paymentSheetResult instanceof PaymentSheetResult.Failed) {
                Log.e("STRIPE", "Got error: ", ((PaymentSheetResult.Failed) paymentSheetResult).getError());
                Toast.makeText(this, "Error :"+((PaymentSheetResult.Failed) paymentSheetResult).getError() , Toast.LENGTH_SHORT).show();
            } else if (paymentSheetResult instanceof PaymentSheetResult.Completed) {
                // Display for example, an order confirmation screen
                Log.d("STRIPE", "Completed");

                int addNewListingCount = 0;
                if(packagePressed == 1){
                    addNewListingCount = 3;
                }
                else if(packagePressed == 2){
                    addNewListingCount = 15;
                }

                Map<String, Object> availableListingsUpdate = new HashMap<>();
                availableListingsUpdate.put("available_listings", available_listings + addNewListingCount);

                db.collection("CompanyProfileDetails").document(auth.getCurrentUser().getUid())
                        .update(availableListingsUpdate)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(PaymentsActivity.this, "Payment Successful", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override public void onFailure(@NonNull Exception e)
                            {
                                Toast.makeText(PaymentsActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        }
        catch (Exception e){
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }

    }



}