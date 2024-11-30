package com.example.agriculturaltrade.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.agriculturaltrade.Models.MyCartModel;
import com.example.agriculturaltrade.Models.NewProductModel;
import com.example.agriculturaltrade.Models.PopularProductModel;
import com.example.agriculturaltrade.Models.ShowAllModel;
import com.example.agriculturaltrade.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class DetailedActivity extends AppCompatActivity {

    ImageView detailedImg;
    TextView rating, name, description, price,quanlity,producerName;
    Button addToCart, buyNow;
    ImageView addItem, removeItem;

    Toolbar toolbar;

    int totalQuanlity = 1;
    int totalPrice = 0;

    private FirebaseFirestore firestore;
    private FirebaseAuth mAuth;

    // Sensor
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private float lastY = 0;
    private long lastShakeTime = 0;
    private static final long SHAKE_THRESHOLD_TIME = 500; // 500ms
    private static final float SHAKE_THRESHOLD_VALUE = 12.0f; // Lắc mạnh theo trục Y

    NewProductModel newProductModel = null;
    PopularProductModel popularProductModel = null;
    ShowAllModel showAllModel = null;
    MyCartModel myCartModel = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);

        detailedImg = findViewById(R.id.detailed_img);
        name = findViewById(R.id.detailed_name);
        description = findViewById(R.id.detailed_desc);
        rating = findViewById(R.id.rating);
        price = findViewById(R.id.detailed_price);
        producerName = findViewById(R.id.detailed_producer);

        addToCart = findViewById(R.id.add_to_cart);
        buyNow = findViewById(R.id.buy_now);

        addItem = findViewById(R.id.add_item);
        removeItem = findViewById(R.id.remove_item);
        quanlity = findViewById(R.id.quanlity);

        firestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        toobarShow();

        setProducerName();

        //Sensor
        sensorManager = (SensorManager) getSystemService(this.SENSOR_SERVICE);
        if (sensorManager != null) {
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }


        final  Object object = getIntent().getSerializableExtra("detailed");
        // New products
        getDataFormNewProduct(object);
        // Popular products details
        getDataFromPopularProduct(object);
        // Show all products details
        getDataFromShowAllProduct(object);
        // Cart
        getDataFromMyCartAdapter(object);

        //Buy now
        NewProductModel finalNewProductModel1 = newProductModel;
        PopularProductModel finalPopularProductModel1 = popularProductModel;
        ShowAllModel finalShowAllModel1 = showAllModel;
        buyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailedActivity.this, AddressActivity.class);
                if (finalNewProductModel1 != null){
                    intent.putExtra("item", finalNewProductModel1);
                }
                if (finalPopularProductModel1 != null){
                    intent.putExtra("item", finalPopularProductModel1);
                }
                if (finalShowAllModel1 != null){
                    intent.putExtra("item", finalShowAllModel1);
                }
                startActivity(intent);
                finish();
            }
        });


        //Add to cart
        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCart();
            }
        });

        PopularProductModel finalPopularProductModel = popularProductModel;
        NewProductModel finalNewProductModel = newProductModel;
        ShowAllModel finalShowAllModel = showAllModel;
        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (totalQuanlity < 10){
                    totalQuanlity ++;
                    quanlity.setText(String.valueOf(totalQuanlity));
                    if (finalPopularProductModel != null){
                        totalPrice = finalPopularProductModel.getPrice().intValue() * totalQuanlity;
                    }
                    if (finalNewProductModel != null){
                        totalPrice = finalNewProductModel.getPrice().intValue() * totalQuanlity;
                    }
                    if (finalShowAllModel != null){
                        totalPrice = finalShowAllModel.getPrice().intValue() * totalQuanlity;
                    }
                }
            }
        });
        removeItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (totalQuanlity > 1){
                    totalQuanlity --;
                    quanlity.setText(String.valueOf(totalQuanlity));
                }
            }
        });
    }

    private void getDataFromMyCartAdapter(Object object) {
        if (object instanceof MyCartModel) {
            myCartModel = (MyCartModel) object;
        }
        if (myCartModel != null) {
            Glide.with(getApplicationContext()).load(myCartModel.getImg_url()).into(detailedImg);
            name.setText(myCartModel.getProductName());
            price.setText(String.valueOf(myCartModel.getProductPrice()));
            quanlity.setText(myCartModel.getTotalQuanlity());
            totalPrice = myCartModel.getTotalPrice();
        }
    }

    private void getDataFromShowAllProduct(Object object) {
        if (object instanceof ShowAllModel) {
            showAllModel = (ShowAllModel) object;
        }
        if (showAllModel != null) {
            Glide.with(getApplicationContext()).load(showAllModel.getImg_url()).into(detailedImg);
            name.setText(showAllModel.getName());
            description.setText(showAllModel.getDescription());
            rating.setText(showAllModel.getRating());
            price.setText(String.valueOf(showAllModel.getPrice()) + " VNĐ");

            totalPrice = showAllModel.getPrice().intValue() * totalQuanlity;
        }
    }

    private void getDataFromPopularProduct(Object object) {
        if (object instanceof PopularProductModel) {
            popularProductModel = (PopularProductModel) object;
        }
        if (popularProductModel != null) {
            Glide.with(getApplicationContext()).load(popularProductModel.getImg_url()).into(detailedImg);
            name.setText(popularProductModel.getName());
            description.setText(popularProductModel.getDescription());
            rating.setText(popularProductModel.getRating());
            price.setText(String.valueOf(popularProductModel.getPrice()) + " VNĐ");

            totalPrice = popularProductModel.getPrice().intValue() * totalQuanlity;
        }
    }

    private void getDataFormNewProduct(Object object) {
        if (object instanceof NewProductModel) {
            newProductModel = (NewProductModel) object;
        }

        // New products details
        if (newProductModel != null) {
            Glide.with(getApplicationContext()).load(newProductModel.getImg_url()).into(detailedImg);
            name.setText(newProductModel.getName());
            description.setText(newProductModel.getDescription());
            rating.setText(newProductModel.getRating());
            price.setText(String.valueOf(newProductModel.getPrice()) + " VNĐ");

            totalPrice = newProductModel.getPrice().intValue() * totalQuanlity;
        }
    }

    private void toobarShow() {
        toolbar = findViewById(R.id.detailed_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setProducerName() {
        if (mAuth.getCurrentUser() != null) {
            String userId = mAuth.getCurrentUser().getUid(); // Lấy UID người dùng

            // Truy vấn Firestore để lấy thông tin người dùng
            firestore.collection("Users").document(userId).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            // Lấy dữ liệu từ Firestore
                            String getName = documentSnapshot.getString("username");

                            // Hiển thị dữ liệu lên UI
                            producerName.setText("Nhà sản xuất:"+getName);
                        } else {
                            System.out.println("Dữ liệu người dùng không tồn tại trong Firestore.");
                        }
                    })
                    .addOnFailureListener(e -> {
                        System.out.println("Lỗi khi truy vấn Firestore: " + e.getMessage());
                    });
        } else {
            System.out.println("Người dùng chưa đăng nhập.");
        }
    }

    private SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                float y = event.values[1]; // Trục Y (lắc dọc)
                long currentTime = System.currentTimeMillis();

                // Kiểm tra điều kiện lắc (dựa trên giá trị trục Y và thời gian giữa các lần lắc)
                if (Math.abs(y - lastY) > SHAKE_THRESHOLD_VALUE && (currentTime - lastShakeTime > SHAKE_THRESHOLD_TIME)) {
                    lastShakeTime = currentTime;
                    lastY = y;

                    // Gọi hàm thêm vào giỏ hàng
                    addToCart();
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // Không cần xử lý ở đây
        }
    };
    @Override
    protected void onResume() {
        super.onResume();
        if (sensorManager != null && accelerometer != null) {
            sensorManager.registerListener(sensorEventListener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (sensorManager != null) {
            sensorManager.unregisterListener(sensorEventListener);
        }
    }

    private void addToCart() {
        String saveCurrentDate, saveCurrentTime;

        // Calendar sử dụng để tham chiêu đến ngày hiện tại
        Calendar calForDate = Calendar.getInstance();

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentDate = currentTime.format(calForDate.getTime());

        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyyy");
        saveCurrentTime = currentDate.format(calForDate.getTime());

        PopularProductModel popularProductModel = null;
        NewProductModel newProductModel = null;
        ShowAllModel showAllModel = null;
        final  Object object3 = getIntent().getSerializableExtra("detailed");
        if (object3 instanceof PopularProductModel) {
            popularProductModel = (PopularProductModel) object3;
        }
        if (object3 instanceof NewProductModel) {
            newProductModel = (NewProductModel) object3;
        }
        if (object3 instanceof ShowAllModel) {
            showAllModel = (ShowAllModel) object3;
        }

        final HashMap<String, Object> cartMap = new HashMap<>();
        String imageUrl = "";
        if (popularProductModel != null) {
            imageUrl = popularProductModel.getImg_url();
        } else if (newProductModel != null) {
            imageUrl = newProductModel.getImg_url();
        } else if (showAllModel != null) {
            imageUrl = showAllModel.getImg_url();
        }
        cartMap.put("img_url", imageUrl);
        cartMap.put("productName", name.getText().toString());
        cartMap.put("productPrice", price.getText().toString());
        cartMap.put("saveCurrentDate", saveCurrentDate);
        cartMap.put("saveCurrentTime", saveCurrentTime);
        cartMap.put("totalQuanlity", quanlity.getText().toString());
        cartMap.put("totalPrice", totalPrice);

        firestore.collection("AddToCart").document(mAuth.getCurrentUser().getUid())
                .collection("User").add(cartMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        Toast.makeText(DetailedActivity.this, "Thêm vào giỏ hàng thành công", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
    }
}