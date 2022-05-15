package com.example.fastfoodapp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.fastfoodapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FoodDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FoodDetailsFragment extends Fragment {
    TextView txtTitle, txtPrice, txtDescription, txtIngredients, txtQuantity;
    ImageView increment, decrement, foodImage;
    Button btnAddToCart;
    int quantity = 1;
    FirebaseFirestore db;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FoodDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FoodDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FoodDetailsFragment newInstance(String param1, String param2) {
        FoodDetailsFragment fragment = new FoodDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_food_details, container, false);
        txtTitle = view.findViewById(R.id.textViewDetailsTitle);
        txtPrice = view.findViewById(R.id.textViewDetailsPrice);
        txtDescription = view.findViewById(R.id.textViewDetailsDescription);
        txtIngredients = view.findViewById(R.id.textViewDetailsIngredients);
        txtQuantity = view.findViewById(R.id.textViewDetailsQuantity);
        increment = view.findViewById(R.id.imageViewDetailsIncrement);
        decrement = view.findViewById(R.id.imageViewDetailsDecrement);
        foodImage = view.findViewById(R.id.imageViewDetails);
        btnAddToCart = view.findViewById(R.id.buttonDetailsAddToCart);

        String foodName = getArguments().getString("foodName");
        increment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quantity++;
                txtQuantity.setText(String.valueOf(quantity));
            }
        });
        decrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(quantity > 1) {
                    quantity--;
                    txtQuantity.setText(String.valueOf(quantity));
                }
            }
        });

        db = FirebaseFirestore.getInstance();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        db.collection("Foods")
                .whereEqualTo("name", foodName)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){
                                txtTitle.setText(document.get("name").toString());
                                Glide.with(getActivity()).load(document.get("imageUrl")).into(foodImage);
                                txtDescription.setText(document.get("description").toString());
                                txtPrice.setText(document.get("price").toString());
                                txtIngredients.setText(document.get("ingredients").toString());

                                btnAddToCart.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        //get all from document where cart name is equal to food name
                                        db.collection("Cart")
                                            .document(userId)
                                            .collection("userCart")
                                            .whereEqualTo("name", foodName)
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.getResult().isEmpty()) {
                                                        //add the new item to cart
                                                        HashMap<String, Object> cartMap = new HashMap<>();
                                                        cartMap.put("name", document.get("name"));
                                                        cartMap.put("price", document.get("price"));
                                                        cartMap.put("quantity", quantity);
                                                        cartMap.put("imgUrl", document.get("imageUrl"));
                                                        //cartMap.put("total", 0);
                                                        db.collection("Cart").document(userId).collection("userCart").add(cartMap);
                                                        Toast.makeText(getActivity(), "Added to Cart", Toast.LENGTH_SHORT).show();
                                                    }else{
                                                        //else, if the product already is in cart, increment quantity
                                                        for (QueryDocumentSnapshot document2 : task.getResult()) {
                                                            db.collection("Cart").document(userId).collection("userCart").document(document2.getId()).update("quantity", FieldValue.increment(quantity));
                                                            Toast.makeText(getActivity(), "Added " + quantity + " " + document2.get("name") + " to Cart", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                }
                                            });
                                    }
                                });
                            }
                        }
                    }
                });

        return view;
    }
}