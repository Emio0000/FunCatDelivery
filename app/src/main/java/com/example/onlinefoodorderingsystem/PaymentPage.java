package com.example.onlinefoodorderingsystem;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;

import java.util.ArrayList;

public class PaymentPage extends Activity {

    private RadioButton rbCreditCard, rbPayPal, rbCod;
    private Button btnSubmitPayment, btnBack;
    private TextView txtTotalAmount;
    private RecyclerView rvCartItems;
    private ArrayList<CartItem> cartItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        cartItems = (ArrayList<CartItem>) getIntent().getSerializableExtra("cartItems");

        rbCreditCard = findViewById(R.id.rb_credit_card);
        rbPayPal = findViewById(R.id.rb_paypal);
        rbCod = findViewById(R.id.rb_cod);
        btnSubmitPayment = findViewById(R.id.btnSubmitPayment);
        btnBack = findViewById(R.id.btnBack);
        txtTotalAmount = findViewById(R.id.txtTotalAmount);
        rvCartItems = findViewById(R.id.rvCartItems);

        rvCartItems.setLayoutManager(new LinearLayoutManager(this));
        rvCartItems.setAdapter(new PaymentCartAdapter(cartItems));

        double total = 0;
        for (CartItem item : cartItems) {
            total += item.getItemPrice() * item.getQuantity();
        }
        txtTotalAmount.setText("Total: RM" + String.format("%.2f", total));

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

        btnBack.setOnClickListener(v -> finish());

        btnSubmitPayment.setOnClickListener(v -> {
            String selectedMethod;
            if (rbCreditCard.isChecked()) selectedMethod = "Credit Card";
            else if (rbPayPal.isChecked()) selectedMethod = "PayPal";
            else if (rbCod.isChecked()) selectedMethod = "Cash on Delivery";
            else {
                selectedMethod = null;
            }

            if (selectedMethod == null) {
                Toast.makeText(this, "Please select a payment method", Toast.LENGTH_SHORT).show();
                return;
            }

            new AlertDialog.Builder(this)
                    .setTitle("Confirm Payment")
                    .setMessage("Pay using " + selectedMethod + "?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        saveOrderToSharedPreferences(cartItems, selectedMethod);
                        startActivity(new Intent(PaymentPage.this, OrderConfirmationPage.class));
                    })
                    .setNegativeButton("No", null)
                    .show();
        });
    }

    private void saveOrderToSharedPreferences(ArrayList<CartItem> cartItems, String paymentMethod) {
        SharedPreferences prefs = getSharedPreferences("OrderPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        Gson gson = new Gson();
        String cartJson = gson.toJson(cartItems);

        editor.putString("orderData", cartJson);
        editor.putString("paymentMethod", paymentMethod);
        editor.apply();
    }
}