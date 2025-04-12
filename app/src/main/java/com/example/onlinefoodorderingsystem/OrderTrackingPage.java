package com.example.onlinefoodorderingsystem;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class OrderTrackingPage extends AppCompatActivity {

    private TextView tvFoodName, tvStatus;
    private Button btnDeleteSelected, btnBackToHome, btnBackToConfirmation;

    private int currentStep = 0;
    private final String[] statusSteps = {
            "\uD83D\uDFE1 Order Placed",        // yellow
            "\uD83D\uDFE0 Preparing Food",     // orange
            "\uD83D\uDD35 Out for Delivery",   // blue
            "\uD83D\uDFE2 Delivered"           // green
    };

    private final Handler handler = new Handler();
    private Runnable statusUpdater;
    private SharedPreferences sharedPreferences;

    private static final String PREF_NAME = "OrderTrackingPrefs";
    private static final String KEY_STATUS = "trackingStatus";
    private static final String KEY_FOOD = "trackingFood";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_tracking);

        // Initialize views
        tvFoodName = findViewById(R.id.tvFoodName);
        tvStatus = findViewById(R.id.tvStatus);
        btnDeleteSelected = findViewById(R.id.btnDeleteSelected);
        btnBackToHome = findViewById(R.id.btnBackHome);
        btnBackToConfirmation = findViewById(R.id.btnBackToConfirmation);

        // Get SharedPreferences
        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        // Retrieve food name and current tracking status from SharedPreferences
        String foodName = sharedPreferences.getString(KEY_FOOD, null);
        currentStep = sharedPreferences.getInt(KEY_STATUS, 0);

        // If food name exists, display it; otherwise, show "Tracking page is empty"
        if (foodName != null) {
            tvFoodName.setText(foodName);
            updateStatus(currentStep);
            if (currentStep < statusSteps.length - 1) {
                startStatusProgression(); // Start updating the status every 7 seconds
            }
        } else {
            tvFoodName.setText("Tracking page is empty");
            tvStatus.setText("");
        }

        // Delete button functionality
        btnDeleteSelected.setOnClickListener(v -> {
            sharedPreferences.edit().clear().apply();  // Clear stored data
            tvFoodName.setText("Tracking page is empty");
            tvStatus.setText("");
            handler.removeCallbacks(statusUpdater);  // Stop the status update
            Toast.makeText(this, "Tracking data deleted", Toast.LENGTH_SHORT).show();
        });

        // Back to Home page
        btnBackToHome.setOnClickListener(v -> {
            Intent intent = new Intent(OrderTrackingPage.this, HomePage.class);
            startActivity(intent);
            finish();
        });

        // Back to OrderConfirmationPage
        btnBackToConfirmation.setOnClickListener(v -> {
            Intent intent = new Intent(OrderTrackingPage.this, OrderConfirmationPage.class);
            startActivity(intent);
            finish();
        });
    }

    private void startStatusProgression() {
        statusUpdater = new Runnable() {
            @Override
            public void run() {
                if (currentStep < statusSteps.length - 1) {
                    currentStep++;
                    updateStatus(currentStep);
                    sharedPreferences.edit().putInt(KEY_STATUS, currentStep).apply();  // Save the updated status
                    handler.postDelayed(this, 7000);  // Update status every 7 seconds
                }
            }
        };
        handler.postDelayed(statusUpdater, 7000);  // Initial delay of 7 seconds
    }

    private void updateStatus(int step) {
        String colorIcon;
        switch (step) {
            case 0:
                colorIcon = "\uD83D\uDFE1"; // yellow
                break;
            case 1:
                colorIcon = "\uD83D\uDFE0"; // orange
                break;
            case 2:
                colorIcon = "\uD83D\uDD35"; // blue
                break;
            case 3:
                colorIcon = "\uD83D\uDFE2"; // green
                break;
            default:
                colorIcon = "⚪";
        }
        tvStatus.setText(colorIcon + " " + statusSteps[step].split(" ", 2)[1]);  // Display status with corresponding color icon
    }
}
