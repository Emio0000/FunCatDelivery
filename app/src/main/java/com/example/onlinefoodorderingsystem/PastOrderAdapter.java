package com.example.onlinefoodorderingsystem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class PastOrderAdapter extends ArrayAdapter<String> {

    public PastOrderAdapter(Context context, ArrayList<String> orders) {
        super(context, 0, orders);  // Passing context and orders to the adapter
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        String orderDetails = getItem(position);

        // Check if an existing view is being reused, otherwise inflate a new view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.past_order_item, parent, false);
        }

        // Lookup view for data population
        TextView orderDetailsTextView = convertView.findViewById(R.id.tvOrderDetails);
        TextView orderStatusTextView = convertView.findViewById(R.id.tvOrderStatus);

        // Populate the data into the template view using the order details
        orderDetailsTextView.setText(orderDetails);
        orderStatusTextView.setText("Status: Placed"); // Example: You may update this based on real-time order status

        // Return the completed view to render on screen
        return convertView;
    }
}
