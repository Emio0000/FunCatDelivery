package com.example.onlinefoodorderingsystem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class OrderConfirmationPage extends Activity {

    private ListView orderConfirmationListView;
    private TextView tvConfirmationMessage, tvPaymentMethod, tvTotalAmount;
    private Button btnGoToMenu, btnGoToHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmation);

        // Find views
        orderConfirmationListView = findViewById(R.id.orderConfirmationListView);
        tvConfirmationMessage = findViewById(R.id.tvConfirmationMessage);
        tvPaymentMethod = findViewById(R.id.tvPaymentMethod);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        btnGoToMenu = findViewById(R.id.btnGoToMenu);
        btnGoToHome = findViewById(R.id.btnGoToHome);

        // Get the cart items and payment method passed from PaymentPage
        Intent intent = getIntent();
        ArrayList<CartItem> cartItems = (ArrayList<CartItem>) intent.getSerializableExtra("cartItems");
        String paymentMethod = intent.getStringExtra("paymentMethod");

        // Check if the cart items are not null
        if (cartItems == null) {
            cartItems = new ArrayList<>();
        }

        // Set the confirmation message and payment method
        tvConfirmationMessage.setText("Thank you for your order!");
        tvPaymentMethod.setText("Payment Method: " + paymentMethod);

        // Calculate the total order amount
        double totalAmount = 0;
        ArrayList<String> orderSummary = new ArrayList<>();
        for (CartItem item : cartItems) {
            double itemTotal = item.getItemPrice() * item.getQuantity();
            totalAmount += itemTotal;
            // Create a detailed order summary for each item
            String itemDetails = item.getItemName() + " x" + item.getQuantity() + " - $" + item.getItemPrice() + " each (Total: $" + itemTotal + ")";
            orderSummary.add(itemDetails);
        }

        // Display the total order amount
        tvTotalAmount.setText("Total: $" + totalAmount);

        // Set up ArrayAdapter to display the order summary in the ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, orderSummary);
        orderConfirmationListView.setAdapter(adapter);

        // Handle back to menu button click
        btnGoToMenu.setOnClickListener(v -> {
            Intent goToMenuIntent = new Intent(OrderConfirmationPage.this, MenuPage.class);
            startActivity(goToMenuIntent);
            finish(); // Close the OrderConfirmationPage
        });

        // Handle back to home button click
        btnGoToHome.setOnClickListener(v -> {
            Intent goToHomeIntent = new Intent(OrderConfirmationPage.this, HomePage.class);
            startActivity(goToHomeIntent);
            finish(); // Close the OrderConfirmationPage
        });
    }
}
