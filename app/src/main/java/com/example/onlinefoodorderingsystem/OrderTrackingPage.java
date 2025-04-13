package com.example.onlinefoodorderingsystem;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class OrderTrackingPage extends AppCompatActivity {

    private TextView tvFoodName, tvStatus, tvSubtotal, tvDeliveryFee, tvTotal;
    private Button btnBackToHome, btnBackToConfirmation;
    private RecyclerView rvCartItems;

    private int currentStep = 0;
    private final String[] statusSteps = {
            "\uD83D\uDFE1 Order Placed",
            "\uD83D\uDFE0 Preparing Food",
            "\uD83D\uDD35 Out for Delivery",
            "\uD83D\uDFE2 Delivered"
    };

    private final Handler handler = new Handler();
    private Runnable statusUpdater;
    private SharedPreferences sharedPreferences;

    private static final String PREF_NAME = "OrderTrackingPrefs";
    private static final String ORDER_PREFS = "OrderPreferences";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_tracking);

        tvFoodName = findViewById(R.id.tvFoodName);
        tvStatus = findViewById(R.id.tvStatus);
        tvSubtotal = findViewById(R.id.tvSubtotal);
        tvDeliveryFee = findViewById(R.id.tvDeliveryFee);
        tvTotal = findViewById(R.id.tvTotal);
        btnBackToHome = findViewById(R.id.btnBackHome);
        btnBackToConfirmation = findViewById(R.id.btnContactSupport);
        rvCartItems = findViewById(R.id.rvCartItems);

        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        SharedPreferences orderPrefs = getSharedPreferences(ORDER_PREFS, MODE_PRIVATE);

        String orderData = orderPrefs.getString("orderData", "");
        if (orderData == null || orderData.isEmpty()) {
            Toast.makeText(this, "No order found. Returning to home.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, HomePage.class));
            finish();
            return;
        }

        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<CartItem>>() {}.getType();
        ArrayList<CartItem> cartItems = gson.fromJson(orderData, type);

        rvCartItems.setLayoutManager(new LinearLayoutManager(this));
        rvCartItems.setAdapter(new TrackingCartAdapter(cartItems)); //

        double subtotal = calculateSubtotal(cartItems);
        double deliveryFee = 5.00;
        double total = subtotal + deliveryFee;

        tvSubtotal.setText(String.format("RM%.2f", subtotal));
        tvDeliveryFee.setText(String.format("RM%.2f", deliveryFee));
        tvTotal.setText(String.format("RM%.2f", total));

        currentStep = sharedPreferences.getInt("trackingStatus", 0);

        // 🔥 Set a static title instead of item name
        tvFoodName.setText("Order Tracking");

        updateStatus(currentStep);

        if (currentStep < statusSteps.length - 1) {
            startStatusProgression();
        }

        btnBackToHome.setOnClickListener(v -> {
            startActivity(new Intent(OrderTrackingPage.this, HomePage.class));
            finish();
        });

        btnBackToConfirmation.setOnClickListener(v -> {
            startActivity(new Intent(OrderTrackingPage.this, OrderConfirmationPage.class));
            finish();
        });
    }

    private double calculateSubtotal(ArrayList<CartItem> items) {
        double subtotal = 0;
        for (CartItem item : items) {
            subtotal += item.getItemPrice() * item.getQuantity();
        }
        return subtotal;
    }

    private void startStatusProgression() {
        statusUpdater = new Runnable() {
            @Override
            public void run() {
                if (currentStep < statusSteps.length - 1) {
                    currentStep++;
                    updateStatus(currentStep);
                    sharedPreferences.edit().putInt("trackingStatus", currentStep).apply();
                    handler.postDelayed(this, 6000);
                }
            }
        };
        handler.postDelayed(statusUpdater, 6000);
    }

    private void updateStatus(int step) {
        if (step >= 0 && step < statusSteps.length) {
            tvStatus.setText(statusSteps[step]);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(statusUpdater);
    }
}
