package com.example.agriculturaltrade.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.agriculturaltrade.Adapters.MyCartAdapter;
import com.example.agriculturaltrade.Fragments.HomeFragment;
import com.example.agriculturaltrade.Models.MyCartModel;
import com.example.agriculturaltrade.Models.ShowAllModel;
import com.example.agriculturaltrade.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    Toolbar toolbar, toolbar_activity;
    List<MyCartModel> myCartModelList;

    Button buyNowBtn;

    int overAllTotalPrice = 0;

    TextView overAllTotalPriceView;

    RecyclerView myCartRecyclerView;
    MyCartAdapter myCartAdapter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        overAllTotalPriceView = findViewById(R.id.total_price);
        buyNowBtn = findViewById(R.id.buy_now);

        toolbarShow();
        toolbarAction();

        //lay data tu my cart adapter
        getDataFromMyCartAdapter();

    }

    private void getDataFromMyCartAdapter() {
        LocalBroadcastManager.getInstance(this)
                .registerReceiver(mMessageReceiver,new IntentFilter("MyTotalAmount"));

        myCartRecyclerView = findViewById(R.id.cart_rec);
        myCartRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        myCartModelList = new ArrayList<>();
        myCartAdapter = new MyCartAdapter(CartActivity.this, myCartModelList);
        myCartRecyclerView.setAdapter(myCartAdapter);


        //doc du lieu
        db.collection("AddToCart").document(mAuth.getCurrentUser().getUid())
                .collection("User")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            MyCartModel myCartModel = document.toObject(MyCartModel.class);
                            myCartModelList.add(myCartModel);
                            myCartAdapter.notifyDataSetChanged();
                        }
                    }

                });
    }

    private void toolbarAction() {
        toolbar_activity = findViewById(R.id.home_toolbar_activity);

        // Sự kiện khi nhấn vào các nút
        findViewById(R.id.action_home).setOnClickListener(v -> {
            // Chuyển hướng đến trang chủ
            startActivity(new Intent(this, MainActivity.class));
        });

        findViewById(R.id.action_product_details).setOnClickListener(v -> {
            // Chuyển hướng đến chi tiết sản phẩm
            startActivity(new Intent(this, ShowAllActivity.class));
        });

        findViewById(R.id.action_cart).setOnClickListener(v -> {
            // Chuyển hướng đến giỏ hàng
            startActivity(new Intent(this, CartActivity.class));
        });

        findViewById(R.id.action_account).setOnClickListener(v -> {
            // Chuyển hướng đến tài khoản
            startActivity(new Intent(this, AccountActivity.class));
        });
    }

    private void toolbarShow() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            overAllTotalPrice = intent.getIntExtra("totalAmount", 0);
            overAllTotalPriceView.setText("Giá tổng: " + overAllTotalPrice);
            buyNowBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(CartActivity.this, AddressActivity.class);
                    intent.putExtra("totalAmount", overAllTotalPrice);
                    startActivity(intent);
                }
            });
        }
    };
}