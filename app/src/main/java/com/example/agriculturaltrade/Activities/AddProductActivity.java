package com.example.agriculturaltrade.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.agriculturaltrade.Adapters.NewProductAdapter;
import com.example.agriculturaltrade.Models.NewProductModel;
import com.example.agriculturaltrade.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.DocumentSnapshot;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddProductActivity extends AppCompatActivity {

    TextView tvRating;
    EditText edtImgUrl, edtProductName, edtPrice, edtDescription;
    Spinner spinnerType;
    Button btnAddProduct;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        edtImgUrl = findViewById(R.id.edtImgUrl);
        edtProductName = findViewById(R.id.edtProductName);
        edtPrice = findViewById(R.id.edtPrice);
        edtDescription = findViewById(R.id.edtDescription);
        btnAddProduct = findViewById(R.id.btnAddProduct);
        spinnerType = findViewById(R.id.spinnerType);
        tvRating = findViewById(R.id.tvRating);

        toolbarShow();


        // Dữ liệu mẫu cho Spinner
        List<String> types = Arrays.asList("luongthuc", "traicay", "raucu", "dacsan", "hoa");

        // Adapter cho Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, types);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(adapter);

        // Set click listener for the add product button
        btnAddProduct.setOnClickListener(v -> {
            setNewProducts();
        });
    }

    private void setNewProducts() {
        String imgUrl = edtImgUrl.getText().toString();
        String productName = edtProductName.getText().toString();
        String productType = spinnerType.getSelectedItem().toString();
        String price = edtPrice.getText().toString();
        String description = edtDescription.getText().toString();
        String rating = tvRating.getText().toString();

        // Kiểm tra dữ liệu nhập vào
        if (TextUtils.isEmpty(imgUrl) || TextUtils.isEmpty(productName) ||
                TextUtils.isEmpty(description) || TextUtils.isEmpty(price)) {
            Toast.makeText(AddProductActivity.this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Chuyển đổi giá trị từ String sang float
        float priceConvert;
        try {
            priceConvert = Float.parseFloat(price);  // Chuyển String (priceStr) thành float
        } catch (NumberFormatException e) {
            Toast.makeText(AddProductActivity.this, "Giá phải là một số hợp lệ!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tạo Map để lưu dữ liệu sản phẩm
        Map<String, Object> product = new HashMap<>();
        product.put("img_url", imgUrl);
        product.put("name", productName);
        product.put("description", description);
        product.put("price", priceConvert);
        product.put("type", productType);
        product.put("rating", rating);

        // Add product to Firestore
        db.collection("AddProduct").document(mAuth.getCurrentUser().getUid())
                .collection("NewProducts")
                .add(product)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(AddProductActivity.this, "Thêm sản phẩm thành công", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AddProductActivity.this, ShowAllActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(AddProductActivity.this, "Thêm sản phẩm thất bại", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void toolbarShow() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Thêm sản phẩm");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
