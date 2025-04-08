package com.example.onlinefoodorderingsystem;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
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
                selectedMethod = "PayPal E-Wallet";
            } else if (rbCod.isChecked()) {
                selectedMethod = "Cash on Delivery";
            } else {
                selectedMethod = null;
            }

            // Check if a payment method is selected
            if (selectedMethod == null) {
                Toast.makeText(this, "Please select a payment method", Toast.LENGTH_SHORT).show();
            } else {
                // Show the confirmation dialog with the appropriate message
                String confirmationMessage = "Are you sure you want to proceed with payment using " + selectedMethod + "?";

                // Show the confirmation dialog
                new AlertDialog.Builder(PaymentPage.this)
                        .setTitle("Confirm Payment")
                        .setMessage(confirmationMessage)
                        .setPositiveButton("Yes", (dialog, which) -> {
                            // Payment successful - Show a Toast message
                            Toast.makeText(this, "Payment Successful using " + selectedMethod, Toast.LENGTH_LONG).show();

                            // Save the order details to SharedPreferences
                            saveOrderToSharedPreferences(cartItems, selectedMethod);

                            // Pass the selected payment method and cart items to the OrderConfirmationPage
                            Intent confirmationIntent = new Intent(PaymentPage.this, OrderConfirmationPage.class);
                            confirmationIntent.putExtra("paymentMethod", selectedMethod);
                            confirmationIntent.putExtra("cartItems", cartItems); // Pass the cart items to the confirmation page
                            startActivity(confirmationIntent); // Navigate to OrderConfirmationPage
                            finish(); // Close PaymentPage
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });
    }

    private void saveOrderToSharedPreferences(ArrayList<CartItem> cartItems, String paymentMethod) {
        // Get the shared preferences editor
        SharedPreferences sharedPreferences = getSharedPreferences("OrderPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Convert cart items into a JSON string (You could also use other methods like Gson)
        StringBuilder orderData = new StringBuilder();
        for (CartItem item : cartItems) {
            orderData.append(item.getItemName()).append(" x").append(item.getQuantity()).append(" - $")
                    .append(item.getItemPrice()).append(" each\n");
        }

        // Save the order data and payment method
        editor.putString("orderData", orderData.toString());
        editor.putString("paymentMethod", paymentMethod);
        editor.apply();
    }
}
