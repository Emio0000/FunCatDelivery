package com.example.onlinefoodorderingsystem;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;

public class PaymentPage extends Activity {

    private RadioGroup paymentMethodGroup;
    private Button btnSubmitPayment;
    private Button btnBackToCart;
    private TextView txtTotalAmount;
    private ArrayList<CartItem> cartItems;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        // Get the cart items passed from CartPage
        Intent intent = getIntent();
        cartItems = (ArrayList<CartItem>) intent.getSerializableExtra("cartItems");

        // If there are no cart items, display an error message and prevent payment
        if (cartItems == null || cartItems.size() == 0) {
            Toast.makeText(this, "No items in cart. Please add items before proceeding.", Toast.LENGTH_LONG).show();
            finish(); // Exit the payment page if there are no items in the cart
        }

        // Initialize Views
        paymentMethodGroup = findViewById(R.id.paymentMethodGroup);
        btnSubmitPayment = findViewById(R.id.btnSubmitPayment);
        btnBackToCart = findViewById(R.id.btnBackToCart);
        txtTotalAmount = findViewById(R.id.txtTotalAmount);

        // Calculate the total cart value
        double totalAmount = 0;
        for (CartItem item : cartItems) {
            totalAmount += item.getItemPrice() * item.getQuantity();
        }
        txtTotalAmount.setText("Total: $" + totalAmount);

        // Handle the submit payment button click
        btnSubmitPayment.setOnClickListener(v -> {
            int selectedId = paymentMethodGroup.getCheckedRadioButtonId();

            if (selectedId == -1) {
                Toast.makeText(PaymentPage.this, "Please select a payment method", Toast.LENGTH_SHORT).show();
            } else {
                RadioButton selectedRadioButton = findViewById(selectedId);
                String selectedPaymentMethod = selectedRadioButton.getText().toString();

                // Show confirmation dialog before proceeding to payment
                new AlertDialog.Builder(PaymentPage.this)
                        .setTitle("Confirm Payment")
                        .setMessage("Are you sure you want to proceed with the payment using " + selectedPaymentMethod + "?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            Toast.makeText(PaymentPage.this, "Payment Method: " + selectedPaymentMethod, Toast.LENGTH_SHORT).show();

                            Intent orderIntent = new Intent(PaymentPage.this, OrderConfirmationPage.class);
                            orderIntent.putExtra("paymentMethod", selectedPaymentMethod);
                            orderIntent.putExtra("cartItems", cartItems);
                            startActivity(orderIntent);
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });

        // Handle back button click to CartPage
        btnBackToCart.setOnClickListener(v -> {
            Intent backIntent = new Intent(PaymentPage.this, CartPage.class);
            backIntent.putExtra("cartItems", cartItems); // Send cart back
            startActivity(backIntent);
            finish(); // Optional: Close PaymentPage so user doesn't return here accidentally
        });
    }
}

