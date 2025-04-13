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
    private static final String KEY_STATUS = "trackingStatus";
    private static final String KEY_FOOD = "trackingFood";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_tracking);

        // Toast to confirm screen loaded
        Toast.makeText(this, "OrderTrackingPage Loaded", Toast.LENGTH_SHORT).show();

        // Init Views
        tvFoodName = findViewById(R.id.tvFoodName);
        tvStatus = findViewById(R.id.tvStatus);
        tvSubtotal = findViewById(R.id.tvSubtotal);
        tvDeliveryFee = findViewById(R.id.tvDeliveryFee);
        tvTotal = findViewById(R.id.tvTotal);
        btnBackToHome = findViewById(R.id.btnBackHome);
        btnBackToConfirmation = findViewById(R.id.btnContactSupport);
        rvCartItems = findViewById(R.id.rvCartItems);

        btnBackToConfirmation.setText("Back to Confirmation");

        // Load from SharedPreferences
        // You've already uploaded the correct version!
// Just make sure the following works:

        // Already uploaded working version — just ensure orderData is loaded:
        SharedPreferences orderPrefs = getSharedPreferences("OrderPreferences", MODE_PRIVATE);
        String orderData = orderPrefs.getString("orderData", "");
        ArrayList<CartItem> cartItems = parseOrderData(orderData);

        rvCartItems.setLayoutManager(new LinearLayoutManager(this));
        rvCartItems.setAdapter(new CartAdapter(cartItems));


// Show total
        double subtotal = calculateSubtotal(cartItems);
        double deliveryFee = 5.00;
        double total = subtotal + deliveryFee;

        tvSubtotal.setText(String.format("RM%.2f", subtotal));
        tvDeliveryFee.setText(String.format("RM%.2f", deliveryFee));
        tvTotal.setText(String.format("RM%.2f", total));
        
        // Status tracking
        if (tvFoodName != null) {
            tvFoodName.setText("Order Summary");
            updateStatus(currentStep);
            if (currentStep < statusSteps.length - 1) {
                startStatusProgression();
            }
        } else {
            tvFoodName.setText("No active order to track");
            tvStatus.setText("");
        }

        // Back buttons
        btnBackToHome.setOnClickListener(v -> {
            startActivity(new Intent(OrderTrackingPage.this, HomePage.class));
            finish();
        });

        btnBackToConfirmation.setOnClickListener(v -> {
            startActivity(new Intent(OrderTrackingPage.this, OrderConfirmationPage.class));
            finish();
        });
    }

    private ArrayList<CartItem> parseOrderData(String orderData) {
        ArrayList<CartItem> items = new ArrayList<>();
        if (!orderData.isEmpty()) {
            String[] itemStrings = orderData.split(":");
            for (String itemString : itemStrings) {
                String[] parts = itemString.split(";");
                if (parts.length == 3) {
                    try {
                        items.add(new CartItem(
                                parts[0],
                                Double.parseDouble(parts[1]),
                                Integer.parseInt(parts[2])
                        ));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return items;
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
                    sharedPreferences.edit().putInt(KEY_STATUS, currentStep).apply();
                    handler.postDelayed(this, 7000);
                }
            }
        };
        handler.postDelayed(statusUpdater, 7000);
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
