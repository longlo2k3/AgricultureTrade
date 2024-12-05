package com.example.agriculturaltrade.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.agriculturaltrade.Adapters.MyProductAdapter;
import com.example.agriculturaltrade.Models.AdressModel;
import com.example.agriculturaltrade.Models.MyCartModel;
import com.example.agriculturaltrade.Models.MyProductModel;
import com.example.agriculturaltrade.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class MyProductsActivity extends AppCompatActivity {
    RecyclerView myProductsRecyclerView;
    List<MyProductModel> myProductModelList;
    MyProductAdapter myProductAdapter;

    Toolbar toolbar;
    ImageView btnSearch;
    EditText inputSearch;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_products);

        toolbarShow();

        myProductsRecyclerView = findViewById(R.id.my_products_rec);
        myProductsRecyclerView.setLayoutManager(new androidx.recyclerview.widget.GridLayoutManager(this,2));
        myProductModelList = new ArrayList<>();
        myProductAdapter = new MyProductAdapter(this,myProductModelList);
        myProductsRecyclerView.setAdapter(myProductAdapter);
        btnSearch = findViewById(R.id.btn_search);
        inputSearch = findViewById(R.id.input_search);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSearchAction();
            }
        });
        getData();

    }

    private void getSearchAction() {
        String search = inputSearch.getText().toString().trim();
        if (!search.isEmpty()) {
            myProductModelList.clear();
            myProductAdapter.notifyDataSetChanged();
            db.collection("AddProduct").document(mAuth.getCurrentUser().getUid())
                    .collection("NewProducts")
                    .whereEqualTo("name", search.toString().trim())
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                MyProductModel myCartModel = document.toObject(MyProductModel.class);
                                myProductModelList.add(myCartModel);
                                myProductAdapter.notifyDataSetChanged();
                            }
                            Intent intent = new Intent(MyProductsActivity.this, CartActivity.class);
                        }
                    });
        } else {
            // Tìm kiếm không có điều kiện
            myProductModelList.clear();
            myProductAdapter.notifyDataSetChanged();
            getData();
        }

    }


    private void getData() {
        db.collection("AddProduct").document(mAuth.getCurrentUser().getUid())
                .collection("NewProducts")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            MyProductModel myCartModel = document.toObject(MyProductModel.class);
                            myProductModelList.add(myCartModel);
                            myProductAdapter.notifyDataSetChanged();
                        }
                        Intent intent = new Intent(MyProductsActivity.this, CartActivity.class);
                    }
                });
    }


    private void toolbarShow() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}