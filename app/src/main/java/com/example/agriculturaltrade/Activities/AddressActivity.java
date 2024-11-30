package com.example.agriculturaltrade.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.bumptech.glide.Glide;
import com.example.agriculturaltrade.Adapters.AddressAdapter;
import com.example.agriculturaltrade.Models.AdressModel;
import com.example.agriculturaltrade.Models.MyCartModel;
import com.example.agriculturaltrade.Models.NewProductModel;
import com.example.agriculturaltrade.Models.PopularProductModel;
import com.example.agriculturaltrade.Models.ShowAllModel;
import com.example.agriculturaltrade.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class AddressActivity extends AppCompatActivity implements AddressAdapter.SelectedAddress{

    Button btnAddAddress,paymentBtn;
    RecyclerView recyclerView;
    private List<AdressModel> adressModelList;

    AddressAdapter addressAdapter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    Toolbar toolbar, toolbar_activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        toolbarShow();

        paymentBtn = findViewById(R.id.payment_btn);
        recyclerView = findViewById(R.id.address_recycler);
        btnAddAddress = findViewById(R.id.add_address_btn);

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        adressModelList = new ArrayList<>();
        addressAdapter = new AddressAdapter(this, adressModelList, this);
        recyclerView.setAdapter(addressAdapter);

        //get Data from detailed activity
        getDataByDetailed();
        btnAddAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddressActivity.this, AddAdressActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void getDataByDetailed() {
        Object object = getIntent().getSerializableExtra("item");
        db.collection("CurrentUser").document(mAuth.getCurrentUser().getUid())
                .collection("Address")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            AdressModel adressModel = document.toObject(AdressModel.class);
                            adressModelList.add(adressModel);
                            addressAdapter.notifyDataSetChanged();
                        }
                    }
                    paymentBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            double amount = 0.0;

                            if (object instanceof NewProductModel) {
                                NewProductModel newProductModel = (NewProductModel) object;
                                amount = newProductModel.getPrice().doubleValue();
                            }
                            if (object instanceof PopularProductModel) {
                                PopularProductModel popularProductModel = (PopularProductModel) object;
                                amount =  popularProductModel.getPrice().doubleValue();
                            }
                            if (object instanceof ShowAllModel) {
                                ShowAllModel showAllModel = (ShowAllModel) object;
                                amount = showAllModel.getPrice().doubleValue();
                            }
                            int totalAmount = getIntent().getIntExtra("totalAmount", 0);
                            // Kiểm tra dữ liệu
                            if (totalAmount != 0) {
                                amount = totalAmount;
                            } else {
                                Log.e("ERROR", "Total Amount is not received or is 0.");
                            }


                            Intent intent = new Intent(AddressActivity.this, PaymentActivity.class);
                            intent.putExtra("amount", amount);
                            startActivity(intent);
                            finish();
                        }
                    });
                });
    }

    private void toolbarShow() {
        toolbar = findViewById(R.id.address_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void setAddress(String address) {

    }
}