package com.example.fastfoodapp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fastfoodapp.R;
import com.example.fastfoodapp.activities.MainActivity;
import com.example.fastfoodapp.adapters.CartListAdapter;
import com.example.fastfoodapp.adapters.MenuListAdapter;
import com.example.fastfoodapp.models.Cart;
import com.example.fastfoodapp.models.Food;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CartFragment extends Fragment {
    RecyclerView rvCartList;
    FirebaseFirestore db;
    FirebaseDatabase database;
    //create a list that will store cart object
    List<Cart> cartList;
    CartListAdapter cartListAdapter;
    double total = 0;
    TextView txtTotal;
    Button btnCheckout;
    DecimalFormat df = new DecimalFormat("0.00");

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CartFragment newInstance(String param1, String param2) {
        CartFragment fragment = new CartFragment();
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
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        //get firebase instances
        db = FirebaseFirestore.getInstance();
        database = FirebaseDatabase.getInstance();
        rvCartList = view.findViewById(R.id.cartList);
        txtTotal = view.findViewById(R.id.textViewTotal);
        btnCheckout = view.findViewById(R.id.buttonCheckOut);
        //initialize array
        cartList = new ArrayList<>();
        //setup recycler view
        rvCartList.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        cartListAdapter = new CartListAdapter(cartList, getActivity());
        rvCartList.setAdapter(cartListAdapter);

        //get current user id
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        //get collection cart with specified user id (get all item that are in the user cart)
        db.collection("Cart").document(userId).collection("userCart").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){ //if successful
                    for (QueryDocumentSnapshot document : task.getResult()){ //foreach document
                        Cart cart = document.toObject(Cart.class); //create new cart object convert document to object cart
                        cartList.add(cart); //add cart object into cartList
                        //calculate the item price * the quantity of the item in cart
                        double sum = document.getDouble("price") * document.getDouble("quantity");
                        total += sum; //add sum to total
                        txtTotal.setText(String.valueOf(df.format(total))); //set txtTotal textview to the total
                        cartListAdapter.notifyDataSetChanged();
                    }
                    btnCheckout.setOnClickListener(new View.OnClickListener() { //if we click the cheout button
                        @Override
                        public void onClick(View view) {
                            //create a reference that get the key of the order id
                            String key = database.getReference().child("Purchased").child(userId).push().getKey();
                            int id = 1; //set int id to 1
                            Random rand = new Random();
                            int randomNum = rand.nextInt(200000000 - 100000000) + 100000000;
                            for (QueryDocumentSnapshot document2 : task.getResult()) {
                                double sum = document2.getDouble("price") * document2.getDouble("quantity");
                                HashMap<String, Object> orderDetailsMap = new HashMap<>(); //store order info
                                HashMap<String, Object> orderMap = new HashMap<>(); //store total so we can update it
                                //put key/value in map
                                orderMap.put("total", df.format(total));
                                orderMap.put("id", randomNum);
                                orderMap.put("imageUrl", "https://firebasestorage.googleapis.com/v0/b/fastfoodapp-3892c.appspot.com/o/Customer-Thank-You-Note-1.png?alt=media&token=9a7788e2-40a4-4bc0-8f91-bdf709850e0a");
                                orderDetailsMap.put("name", document2.get("name"));
                                orderDetailsMap.put("totalPriceItem", df.format(sum));
                                orderDetailsMap.put("imgUrl", document2.get("imgUrl"));
                                orderDetailsMap.put("quantity", document2.get("quantity"));
                                orderDetailsMap.put("price", document2.get("price"));
                                //create a node in firebase database that have all the food that the user just ordered
                                //database.getReference().child("Orders").child(userId).child(key).child(String.valueOf(id + "_key")).setValue(orderMap);
                                //db.collection("Orders").document(userId).collection(String.valueOf(randomNum)).document(String.valueOf(id)).set(orderDetailsMap);
                                db.collection("Orders").document(userId).collection("order").document(String.valueOf(randomNum)).collection("OrderInfo").document(String.valueOf(id)).set(orderDetailsMap);
                                db.collection("Orders").document(userId).collection("order").document(String.valueOf(randomNum)).set(orderMap);
                                //update it soon after to add the total outside the food nodes
                                //database.getReference().child("Orders").child(userId).child(key).updateChildren(totalMap);
                                id++; //increment id
                                document2.getReference().delete(); //delete food from cart
                            }
                            //re-open fragment 2
                            MainActivity.fragmentManager.beginTransaction().replace(R.id.fragmentContainerView, new CartFragment()).commit();
                        }
                    });
                }else{
                    Toast.makeText(getActivity(), "Error:" + task.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }
}