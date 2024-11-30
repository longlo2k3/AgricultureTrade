package com.example.agriculturaltrade.Activities;

import static com.example.agriculturaltrade.R.id.home_toolbar_activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.agriculturaltrade.Adapters.ShowAllAdapter;
import com.example.agriculturaltrade.Models.ShowAllModel;
import com.example.agriculturaltrade.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ShowAllActivity extends AppCompatActivity {

    RecyclerView showAllRecyclerView;
    ShowAllAdapter showAllAdapter;
    List<ShowAllModel> showAllModelList;
    EditText inputSearch;
    ImageView btnSearch;
    ImageView MicSearch;

    Toolbar toolbar, toolbar_activity;

    FirebaseFirestore db = FirebaseFirestore.getInstance();


    private MediaRecorder mediaRecorder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all);

        toolbarShow();
        toolbarAction();

        String type = getIntent().getStringExtra("type");
        inputSearch = findViewById(R.id.input_search);
        btnSearch = findViewById(R.id.btn_search);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSearchAction();
            }
        });

        showAllRecyclerView = findViewById(R.id.show_all_rec);
        showAllRecyclerView.setLayoutManager(new GridLayoutManager(this,2));
        showAllModelList = new ArrayList<>();
        showAllAdapter = new ShowAllAdapter(this, showAllModelList,showAllModelList);
        showAllRecyclerView.setAdapter(showAllAdapter);
        getData(type);

        //Add to cart
    }

    private void getSearchAction() {
        String search = inputSearch.getText().toString().trim();
        if (!search.isEmpty()) {
            // Làm mới danh sách
            showAllModelList.clear();
            showAllAdapter.notifyDataSetChanged();

            int[] tasksCompleted = {0}; // Bộ đếm cho các truy vấn
            int totalTasks = 3; // Số truy vấn cần thực hiện

            // Thực hiện các truy vấn
            searchFromCollection("ShowAll", "name", search, tasksCompleted, totalTasks);
            searchFromCollection("NewProduct", "name", search, tasksCompleted, totalTasks);
            searchFromCollection("PopularProducts", "name", search, tasksCompleted, totalTasks);
        } else {
            // Tìm kiếm không có điều kiện
            showAllModelList.clear();
            showAllAdapter.notifyDataSetChanged();
            searchNonCondition();
        }
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


    private void searchFromCollection(String collectionName, String fieldName, String searchValue, int[] tasksCompleted, int totalTasks){
        db.collection(collectionName)
                .orderBy(fieldName) // Cần tạo chỉ mục trên Firestore
                .startAt(searchValue) // Bắt đầu với giá trị tìm kiếm
                .endAt(searchValue + "\uf8ff") // Kết thúc với giá trị tương đồng
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            ShowAllModel showAllModel = document.toObject(ShowAllModel.class);
                            showAllModelList.add(showAllModel);
                            showAllAdapter.notifyDataSetChanged();
                        }
                    } else {
                        Log.e("Firestore", "Error querying " + collectionName, task.getException());
                    }

                    // Kiểm tra hoàn thành
                    checkTasksCompleted(tasksCompleted, totalTasks, showAllAdapter);
                });
    }

    private void searchNonCondition(){
        int[] tasksCompleted = {0}; // Dùng mảng để cập nhật giá trị tham chiếu
        int totalTasks = 3; // Tổng số truy vấn cần hoàn thành
        // Truy vấn từ ShowAll
        db.collection("ShowAll")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            ShowAllModel showAllModel = document.toObject(ShowAllModel.class);
                            showAllModelList.add(showAllModel);
                        }
                    }
                    checkTasksCompleted(tasksCompleted, totalTasks, showAllAdapter);
                });

        // Truy vấn từ NewProduct
        db.collection("NewProduct")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            ShowAllModel showAllModel = document.toObject(ShowAllModel.class);
                            showAllModelList.add(showAllModel);
                        }
                    }
                    checkTasksCompleted(tasksCompleted, totalTasks, showAllAdapter);
                });

        // Truy vấn từ PopularProduct
        db.collection("PopularProducts")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            ShowAllModel showAllModel = document.toObject(ShowAllModel.class);
                            showAllModelList.add(showAllModel);
                        }
                    }
                    checkTasksCompleted(tasksCompleted, totalTasks, showAllAdapter);
                });
    }
    private void getData(String type) {
        if (type == null || type.isEmpty()) {
            int[] tasksCompleted = {0}; // Dùng mảng để cập nhật giá trị tham chiếu
            int totalTasks = 3; // Tổng số truy vấn cần hoàn thành

            // Truy vấn từ ShowAll
            db.collection("ShowAll")
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ShowAllModel showAllModel = document.toObject(ShowAllModel.class);
                                showAllModelList.add(showAllModel);
                            }
                        }
                        checkTasksCompleted(tasksCompleted, totalTasks, showAllAdapter);
                    });

            // Truy vấn từ NewProduct
            db.collection("NewProduct")
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ShowAllModel showAllModel = document.toObject(ShowAllModel.class);
                                showAllModelList.add(showAllModel);
                            }
                        }
                        checkTasksCompleted(tasksCompleted, totalTasks, showAllAdapter);
                    });

            // Truy vấn từ PopularProduct
            db.collection("PopularProducts")
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ShowAllModel showAllModel = document.toObject(ShowAllModel.class);
                                showAllModelList.add(showAllModel);
                            }
                        }
                        checkTasksCompleted(tasksCompleted, totalTasks, showAllAdapter);
                    });
        }

        if (type != null && type.equalsIgnoreCase("luongthuc")) {
            int[] tasksCompleted = {0}; // Dùng mảng để cập nhật giá trị tham chiếu
            int totalTasks = 3; // Tổng số truy vấn cần hoàn thành

            // Truy vấn từ ShowAll
            db.collection("ShowAll")
                    .whereEqualTo("type", "luongthuc")
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ShowAllModel showAllModel = document.toObject(ShowAllModel.class);
                                showAllModelList.add(showAllModel);
                            }
                        }
                        checkTasksCompleted(tasksCompleted, totalTasks, showAllAdapter);
                    });

            // Truy vấn từ NewProduct
            db.collection("NewProduct")
                    .whereEqualTo("type", "luongthuc")
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ShowAllModel showAllModel = document.toObject(ShowAllModel.class);
                                showAllModelList.add(showAllModel);
                            }
                        }
                        checkTasksCompleted(tasksCompleted, totalTasks, showAllAdapter);
                    });

            // Truy vấn từ PopularProduct
            db.collection("PopularProducts")
                    .whereEqualTo("type", "luongthuc")
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ShowAllModel showAllModel = document.toObject(ShowAllModel.class);
                                showAllModelList.add(showAllModel);
                            }
                        }
                        checkTasksCompleted(tasksCompleted, totalTasks, showAllAdapter);
                    });
        }
        //-------------------------------

        if (type != null && type.equalsIgnoreCase("traicay")) {
            int[] tasksCompleted = {0}; // Dùng mảng để cập nhật giá trị tham chiếu
            int totalTasks = 3; // Tổng số truy vấn cần hoàn thành

            // Truy vấn từ ShowAll
            db.collection("ShowAll")
                    .whereEqualTo("type", "traicay")
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ShowAllModel showAllModel = document.toObject(ShowAllModel.class);
                                showAllModelList.add(showAllModel);
                            }
                        }
                        checkTasksCompleted(tasksCompleted, totalTasks, showAllAdapter);
                    });

            // Truy vấn từ NewProduct
            db.collection("NewProduct")
                    .whereEqualTo("type", "traicay")
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ShowAllModel showAllModel = document.toObject(ShowAllModel.class);
                                showAllModelList.add(showAllModel);
                            }
                        }
                        checkTasksCompleted(tasksCompleted, totalTasks, showAllAdapter);
                    });

            // Truy vấn từ PopularProduct
            db.collection("PopularProducts")
                    .whereEqualTo("type", "traicay")
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ShowAllModel showAllModel = document.toObject(ShowAllModel.class);
                                showAllModelList.add(showAllModel);
                            }
                        }
                        checkTasksCompleted(tasksCompleted, totalTasks, showAllAdapter);
                    });
        }

        //--------------------------------

        if (type != null && type.equalsIgnoreCase("raucu")) {
            int[] tasksCompleted = {0}; // Dùng mảng để cập nhật giá trị tham chiếu
            int totalTasks = 3; // Tổng số truy vấn cần hoàn thành

            // Truy vấn từ ShowAll
            db.collection("ShowAll")
                    .whereEqualTo("type", "raucu")
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ShowAllModel showAllModel = document.toObject(ShowAllModel.class);
                                showAllModelList.add(showAllModel);
                            }
                        }
                        checkTasksCompleted(tasksCompleted, totalTasks, showAllAdapter);
                    });

            // Truy vấn từ NewProduct
            db.collection("NewProduct")
                    .whereEqualTo("type", "raucu")
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ShowAllModel showAllModel = document.toObject(ShowAllModel.class);
                                showAllModelList.add(showAllModel);
                            }
                        }
                        checkTasksCompleted(tasksCompleted, totalTasks, showAllAdapter);
                    });

            // Truy vấn từ PopularProduct
            db.collection("PopularProducts")
                    .whereEqualTo("type", "raucu")
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ShowAllModel showAllModel = document.toObject(ShowAllModel.class);
                                showAllModelList.add(showAllModel);
                            }
                        }
                        checkTasksCompleted(tasksCompleted, totalTasks, showAllAdapter);
                    });
        }

        //
        if (type != null && type.equalsIgnoreCase("dacsan")) {
            int[] tasksCompleted = {0}; // Dùng mảng để cập nhật giá trị tham chiếu
            int totalTasks = 3; // Tổng số truy vấn cần hoàn thành

            // Truy vấn từ ShowAll
            db.collection("ShowAll")
                    .whereEqualTo("type", "dacsan")
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ShowAllModel showAllModel = document.toObject(ShowAllModel.class);
                                showAllModelList.add(showAllModel);
                            }
                        }
                        checkTasksCompleted(tasksCompleted, totalTasks, showAllAdapter);
                    });

            // Truy vấn từ NewProduct
            db.collection("NewProduct")
                    .whereEqualTo("type", "dacsan")
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ShowAllModel showAllModel = document.toObject(ShowAllModel.class);
                                showAllModelList.add(showAllModel);
                            }
                        }
                        checkTasksCompleted(tasksCompleted, totalTasks, showAllAdapter);
                    });

            // Truy vấn từ PopularProduct
            db.collection("PopularProducts")
                    .whereEqualTo("type", "dacsan")
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ShowAllModel showAllModel = document.toObject(ShowAllModel.class);
                                showAllModelList.add(showAllModel);
                            }
                        }
                        checkTasksCompleted(tasksCompleted, totalTasks, showAllAdapter);
                    });
        }

        //
        if (type != null && type.equalsIgnoreCase("hoa")) {
            int[] tasksCompleted = {0}; // Dùng mảng để cập nhật giá trị tham chiếu
            int totalTasks = 3; // Tổng số truy vấn cần hoàn thành

            // Truy vấn từ ShowAll
            db.collection("ShowAll")
                    .whereEqualTo("type", "hoa")
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ShowAllModel showAllModel = document.toObject(ShowAllModel.class);
                                showAllModelList.add(showAllModel);
                            }
                        }
                        checkTasksCompleted(tasksCompleted, totalTasks, showAllAdapter);
                    });

            // Truy vấn từ NewProduct
            db.collection("NewProduct")
                    .whereEqualTo("type", "hoa")
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ShowAllModel showAllModel = document.toObject(ShowAllModel.class);
                                showAllModelList.add(showAllModel);
                            }
                        }
                        checkTasksCompleted(tasksCompleted, totalTasks, showAllAdapter);
                    });

            // Truy vấn từ PopularProduct
            db.collection("PopularProducts")
                    .whereEqualTo("type", "hoa")
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ShowAllModel showAllModel = document.toObject(ShowAllModel.class);
                                showAllModelList.add(showAllModel);
                            }
                        }
                        checkTasksCompleted(tasksCompleted, totalTasks, showAllAdapter);
                    });
        }
    }
    private void checkTasksCompleted(int[] tasksCompleted, int totalTasks, ShowAllAdapter adapter) {
        tasksCompleted[0]++; // Cập nhật số lượng task hoàn thành
        if (tasksCompleted[0] == totalTasks) {
            // Tất cả các task hoàn thành, cập nhật adapter
            adapter.notifyDataSetChanged();
        }
    }
}