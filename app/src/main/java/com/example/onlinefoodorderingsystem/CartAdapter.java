package com.example.onlinefoodorderingsystem;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private List<CartItem> cartItems;

    public CartAdapter(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem item = cartItems.get(position);
        holder.tvName.setText(item.getItemName());
        holder.tvPrice.setText(String.format("$%.2f", item.getItemPrice()));
        holder.tvQuantity.setText("x" + item.getQuantity());

        // Add content description for accessibility
        holder.itemView.setContentDescription(
                item.getItemName() + ", Quantity: " + item.getQuantity() + ", Price: RM " + String.format("%.2f", item.getItemPrice())
        );
    }


    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPrice, tvQuantity;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.itemName);
            tvPrice = itemView.findViewById(R.id.itemPrice);
            tvQuantity = itemView.findViewById(R.id.itemQuantity);
        }
    }
}