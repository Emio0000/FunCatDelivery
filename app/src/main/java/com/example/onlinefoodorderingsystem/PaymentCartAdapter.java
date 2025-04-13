package com.example.onlinefoodorderingsystem;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PaymentCartAdapter extends RecyclerView.Adapter<PaymentCartAdapter.ViewHolder> {

    private final List<CartItem> cartItems;

    public PaymentCartAdapter(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemName, itemPriceQty;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.tvItemName);
            itemPriceQty = itemView.findViewById(R.id.tvItemPriceQty);
        }
    }

    @NonNull
    @Override
    public PaymentCartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_payment_cart, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentCartAdapter.ViewHolder holder, int position) {
        CartItem item = cartItems.get(position);
        holder.itemName.setText(item.getItemName());
        holder.itemPriceQty.setText("RM" + String.format("%.2f", item.getItemPrice()) + " x" + item.getQuantity());
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }
}
