package com.example.agriculturaltrade.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.agriculturaltrade.Models.MyProductModel;
import com.example.agriculturaltrade.Models.NewProductModel;
import com.example.agriculturaltrade.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UpdateMyProductActivity extends AppCompatActivity {

    TextView tvRating;
    EditText edtImgUrl, edtProductName, edtPrice, edtDescription;
    String nameProduct;
    Spinner spinnerType;
    Button btnUpdateProduct;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_my_product);

        toolbarShow();

        edtImgUrl = findViewById(R.id.edtImgUrl);
        edtProductName = findViewById(R.id.edtProductName);
        edtPrice = findViewById(R.id.edtPrice);
        edtDescription = findViewById(R.id.edtDescription);
        btnUpdateProduct = findViewById(R.id.btnUpdateProduct);
        spinnerType = findViewById(R.id.spinnerType);
        tvRating = findViewById(R.id.tvRating);

        // Dữ liệu mẫu cho Spinner
        List<String> types = Arrays.asList("luongthuc", "traicay", "raucu", "dacsan", "hoa");

        // Adapter cho Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, types);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(adapter);

        final  Object object = getIntent().getSerializableExtra("myproduct");
        if (object != null) {
            MyProductModel myProductModel = (MyProductModel) object;
            edtImgUrl.setText(myProductModel.getImg_url());
            nameProduct = myProductModel.getName();
            edtProductName.setText(nameProduct);
            edtPrice.setText(String.valueOf(myProductModel.getPrice()));
            edtDescription.setText(myProductModel.getDescription());
        }
        else{
            Toast.makeText(this, "Fail", Toast.LENGTH_SHORT).show();
        }


        btnUpdateProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String imgUrl = edtImgUrl.getText().toString();
                String productName = edtProductName.getText().toString();
                String productType = spinnerType.getSelectedItem().toString();
                String price = edtPrice.getText().toString();
                String description = edtDescription.getText().toString();
                String rating = tvRating.getText().toString();

                float priceConvert;
                try {
                    priceConvert = Float.parseFloat(price);  // Chuyển String (priceStr) thành float
                } catch (NumberFormatException e) {
                    Toast.makeText(UpdateMyProductActivity.this, "Giá phải là một số hợp lệ!", Toast.LENGTH_SHORT).show();
                    return;
                }
                Map<String, Object> product = new HashMap<>();
                product.put("img_url", imgUrl);
                product.put("name", productName);
                product.put("description", description);
                product.put("price", priceConvert);
                product.put("type", productType);
                product.put("rating", rating);


                db.collection("AddProduct").document(mAuth.getCurrentUser().getUid())
                        .collection("NewProducts")
                        .whereEqualTo("name", nameProduct)
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                if (!queryDocumentSnapshots.isEmpty()) {
                                    for(QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                        String id = document.getId();
                                        DocumentReference docRef = db.collection("AddProduct").document(mAuth.getCurrentUser().getUid())
                                                .collection("NewProducts")
                                                .document(id);
                                        docRef.update(product)
                                                .addOnSuccessListener(aVoid -> {
                                                    Toast.makeText(UpdateMyProductActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(UpdateMyProductActivity.this, MyProductsActivity.class);
                                                    startActivity(intent);
                                                })
                                                .addOnFailureListener(e -> {
                                                    Toast.makeText(UpdateMyProductActivity.this, "Fail", Toast.LENGTH_SHORT).show();
                                                });
                                    }
                                }
                            }
                        });
            }
        });
    }

    private void toolbarShow() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Sửa sản phẩm");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}