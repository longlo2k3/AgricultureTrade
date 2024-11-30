package com.example.agriculturaltrade.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.example.agriculturaltrade.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AddAdressActivity extends AppCompatActivity {
    EditText name, phoneNumber, tvLocation;
    Toolbar toolbar;

    Button btnAddAddress;

    ImageView btnGetLocation;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;
    private FusedLocationProviderClient fusedLocationProviderClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_adress);

        name =  findViewById(R.id.ad_name);
        phoneNumber =  findViewById(R.id.ad_phone);
        tvLocation =  findViewById(R.id.ad_address);
        btnAddAddress = findViewById(R.id.ad_add_address);
        btnGetLocation = findViewById(R.id.btnGetLocation);

        // Khởi tạo FusedLocationProviderClient
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        toolbarShow();

        // Lấy vị trí khi nhấn nút
        btnGetLocation.setOnClickListener(v -> checkGPSAndRequestLocation());

        btnAddAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAddress();
            };
        });
    }

    private void addAddress() {
        String userName = name.getText().toString();
        String userPhoneNumber = phoneNumber.getText().toString();
        String userAddress = tvLocation.getText().toString();

        String final_address = "";

        if (!userName.isEmpty()){
            final_address+= "Tên: "+ userName + ",";
        }
        if (!userPhoneNumber.isEmpty()){
            final_address+= "SDT: "+userPhoneNumber+",";
        }
        if (!userAddress.isEmpty()){
            final_address+= "Địa chỉ: "+ userAddress+ ",";
        }
        if (!userName.isEmpty() && !userPhoneNumber.isEmpty() && !userAddress.isEmpty()) {
            Map<String, String> map = new HashMap<>();
            map.put("userAddress", final_address);

            db.collection("CurrentUser").document(mAuth.getCurrentUser().getUid())
                    .collection("Address")
                    .add(map)
                    .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(AddAdressActivity.this, "Address added successfully", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        else {
            Toast.makeText(AddAdressActivity.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
        }
    }

    private void toolbarShow() {
        toolbar = findViewById(R.id.add_address_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void checkGPSAndRequestLocation() {
        // Kiểm tra trạng thái GPS
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            // Hiển thị thông báo yêu cầu bật GPS
            new AlertDialog.Builder(this)
                    .setTitle("Bật GPS")
                    .setMessage("GPS đang bị tắt. Vui lòng bật GPS để lấy vị trí.")
                    .setPositiveButton("Bật", (dialog, which) -> startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)))
                    .setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss())
                    .show();
        } else {
            requestLocation();
        }
    }

    private void requestLocation() {
        // Kiểm tra quyền truy cập
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Yêu cầu quyền nếu chưa được cấp
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }

        // Tạo yêu cầu vị trí
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(1000); // Lấy vị trí mỗi 1 giây
        locationRequest.setFastestInterval(500); // Thời gian tối thiểu giữa các lần cập nhật

        // Xử lý callback khi có dữ liệu vị trí
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                if (locationResult == null) {
                    tvLocation.setText("Không thể lấy vị trí - LocationResult null.");
                    Log.e("LocationError", "LocationResult is null.");
                    return;
                }

                // Lấy vị trí hiện tại
                Location location = locationResult.getLastLocation();
                if (location != null) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();

                    // Hiển thị latitude, longitude trên màn hình
                    tvLocation.setText("Latitude: " + latitude + "\nLongitude: " + longitude);

                    // Gọi phương thức để chuyển đổi latitude, longitude thành địa chỉ
                    getAddressFromLocation1(latitude, longitude);
                } else {
                    tvLocation.setText("Không thể lấy được vị trí.");
                    Log.e("LocationError", "Location is null.");
                }
            }
        }, getMainLooper());
    }

    // Phương thức chuyển đổi latitude, longitude thành địa chỉ
    private void getAddressFromLocation(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                StringBuilder addressString = new StringBuilder();

                // Lấy thông tin địa chỉ từ đối tượng Address
                for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                    addressString.append(address.getAddressLine(i)).append("\n");
                }

                // Hiển thị địa chỉ trên màn hình
                tvLocation.setText("Địa chỉ: \n" + addressString.toString());
                Log.d("Location", "Address: " + addressString.toString());
            } else {
                tvLocation.setText("Không thể tìm thấy địa chỉ.");
                Log.e("LocationError", "No address found for the coordinates.");
            }
        } catch (IOException e) {
//            tvLocation.setText("Lỗi khi lấy địa chỉ.");
//            Log.e("LocationError", "Geocoder error: " + e.getMessage(), e);
            if (e.getMessage().contains("Service not Available")) {
                // Hiển thị thông báo cho người dùng hoặc thử lại sau
                Toast.makeText(this, "Dịch vụ Geocoding hiện không khả dụng. Hãy thử lại lần sau", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void getAddressFromLocation1(double latitude, double longitude) {
        // Tạo URL cho Nominatim API
        String urlString = "https://nominatim.openstreetmap.org/reverse?lat=" + latitude + "&lon=" + longitude + "&format=json";

        // Thực hiện yêu cầu HTTP để lấy dữ liệu địa chỉ từ Nominatim
        new Thread(() -> {
            try {
                // Mở kết nối HTTP
                URL url = new URL(urlString);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setConnectTimeout(5000); // Thời gian chờ kết nối
                urlConnection.setReadTimeout(5000);    // Thời gian chờ nhận dữ liệu

                // Đọc dữ liệu trả về từ Nominatim
                InputStream inputStream = urlConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }

                // Chuyển đổi JSON trả về thành một đối tượng Address
                String response = stringBuilder.toString();
                JSONObject jsonObject = new JSONObject(response);
                String address = jsonObject.optString("display_name");

                // Cập nhật giao diện người dùng với địa chỉ
                runOnUiThread(() -> {
                    if (address != null && !address.isEmpty()) {
                        tvLocation.setText("Địa chỉ: \n" + address);
                    } else {
                        tvLocation.setText("Không thể tìm thấy địa chỉ.");
                    }
                });

            } catch (IOException | JSONException e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    tvLocation.setText("Lỗi khi lấy địa chỉ.");
                });
            }
        }).start();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                requestLocation();
            } else {
                Toast.makeText(this, "Quyền truy cập vị trí bị từ chối", Toast.LENGTH_SHORT).show();
            }
        }
    }
}