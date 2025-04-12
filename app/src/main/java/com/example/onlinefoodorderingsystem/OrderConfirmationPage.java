package com.example.onlinefoodorderingsystem;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class OrderConfirmationPage extends AppCompatActivity {

    private ListView orderConfirmationListView;
    private TextView tvConfirmationMessage, tvPaymentMethod, tvTotalAmount;
    private Button btnGoToMenu, btnGoToHome, btnTrackOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmation);

        // Initialize views
        orderConfirmationListView = findViewById(R.id.orderConfirmationListView);
        tvConfirmationMessage = findViewById(R.id.tvConfirmationMessage);
        tvPaymentMethod = findViewById(R.id.tvPaymentMethod);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        btnGoToMenu = findViewById(R.id.btnGoToMenu);
        btnGoToHome = findViewById(R.id.btnGoToHome);
        btnTrackOrder = findViewById(R.id.btnTrackOrder);

        // Example display (you should replace these with real values)
        tvConfirmationMessage.setText("Thank you for your order!");
        tvPaymentMethod.setText("Paid with: Credit Card");
        tvTotalAmount.setText("Total: $25.00");

        // Handle Go to Menu button
        btnGoToMenu.setOnClickListener(v -> {
            Intent intent = new Intent(OrderConfirmationPage.this, MenuPage.class);
            startActivity(intent);
        });

        // Handle Go to Home button
        btnGoToHome.setOnClickListener(v -> {
            Intent intent = new Intent(OrderConfirmationPage.this, HomePage.class);
            startActivity(intent);
        });

        // Handle Track Order button
        btnTrackOrder.setOnClickListener(v -> {
            String selectedFoodName = getSelectedFoodName(); // Replace with actual logic to get the selected food name

            // Save tracking data in SharedPreferences
            SharedPreferences sharedPreferences = getSharedPreferences("OrderTrackingPrefs", MODE_PRIVATE);
            sharedPreferences.edit()
                    .putString("trackingFood", selectedFoodName)  // Store the actual food name
                    .putInt("trackingStatus", 0) // Start at the first status (Order Placed)
                    .apply();

            // Launch OrderTrackingPage
            Intent intent = new Intent(OrderConfirmationPage.this, OrderTrackingPage.class);
            startActivity(intent);
        });
    }

    // Replace this with your actual selected food logic
    private String getSelectedFoodName() {
        // Get the food name from the data or selection the user made.
        // Example: this could be the food name selected on the menu page or passed through the intent.

        // For the sake of this example, we'll assume the user selected "Pizza"
        return "Pizza"; // Example, replace with actual dynamic value
    }
}
