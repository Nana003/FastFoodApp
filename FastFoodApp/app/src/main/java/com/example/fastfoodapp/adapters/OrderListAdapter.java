package com.example.fastfoodapp.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fastfoodapp.R;
import com.example.fastfoodapp.activities.MainActivity;
import com.example.fastfoodapp.fragments.FoodDetailsFragment;
import com.example.fastfoodapp.fragments.OrderDetailsFragment;
import com.example.fastfoodapp.models.Order;
import com.example.fastfoodapp.models.OrderDetails;

import java.util.List;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.ViewHolder> {
    List<Order> orderModelList;
    Context context;
    LayoutInflater mInflater;

    public OrderListAdapter(List<Order> orderModelList, Context context) {
        this.orderModelList = orderModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = mInflater.from(parent.getContext()).inflate(R.layout.child_past_orders_item, parent , false);
        return new OrderListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(orderModelList.get(position).getImageUrl()).into(holder.thumbPic);
        holder.txtOrderNo.setText("Order#: " + String.valueOf(orderModelList.get(position).getId()));
        holder.txtTotal.setText("Price: " + String.valueOf(orderModelList.get(position).getTotal()));
        //create new bundle and put a key/value
        Bundle bundle = new Bundle();
        bundle.putLong("OrderNo", orderModelList.get(position).getId());
        holder.btnSeeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Set FoodDetailsFragment Arguments
                OrderDetailsFragment orderDetailsFragment = new OrderDetailsFragment();
                orderDetailsFragment.setArguments(bundle);

                //open fragments
                MainActivity.fragmentManager.beginTransaction().replace(R.id.fragmentContainerView, orderDetailsFragment).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView thumbPic;
        TextView txtOrderNo, txtTotal;
        Button btnSeeOrder;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbPic = itemView.findViewById(R.id.imageViewOrderPicture);
            txtOrderNo = itemView.findViewById(R.id.textViewOrderId);
            txtTotal = itemView.findViewById(R.id.textViewOrderTotalPrice);
            btnSeeOrder = itemView.findViewById(R.id.buttonSeeDetails);
        }
    }
}
