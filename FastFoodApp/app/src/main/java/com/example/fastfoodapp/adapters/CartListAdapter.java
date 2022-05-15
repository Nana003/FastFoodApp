package com.example.fastfoodapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fastfoodapp.R;
import com.example.fastfoodapp.models.Cart;
import com.example.fastfoodapp.models.Food;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class CartListAdapter extends RecyclerView.Adapter<CartListAdapter.ViewHolder> {
    List<Cart> cartModelList;
    Context context;
    FirebaseFirestore db;
    FirebaseAuth auth;
    LayoutInflater mInflater;

    public CartListAdapter(List<Cart> cartModelList, Context context) {
        this.cartModelList = cartModelList;
        this.context = context;
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.from(parent.getContext()).inflate(R.layout.child_cart_item, parent , false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(cartModelList.get(position).getImgUrl()).into(holder.foodPic);
        holder.txtTitle.setText(cartModelList.get(position).getName());
        holder.txtPrice.setText(String.valueOf(cartModelList.get(position).getPrice()));
        holder.txtQuantity.setText(String.valueOf(cartModelList.get(position).getQuantity()));
        holder.cartDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection("Cart").document(auth.getCurrentUser().getUid()).collection("userCart")
                        .whereEqualTo("name", cartModelList.get(holder.getAdapterPosition()).getName())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()){
                                    for (DocumentSnapshot document : task.getResult()){
                                        db.collection("Cart").document(auth.getCurrentUser().getUid()).collection("userCart").document(document.getId()).delete();
                                        cartModelList.remove(cartModelList.get(holder.getAdapterPosition()));
                                        notifyDataSetChanged();
                                        Toast.makeText(context, "Item Deleted", Toast.LENGTH_SHORT).show();
                                    }
                                }else {
                                    Toast.makeText(context, "Error:" + task.getException(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView foodPic, cartDelete;
        TextView txtTitle, txtPrice, txtQuantity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            foodPic = itemView.findViewById(R.id.imageViewCart);
            txtTitle = itemView.findViewById(R.id.textViewCartTitle);
            txtPrice = itemView.findViewById(R.id.textViewCartPrice);
            txtQuantity = itemView.findViewById(R.id.textViewCartQuantity);
            cartDelete = itemView.findViewById(R.id.imageViewDelete);
        }
    }
}
