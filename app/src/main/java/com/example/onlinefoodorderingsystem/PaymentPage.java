package com.example.onlinefoodorderingsystem;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;

public class PaymentPage extends Activity {

    private RadioButton rbCreditCard, rbPayPal, rbCod;
    private Button btnSubmitPayment;
    private TextView txtTotalAmount;
    private ArrayList<CartItem> cartItems;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        // Get cart items from intent
        Intent intent = getIntent();
        cartItems = (ArrayList<CartItem>) intent.getSerializableExtra("cartItems");

        if (cartItems == null || cartItems.isEmpty()) {
            Toast.makeText(this, "No items in cart. Please add items before proceeding.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // Initialize Views
        rbCreditCard = findViewById(R.id.rb_credit_card);
        rbPayPal = findViewById(R.id.rb_paypal);
        rbCod = findViewById(R.id.rb_cod);
        btnSubmitPayment = findViewById(R.id.btnSubmitPayment);
        txtTotalAmount = findViewById(R.id.txtTotalAmount);

        // Calculate total price
        double total = 0;
        for (CartItem item : cartItems) {
            total += item.getItemPrice() * item.getQuantity();
        }
        txtTotalAmount.setText("Total: $" + String.format("%.2f", total));

        // Ensure only one radio button can be selected at a time
        rbCreditCard.setOnClickListener(v -> {
            rbCreditCard.setChecked(true);
            rbPayPal.setChecked(false);
            rbCod.setChecked(false);
        });

        rbPayPal.setOnClickListener(v -> {
            rbCreditCard.setChecked(false);
            rbPayPal.setChecked(true);
            rbCod.setChecked(false);
        });

        rbCod.setOnClickListener(v -> {
            rbCreditCard.setChecked(false);
            rbPayPal.setChecked(false);
            rbCod.setChecked(true);
        });

        // Confirm payment
        btnSubmitPayment.setOnClickListener(v -> {
            String selectedMethod;

            // Check which payment method is selected
            if (rbCreditCard.isChecked()) {
                selectedMethod = "Credit Card";
            } else if (rbPayPal.isChecked()) {
                selectedMethod = "PayPal E-Wallet"; // Updated message for PayPal
            } else if (rbCod.isChecked()) {
                selectedMethod = "Cash on Delivery";
            } else {
                selectedMethod = null;
            }

            // Check if a payment method is selected
            if (selectedMethod == null) {
                Toast.makeText(this, "Please select a payment method", Toast.LENGTH_SHORT).show();
            } else {
                // Update the pop-up message based on the payment method
                String confirmationMessage = "Are you sure you want to proceed with payment using " + selectedMethod + "?";
                if (rbPayPal.isChecked()) {
                    confirmationMessage = "Are you sure you want to proceed with payment PayPal E-Wallet?"; // Custom message for PayPal
                }

                // Show the confirmation dialog with the appropriate message
                new AlertDialog.Builder(PaymentPage.this)
                        .setTitle("Confirm Payment")
                        .setMessage(confirmationMessage)
                        .setPositiveButton("Yes", (dialog, which) -> {
                            // Payment successful - Show a Toast message
                            Toast.makeText(this, "Payment Successful using " + selectedMethod, Toast.LENGTH_LONG).show();

                            // Pass the selected payment method and cart items to the next activity
                            Intent orderIntent = new Intent(PaymentPage.this, OrderConfirmationPage.class);
                            orderIntent.putExtra("paymentMethod", selectedMethod);
                            orderIntent.putExtra("cartItems", cartItems);
                            startActivity(orderIntent); // Navigate to Order Confirmation page
                            finish(); // Close PaymentPage
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });
    }
}
