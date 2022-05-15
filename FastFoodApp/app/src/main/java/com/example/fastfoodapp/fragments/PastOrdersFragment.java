package com.example.fastfoodapp.fragments;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.fastfoodapp.R;
import com.example.fastfoodapp.adapters.OrderListAdapter;
import com.example.fastfoodapp.models.Food;
import com.example.fastfoodapp.models.Order;
import com.example.fastfoodapp.models.OrderDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PastOrdersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PastOrdersFragment extends Fragment {
    RecyclerView rvOrderList;
    FirebaseFirestore db;
    //create a list that will store cart object
    List<Order> orderList;
    OrderListAdapter orderListAdapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PastOrdersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PastOrdersFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PastOrdersFragment newInstance(String param1, String param2) {
        PastOrdersFragment fragment = new PastOrdersFragment();
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
        View view = inflater.inflate(R.layout.fragment_past_orders, container, false);
        rvOrderList = view.findViewById(R.id.recyclerViewOrderList);
        db = FirebaseFirestore.getInstance();

        orderList = new ArrayList<>();
        rvOrderList.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        orderListAdapter = new OrderListAdapter(orderList, getActivity());
        rvOrderList.setAdapter(orderListAdapter);

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        db.collection("Orders")
                .document(userId)
                .collection("order")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult().getDocuments()) {
                                db.collection("Orders").document(userId).collection("order").document(document.getId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        DocumentSnapshot doc = task.getResult();
                                        if (doc.exists()) {
                                            Order order = doc.toObject(Order.class);
                                            orderList.add(order);
                                            orderListAdapter.notifyDataSetChanged();
                                        }
                                    }
                                });
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

        /*Order order = document2.toObject(Order.class);
        orderList.add(order);
        orderListAdapter.notifyDataSetChanged();*/
        return view;
    }
}