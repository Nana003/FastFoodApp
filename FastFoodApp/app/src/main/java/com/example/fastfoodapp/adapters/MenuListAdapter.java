package com.example.fastfoodapp.adapters;

import android.content.Context;
import android.os.Bundle;
import android.text.Layout;
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
import com.example.fastfoodapp.activities.MainActivity;
import com.example.fastfoodapp.fragments.FoodDetailsFragment;
import com.example.fastfoodapp.models.Food;
import com.example.fastfoodapp.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Queue;

public class MenuListAdapter extends RecyclerView.Adapter<MenuListAdapter.ViewHolder> {
    List<Food> foodModelList;
    Context context;
    LayoutInflater mInflater;
    int totalPrice = 0;
    int totalQuantity = 1;

    public MenuListAdapter(List<Food> foodModelList, Context context) {
        this.foodModelList = foodModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.from(parent.getContext()).inflate(R.layout.child_menu_card, parent , false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(foodModelList.get(position).getImageUrl()).into(holder.foodPicture);
        holder.txtTitle.setText(foodModelList.get(position).getName());
        holder.txtPrice.setText(String.valueOf(foodModelList.get(position).getPrice()));
        totalQuantity = 1;
        holder.btnIncrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                totalQuantity++;
                holder.txtQuantity.setText(String.valueOf(totalQuantity));
            }
        });
        holder.btnDecrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(totalQuantity > 1) {
                    totalQuantity--;
                    holder.txtQuantity.setText(String.valueOf(totalQuantity));
                }
            }
        });
        holder.btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Write a message to the database
                FirebaseFirestore databaseReference = FirebaseFirestore.getInstance();
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                //get all from food collection where the food name is equal to txtTitle
                databaseReference.collection("Foods")
                        .whereEqualTo("name", holder.txtTitle.getText().toString())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                //if successful
                                if (task.isSuccessful()){
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        //get all from document where cart name is equal to food name
                                        databaseReference.collection("Cart")
                                                .document(userId)
                                                .collection("userCart")
                                                .whereEqualTo("name", document.get("name"))
                                                .get()
                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                        //if result is empty
                                                        if (task.getResult().isEmpty()) {
                                                            //add the new item to cart
                                                            HashMap<String, Object> cartMap = new HashMap<>();
                                                            cartMap.put("name", document.get("name"));
                                                            cartMap.put("price", document.get("price"));
                                                            cartMap.put("quantity", totalQuantity);
                                                            cartMap.put("imgUrl", document.get("imageUrl"));
                                                            //cartMap.put("total", 0);
                                                            databaseReference.collection("Cart").document(userId).collection("userCart").add(cartMap);
                                                            Toast.makeText(context, "Added to Cart", Toast.LENGTH_SHORT).show();
                                                        }else{
                                                            //else, if the product already is in cart, increment quantity
                                                            for (QueryDocumentSnapshot document2 : task.getResult()) {
                                                                databaseReference.collection("Cart").document(userId).collection("userCart").document(document2.getId()).update("quantity", FieldValue.increment(totalQuantity));
                                                                Toast.makeText(context, "Added " + totalQuantity + " " + document2.get("name") + " to Cart", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    }
                                                });
                                    }
                                }else {
                                    Toast.makeText(context, "Error:" + task.getException(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
        holder.txtDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //create new bundle and put a key/value
                Bundle bundle = new Bundle();
                bundle.putString("foodName", holder.txtTitle.getText().toString());

                // Set FoodDetailsFragment Arguments
                FoodDetailsFragment foodDetailsFragment = new FoodDetailsFragment();
                foodDetailsFragment.setArguments(bundle);

                //open fragments
                MainActivity.fragmentManager.beginTransaction().replace(R.id.fragmentContainerView, foodDetailsFragment).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return foodModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView foodPicture;
        TextView txtTitle, txtPrice, txtDetails, txtQuantity;
        ImageView btnIncrement, btnDecrement;
        Button btnAddToCart;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            foodPicture = itemView.findViewById(R.id.imageViewFoodPicture);
            txtTitle = itemView.findViewById(R.id.textViewFoodTitle);
            txtPrice = itemView.findViewById(R.id.textViewFoodPrice);
            txtDetails = itemView.findViewById(R.id.textViewFoodDetails);
            btnAddToCart = itemView.findViewById(R.id.buttonAddToCart);
            btnIncrement = itemView.findViewById(R.id.imageViewIncrement);
            btnDecrement = itemView.findViewById(R.id.imageViewDecrement);
            txtQuantity = itemView.findViewById(R.id.textViewQuantity);
        }
    }
}
