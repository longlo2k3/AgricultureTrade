package com.example.agriculturaltrade.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.agriculturaltrade.Adapters.ShowAllAdapter;
import com.example.agriculturaltrade.Models.ShowAllModel;
import com.example.agriculturaltrade.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ShowAllActivity extends AppCompatActivity {

    RecyclerView showAllRecyclerView;
    ShowAllAdapter showAllAdapter;
    List<ShowAllModel> showAllModelList;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all);

        String type = getIntent().getStringExtra("type");


        showAllRecyclerView = findViewById(R.id.show_all_rec);
        showAllRecyclerView.setLayoutManager(new GridLayoutManager(this,2));
        showAllModelList = new ArrayList<>();
        showAllAdapter = new ShowAllAdapter(this, showAllModelList);
        showAllRecyclerView.setAdapter(showAllAdapter);

        getData1(type);
    }
    private void getData(String type) {
        if (type == null || type.isEmpty()) {
            db.collection("ShowAll")
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ShowAllModel showAllModel = document.toObject(ShowAllModel.class);
                                showAllModelList.add(showAllModel);
                                showAllAdapter.notifyDataSetChanged();
                            }
                        }
                    });
        }
        if (type != null && type.equalsIgnoreCase("luongthuc")) {
            db.collection("ShowAll")
                    .whereEqualTo("type", "luongthuc")
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ShowAllModel showAllModel = document.toObject(ShowAllModel.class);
                                showAllModelList.add(showAllModel);
                                showAllAdapter.notifyDataSetChanged();
                            }
                        }
                    });
        }
        if (type != null && type.equalsIgnoreCase("traicay")) {
            db.collection("ShowAll")
                    .whereEqualTo("type", "traicay")
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ShowAllModel showAllModel = document.toObject(ShowAllModel.class);
                                showAllModelList.add(showAllModel);
                                showAllAdapter.notifyDataSetChanged();
                            }
                        }


                    });
        }
        if (type != null && type.equalsIgnoreCase("raucu")) {
            db.collection("ShowAll")
                    .whereEqualTo("type", "raucu")
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ShowAllModel showAllModel = document.toObject(ShowAllModel.class);
                                showAllModelList.add(showAllModel);
                                showAllAdapter.notifyDataSetChanged();
                            }
                        }

                    });
        }
        if (type != null && type.equalsIgnoreCase("dacsan")) {
            db.collection("ShowAll")
                    .whereEqualTo("type", "dacsan")
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ShowAllModel showAllModel = document.toObject(ShowAllModel.class);
                                showAllModelList.add(showAllModel);
                                showAllAdapter.notifyDataSetChanged();
                            }
                        }

                    });
        }
        if (type != null && type.equalsIgnoreCase("hoa")) {
            db.collection("ShowAll")
                    .whereEqualTo("type", "hoa")
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ShowAllModel showAllModel = document.toObject(ShowAllModel.class);
                                showAllModelList.add(showAllModel);
                                showAllAdapter.notifyDataSetChanged();
                            }
                        }

                    });
        }
    }
    private void getData1(String type) {
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