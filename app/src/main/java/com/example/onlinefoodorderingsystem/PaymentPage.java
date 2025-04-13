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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PaymentPage extends Activity {

    private RadioButton rbCreditCard, rbPayPal, rbCod;
    private Button btnSubmitPayment, btnBack;
    private TextView txtTotalAmount;
    private RecyclerView rvCartItems;
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

        Toast.makeText(this, "Opened PaymentPage with " + cartItems.size() + " items", Toast.LENGTH_SHORT).show();

        // Initialize Views
        rbCreditCard = findViewById(R.id.rb_credit_card);
        rbPayPal = findViewById(R.id.rb_paypal);
        rbCod = findViewById(R.id.rb_cod);
        btnSubmitPayment = findViewById(R.id.btnSubmitPayment);
        btnBack = findViewById(R.id.btnBack);
        txtTotalAmount = findViewById(R.id.txtTotalAmount);
        rvCartItems = findViewById(R.id.rvCartItems);

        // Setup RecyclerView
        rvCartItems.setLayoutManager(new LinearLayoutManager(this));
        PaymentCartAdapter adapter = new PaymentCartAdapter(cartItems);
        rvCartItems.setAdapter(adapter);

        // Calculate total price
        double total = 0;
        for (CartItem item : cartItems) {
            total += item.getItemPrice() * item.getQuantity();
        }
        txtTotalAmount.setText("Total: RM" + String.format("%.2f", total));

        // RadioButton logic
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

        // Back button logic
        btnBack.setOnClickListener(v -> finish());

        // Submit Payment
        btnSubmitPayment.setOnClickListener(v -> {
            String selectedMethod;

            if (rbCreditCard.isChecked()) {
                selectedMethod = "Credit Card";
            } else if (rbPayPal.isChecked()) {
                selectedMethod = "PayPal E-Wallet";
            } else if (rbCod.isChecked()) {
                selectedMethod = "Cash on Delivery";
            } else {
                selectedMethod = null;
            }

            if (selectedMethod == null) {
                Toast.makeText(this, "Please select a payment method", Toast.LENGTH_SHORT).show();
            } else {
                String confirmationMessage = "Are you sure you want to proceed with payment using " + selectedMethod + "?";

                new AlertDialog.Builder(PaymentPage.this)
                        .setTitle("Confirm Payment")
                        .setMessage(confirmationMessage)
                        .setPositiveButton("Yes", (dialog, which) -> {
                            Toast.makeText(this, "Payment Successful using " + selectedMethod, Toast.LENGTH_LONG).show();
                            saveOrderToSharedPreferences(cartItems, selectedMethod);

                            Intent confirmationIntent = new Intent(PaymentPage.this, OrderConfirmationPage.class);
                            confirmationIntent.putExtra("paymentMethod", selectedMethod);
                            confirmationIntent.putExtra("cartItems", cartItems);
                            startActivity(confirmationIntent);
                            finish();
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });
    }

    private void saveOrderToSharedPreferences(ArrayList<CartItem> cartItems, String paymentMethod) {
        SharedPreferences sharedPreferences = getSharedPreferences("OrderPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        StringBuilder orderData = new StringBuilder();
        for (CartItem item : cartItems) {
            orderData.append(item.getItemName())
                    .append(" x").append(item.getQuantity())
                    .append(" - RM").append(String.format("%.2f", item.getItemPrice()))
                    .append(" each\n");
        }

        editor.putString("orderData", orderData.toString());
        editor.putString("paymentMethod", paymentMethod);
        editor.apply();
    }
}
