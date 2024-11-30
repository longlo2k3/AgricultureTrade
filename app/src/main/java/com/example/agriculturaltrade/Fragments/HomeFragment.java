package com.example.agriculturaltrade.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.agriculturaltrade.Activities.ShowAllActivity;
import com.example.agriculturaltrade.Adapters.CategoryAdapter;
import com.example.agriculturaltrade.Adapters.NewProductAdapter;
import com.example.agriculturaltrade.Adapters.PopularProductAdapter;
import com.example.agriculturaltrade.Authentication.Log_in;
import com.example.agriculturaltrade.Models.CategoryModel;
import com.example.agriculturaltrade.Models.NewProductModel;
import com.example.agriculturaltrade.Models.PopularProductModel;
import com.example.agriculturaltrade.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    TextView catShowAll, newShowAll, popularShowAll;

    RecyclerView catRecyclerView, newRecyclerView,popularRecyclerView;

    //Category recyclerview
    CategoryAdapter categoryAdapter;

    //New product recyclerview
    NewProductAdapter newProductAdapter;

    //Popular product recyclerview
    PopularProductAdapter popularProductAdapter;

    //List
    List<CategoryModel> categoryModelList;
    List<NewProductModel> newProductModelList;
    List<PopularProductModel> popularProductModelList;

    //Firestore
    FirebaseFirestore db;

    FirebaseAuth mAuth;
    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_home, container, false);


        //Recyclerview
        catRecyclerView = root.findViewById(R.id.rec_category);
        newRecyclerView = root.findViewById(R.id.new_product_rec);
        popularRecyclerView = root.findViewById(R.id.popular_rec);
        catShowAll = root.findViewById(R.id.category_see_all);
        newShowAll = root.findViewById(R.id.newProducts_see_all);
        popularShowAll = root.findViewById(R.id.popular_see_all);

        catShowAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ShowAllActivity.class);
//                intent.putExtra("category", "category");
                startActivity(intent);
            }
        });
        newShowAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ShowAllActivity.class);
//                intent.putExtra("category", "category");
                startActivity(intent);
            }
        });
        popularShowAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ShowAllActivity.class);
//                intent.putExtra("category", "category");
                startActivity(intent);
            }
        });



;
        //Firestore
        db = FirebaseFirestore.getInstance();

        // image slider
        ImageSlider imageSlider = root.findViewById(R.id.image_slider);

        List<SlideModel> slideModels = new java.util.ArrayList<>();
        slideModels.add(new SlideModel(R.drawable.banner1, "Nông sản quảng ninh", ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel(R.drawable.banner2, "Trái cây theo mùa", ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel(R.drawable.banner3, "Nông sản Việt", ScaleTypes.CENTER_CROP));

        imageSlider.setImageList(slideModels);


        //Category
        catRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        categoryModelList = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(getContext(), categoryModelList);
        catRecyclerView.setAdapter(categoryAdapter);

        db.collection("Category")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            CategoryModel categoryModel = document.toObject(CategoryModel.class);
                            categoryModelList.add(categoryModel);
                            categoryAdapter.notifyDataSetChanged();
                        }
                    }else {
                        Toast.makeText(getContext(), "Error: " + task.getException() , Toast.LENGTH_SHORT).show();
                    }
                });

        // New product
        newRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        newProductModelList = new ArrayList<>();
        newProductAdapter = new NewProductAdapter(getContext(), newProductModelList);
        newRecyclerView.setAdapter(newProductAdapter);

        db.collection("NewProduct")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            NewProductModel newProductModel = document.toObject(NewProductModel.class);
                            newProductModelList.add(newProductModel);
                            newProductAdapter.notifyDataSetChanged();
                        }
                    }else {
                        Toast.makeText(getContext(), "Error: " + task.getException() , Toast.LENGTH_SHORT).show();
                    }
                });

        // Popular products
        popularRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        popularProductModelList = new ArrayList<>();
        popularProductAdapter = new PopularProductAdapter(getContext(), popularProductModelList);
        popularRecyclerView.setAdapter(popularProductAdapter);

        db.collection("PopularProducts")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            PopularProductModel popularProductModel = document.toObject(PopularProductModel.class);
                            popularProductModelList.add(popularProductModel);
                            popularProductAdapter.notifyDataSetChanged();
                        }
                    }else {
                        Toast.makeText(getContext(), "Error: " + task.getException() , Toast.LENGTH_SHORT).show();
                    }
                });

        return  root;
    }
}