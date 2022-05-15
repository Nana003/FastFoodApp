package com.example.fastfoodapp.adapters;

import android.content.Context;
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
import com.example.fastfoodapp.models.Order;
import com.example.fastfoodapp.models.OrderDetails;

import java.util.List;

public class OrderDetailsAdapter extends RecyclerView.Adapter<OrderDetailsAdapter.ViewHolder>{
    List<OrderDetails> orderDetailsModelList;
    Context context;
    LayoutInflater mInflater;

    public OrderDetailsAdapter(List<OrderDetails> orderDetailsModelList, Context context) {
        this.orderDetailsModelList = orderDetailsModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public OrderDetailsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.from(parent.getContext()).inflate(R.layout.child_order_details_item, parent , false);
        return new OrderDetailsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDetailsAdapter.ViewHolder holder, int position) {
        Glide.with(context).load(orderDetailsModelList.get(position).getImgUrl()).into(holder.thumbPic);
        holder.txtOrderFoodName.setText(orderDetailsModelList.get(position).getName());
        holder.txtFoodQuantity.setText(String.valueOf(orderDetailsModelList.get(position).getQuantity()));
        holder.txtFoodPrice.setText(String.valueOf(orderDetailsModelList.get(position).getPrice()));
        holder.txtFoodTotal.setText(orderDetailsModelList.get(position).getTotalPriceItem());
    }

    @Override
    public int getItemCount() {
        return orderDetailsModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView thumbPic;
        TextView txtOrderFoodName, txtFoodQuantity, txtFoodTotal, txtFoodPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbPic = itemView.findViewById(R.id.imageViewOrderDPicture);
            txtOrderFoodName = itemView.findViewById(R.id.textViewOrderDname);
            txtFoodQuantity = itemView.findViewById(R.id.textViewOrderDQuantity);
            txtFoodTotal = itemView.findViewById(R.id.textViewOrderDTotal);
            txtFoodPrice = itemView.findViewById(R.id.textViewOrderDPrice);
        }
    }
}
