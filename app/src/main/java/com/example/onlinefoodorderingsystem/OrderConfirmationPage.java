package com.example.onlinefoodorderingsystem;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class OrderConfirmationPage extends AppCompatActivity {

    private ListView orderConfirmationListView;
    private TextView tvConfirmationMessage, tvPaymentMethod, tvTotalAmount;
    private Button btnGoToMenu, btnGoToHome, btnTrackOrder;

    private ArrayList<CartItem> cartItems;

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

        // Get cartItems and payment method from intent
        Intent intent = getIntent();
        cartItems = (ArrayList<CartItem>) intent.getSerializableExtra("cartItems");
        String paymentMethod = intent.getStringExtra("paymentMethod");

        // Fallback if intent data is missing
        if (cartItems == null) cartItems = new ArrayList<>();
        if (paymentMethod == null) paymentMethod = "Unknown";

        // Show confirmation info
        tvConfirmationMessage.setText("Thank you for your order!");
        tvPaymentMethod.setText("Paid with: " + paymentMethod);
        tvTotalAmount.setText("Total: " + calculateTotal(cartItems));

        // Display selected menu items in the ListView
        ArrayList<String> itemDetails = new ArrayList<>();
        for (CartItem item : cartItems) {
            itemDetails.add(item.getItemName() + " x" + item.getQuantity() + " - RM" + String.format("%.2f", item.getItemPrice()));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, itemDetails);
        orderConfirmationListView.setAdapter(adapter);

        // Go to Menu
        btnGoToMenu.setOnClickListener(v -> {
            Intent i = new Intent(OrderConfirmationPage.this, MenuPage.class);
            startActivity(i);
        });

        // Go to Home
        btnGoToHome.setOnClickListener(v -> {
            Intent i = new Intent(OrderConfirmationPage.this, HomePage.class);
            startActivity(i);
        });

        // Track Order
        btnTrackOrder.setOnClickListener(v -> {
            SharedPreferences sharedPreferences = getSharedPreferences("OrderTrackingPrefs", MODE_PRIVATE);
            sharedPreferences.edit()
                    .putString("trackingFood", cartItems.size() > 0 ? cartItems.get(0).getItemName() : "Food") // example
                    .putInt("trackingStatus", 0)
                    .apply();

            Intent i = new Intent(OrderConfirmationPage.this, OrderTrackingPage.class);
            startActivity(i);
        });
    }

    private String calculateTotal(ArrayList<CartItem> items) {
        double total = 0;
        for (CartItem item : items) {
            total += item.getItemPrice() * item.getQuantity();
        }
        return "RM" + String.format("%.2f", total);
    }
}
