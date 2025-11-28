package com.example.personalrestaurantguide;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class DetailsActivity extends AppCompatActivity {

    // Keys for Intent extras
    public static final String EXTRA_NAME = "extra_name";
    public static final String EXTRA_ADDRESS = "extra_address";
    public static final String EXTRA_PHONE = "extra_phone";
    public static final String EXTRA_DESCRIPTION = "extra_description";
    public static final String EXTRA_LAT = "extra_lat";
    public static final String EXTRA_LNG = "extra_lng";

    private String name;
    private String address;
    private String phone;
    private String description;
    private double latitude;
    private double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        // Read data from Intent
        Intent intent = getIntent();
        if (intent == null || !intent.hasExtra(EXTRA_NAME)) {
            Toast.makeText(this, "Missing restaurant data", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        name = intent.getStringExtra(EXTRA_NAME);
        address = intent.getStringExtra(EXTRA_ADDRESS);
        phone = intent.getStringExtra(EXTRA_PHONE);
        description = intent.getStringExtra(EXTRA_DESCRIPTION);
        latitude = intent.getDoubleExtra(EXTRA_LAT, 0);
        longitude = intent.getDoubleExtra(EXTRA_LNG, 0);

        // Bind views
        TextView tvName = findViewById(R.id.tvRestaurantName);
        TextView tvAddress = findViewById(R.id.tvRestaurantAddress);
        TextView tvPhone = findViewById(R.id.tvRestaurantPhone);
        TextView tvDesc = findViewById(R.id.tvRestaurantDescription);
        Button btnDirections = findViewById(R.id.btnDirections);
        Button btnShare = findViewById(R.id.btnShare);

        // Set data
        tvName.setText(name);
        tvAddress.setText(address);
        tvPhone.setText(phone != null ? phone : "N/A");
        tvDesc.setText(description != null ? description : "");

        // Directions button
        btnDirections.setOnClickListener(v -> openDirections());

        // Share button
        btnShare.setOnClickListener(v -> shareRestaurant());
    }

    private void openDirections() {
        // Try the Google Maps app
        String uri = "google.navigation:q=" + latitude + "," + longitude + "&mode=d";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setPackage("com.google.android.apps.maps");

        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            // Fallback: open browser Google Maps
            String webUri = "https://www.google.com/maps/dir/?api=1&destination=" + latitude + "," + longitude;
            Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(webUri));
            startActivity(webIntent);
        }
    }

    private void shareRestaurant() {
        StringBuilder shareText = new StringBuilder();
        shareText.append("Check out this restaurant:\n");
        shareText.append(name).append("\n\n");
        shareText.append("Address: ").append(address).append("\n");
        if (phone != null && !phone.isEmpty()) {
            shareText.append("Phone: ").append(phone).append("\n");
        }
        if (description != null && !description.isEmpty()) {
            shareText.append("\nDetails:\n").append(description).append("\n");
        }
        shareText.append("\nLocation: https://www.google.com/maps/search/?api=1&query=")
                .append(latitude).append(",").append(longitude);

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Restaurant: " + name);
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText.toString());

        startActivity(Intent.createChooser(shareIntent, "Share restaurant via"));
    }
}