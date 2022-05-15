package com.example.fastfoodapp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.fastfoodapp.R;
import com.example.fastfoodapp.adapters.OrderDetailsAdapter;
import com.example.fastfoodapp.adapters.OrderListAdapter;
import com.example.fastfoodapp.models.Food;
import com.example.fastfoodapp.models.Order;
import com.example.fastfoodapp.models.OrderDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrderDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderDetailsFragment extends Fragment {
    RecyclerView rvOrderDetailsList;
    TextView txtOrderNb, txtTotal;
    FirebaseFirestore db;
    //create a list that will store object
    List<OrderDetails> orderDetailsList;
    OrderDetailsAdapter orderDetailsAdapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public OrderDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OrderDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OrderDetailsFragment newInstance(String param1, String param2) {
        OrderDetailsFragment fragment = new OrderDetailsFragment();
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
        View view = inflater.inflate(R.layout.fragment_order_details, container, false);
        rvOrderDetailsList = view.findViewById(R.id.recyclerViewOrderDetailsList);
        txtOrderNb = view.findViewById(R.id.textViewOrderNo);
        txtTotal = view.findViewById(R.id.textViewOrderTotal);
        db = FirebaseFirestore.getInstance();

        orderDetailsList = new ArrayList<>();
        rvOrderDetailsList.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        orderDetailsAdapter = new OrderDetailsAdapter(orderDetailsList, getActivity());
        rvOrderDetailsList.setAdapter(orderDetailsAdapter);

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Long orderNo = getArguments().getLong("OrderNo");

        txtOrderNb.setText("Order#: " + String.valueOf(orderNo));

        db.collection("Orders")
                .document(userId)
                .collection("order")
                .document(String.valueOf(orderNo))
                .collection("OrderInfo")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){
                                OrderDetails orderDetails = document.toObject(OrderDetails.class);
                                orderDetailsList.add(orderDetails);
                                orderDetailsAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                });
        db.collection("Orders").document(userId).collection("order").document(String.valueOf(orderNo))
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot doc = task.getResult();
                        if (doc.exists()) {
                            txtTotal.setText(String.valueOf(doc.get("total")));
                        }
                    }
                });
        return view;
    }
}