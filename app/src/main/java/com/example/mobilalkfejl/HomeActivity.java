package com.example.mobilalkfejl;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.MenuItemCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";
    private FirebaseUser user;
    private FirebaseFirestore mFirestore;
    private CollectionReference mItems;

    private RecyclerView recyclerView;
    private ArrayList<HouseItem> itemList;
    private HouseItemAdapter adapter;
    private int gridnumber = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Log.d(TAG, "Auth user Failed :( ");
            finish();
        } else {
            Log.d(TAG, "Auth user Successful ");
        }
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, gridnumber));
        itemList = new ArrayList<>();
        adapter = new HouseItemAdapter(this, itemList);
        recyclerView.setAdapter(adapter);

        mFirestore = FirebaseFirestore.getInstance();
        mItems = mFirestore.collection("Houses");

        initializeData();
    }

    protected void onResume() {
        super.onResume();
        // Refresh data here
        initializeData();
    }

    private void initializeData(){
        String[] itemDescriptions = getResources().getStringArray(R.array.description_array);
        String[] itemAddresses = getResources().getStringArray(R.array.address_array);
        String[] itemPrices = getResources().getStringArray(R.array.price_array);
        String[] itemContacts = getResources().getStringArray(R.array.contact_array);
        TypedArray itemImageResources = getResources().obtainTypedArray(R.array.images_array);

        //itemList.clear();

        for (int i = 0; i < itemAddresses.length; i++) {
            HouseItem houseItem = new HouseItem(itemAddresses[i], itemPrices[i], itemDescriptions[i], itemContacts[i], itemImageResources.getResourceId(i, 0));
            mItems.add(houseItem).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                @Override
                public void onComplete(@NonNull Task<DocumentReference> task) {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + task.getResult().getId());
                    } else {
                        Log.w(TAG, "Error adding document", task.getException());
                    }
                }
            });
        }


        //for (int i = 0; i < itemAddresses.length; i++) {
        //    mItems.add(new HouseItem(itemAddresses[i], itemPrices[i], itemDescriptions[i], itemContacts[i], itemImageResources.getResourceId(i, 0)));
        //}
        itemImageResources.recycle();
        //adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.home_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search_bar);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d(TAG, "Searched For: " + newText);
                adapter.getFilter().filter(newText);
                return false;
            }
        });


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Log.d(TAG, "Item Selected");
        if(item.getItemId() == R.id.own_listings){
            Intent intent = new Intent(this, CreateListingActivity.class);
            startActivity(intent);
            return true;
        }
        else if(item.getItemId() == R.id.log_out){
            FirebaseAuth.getInstance().signOut();
            finish();
            return true;
        }
        else{
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }
}