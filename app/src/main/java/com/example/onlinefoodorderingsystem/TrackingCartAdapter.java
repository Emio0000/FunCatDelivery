package com.example.onlinefoodorderingsystem;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TrackingCartAdapter extends RecyclerView.Adapter<TrackingCartAdapter.ViewHolder> {

    private List<CartItem> items;

    public TrackingCartAdapter(List<CartItem> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_tracking_cart, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CartItem item = items.get(position);
        holder.name.setText(item.getItemName());
        holder.price.setText(String.format("RM %.2f", item.getItemPrice()));
        holder.quantity.setText("x" + item.getQuantity());

        // ✅ Load the correct image based on item name
        String itemName = item.getItemName().toLowerCase();
        int imageResId = R.drawable.logo; // fallback image

        switch (itemName) {
            case "chicken chop":
                imageResId = R.drawable.chicken;
                break;
            case "lamb grilled":
                imageResId = R.drawable.lamb;
                break;
            case "double burger":
                imageResId = R.drawable.burger;
                break;
            case "deer satay":
                imageResId = R.drawable.satay;
                break;
            case "salad":
                imageResId = R.drawable.salad;
                break;
            case "salmon salad":
                imageResId = R.drawable.salmon;
                break;
            case "orange juice":
                imageResId = R.drawable.drink1;
                break;
            case "dragon juice":
                imageResId = R.drawable.drink2;
                break;
            case "ice matcha":
                imageResId = R.drawable.drink3;
                break;
            case "vanilla coffe":
                imageResId = R.drawable.drink4;
                break;
        }

        holder.image.setImageResource(imageResId);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, price, quantity;
        ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.itemText);
            price = itemView.findViewById(R.id.itemPrice);
            quantity = itemView.findViewById(R.id.itemQuantity);
            image = itemView.findViewById(R.id.itemImage);
        }
    }
}
