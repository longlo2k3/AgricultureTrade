package com.example.agriculturaltrade.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.agriculturaltrade.Models.NewProductModel;
import com.example.agriculturaltrade.Models.PopularProductModel;
import com.example.agriculturaltrade.Models.ShowAllModel;
import com.example.agriculturaltrade.R;
import com.google.firebase.firestore.FirebaseFirestore;

public class DetailedActivity extends AppCompatActivity {

    ImageView detailedImg;
    TextView rating, name, description, price;
    Button addToCart, buyNow;
    ImageView addItem, removeItem;

    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);

        detailedImg = findViewById(R.id.detailed_img);
        name = findViewById(R.id.detailed_name);
        description = findViewById(R.id.detailed_desc);
        rating = findViewById(R.id.rating);
        price = findViewById(R.id.detailed_price);

        addToCart = findViewById(R.id.add_to_cart);
        buyNow = findViewById(R.id.buy_now);

        addItem = findViewById(R.id.add_item);
        removeItem = findViewById(R.id.remove_item);

        // New products
        NewProductModel newProductModel = null;
        firestore = FirebaseFirestore.getInstance();
        final  Object object = getIntent().getSerializableExtra("detailed");
        if (object instanceof NewProductModel) {
            newProductModel = (NewProductModel) object;
        }

        // New products details
        if (newProductModel != null) {
            Glide.with(getApplicationContext()).load(newProductModel.getImg_url()).into(detailedImg);
            name.setText(newProductModel.getName());
            description.setText(newProductModel.getDescription());
            rating.setText(newProductModel.getRating());
            price.setText(String.valueOf(newProductModel.getPrice()));
        }

        // Popular products details
        PopularProductModel popularProductModel = null;
        final  Object object1 = getIntent().getSerializableExtra("detailed");
        if (object1 instanceof PopularProductModel) {
            popularProductModel = (PopularProductModel) object1;
        }
        if (popularProductModel != null) {
            Glide.with(getApplicationContext()).load(popularProductModel.getImg_url()).into(detailedImg);
            name.setText(popularProductModel.getName());
            description.setText(popularProductModel.getDescription());
            rating.setText(popularProductModel.getRating());
            price.setText(String.valueOf(popularProductModel.getPrice()));
        }

        ShowAllModel showAllModel = null;
        final  Object object2 = getIntent().getSerializableExtra("detailed");
        if (object2 instanceof ShowAllModel) {
            showAllModel = (ShowAllModel) object2;
        }
        if (showAllModel != null) {
            Glide.with(getApplicationContext()).load(showAllModel.getImg_url()).into(detailedImg);
            name.setText(showAllModel.getName());
            description.setText(showAllModel.getDescription());
            rating.setText(showAllModel.getRating());
            price.setText(String.valueOf(showAllModel.getPrice()));
        }




    }
}