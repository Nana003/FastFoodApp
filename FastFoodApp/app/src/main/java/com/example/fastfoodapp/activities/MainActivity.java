package com.example.fastfoodapp.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.example.fastfoodapp.databinding.ActivityMainBinding;
import com.example.fastfoodapp.fragments.CartFragment;
import com.example.fastfoodapp.fragments.HomeFragment;
import com.example.fastfoodapp.fragments.MenuFragment;
import com.example.fastfoodapp.R;
import com.example.fastfoodapp.fragments.PastOrdersFragment;
import com.example.fastfoodapp.fragments.RewardFragment;
import com.example.fastfoodapp.fragments.SettingsFragment;
import com.example.fastfoodapp.models.Food;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    BottomNavigationView bottomNavigationViewavView;
    FirebaseFirestore db;
    public static FragmentManager fragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        bottomNavigationViewavView = findViewById(R.id.nav_view);

        fragmentManager = getSupportFragmentManager();
        bottomNavigationViewavView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_cart:
                        fragmentManager.beginTransaction().replace(R.id.fragmentContainerView, new CartFragment()).commit();
                        return true;
                    case R.id.navigation_menu:
                        fragmentManager.beginTransaction().replace(R.id.fragmentContainerView, new MenuFragment()).commit();
                        return true;
                    case R.id.navigation_pastOrders:
                        fragmentManager.beginTransaction().replace(R.id.fragmentContainerView, new PastOrdersFragment()).commit();
                        return true;
                    case R.id.navigation_settings:
                        fragmentManager.beginTransaction().replace(R.id.fragmentContainerView, new SettingsFragment()).commit();
                        return true;
                }
                return false;
            }
        });

    /*private void searchData(String s) {
        db = FirebaseFirestore.getInstance();
        db.collection("Foods")
                .whereEqualTo("name", s)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Food food = document.toObject(Food.class);
                                foodList.add(food);
                                menuListAdapter.notifyDataSetChanged();
                                // db.collection("Foods").whereEqualTo("name", "==");
                            }
                        } else {
                            Toast.makeText(getActivity(), "Error:" + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }*/
    }
}