package com.example.agriculturaltrade.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.agriculturaltrade.Activities.DetailedActivity;
import com.example.agriculturaltrade.Activities.UpdateMyProductActivity;
import com.example.agriculturaltrade.Models.MyProductModel;
import com.example.agriculturaltrade.Models.ShowAllModel;
import com.example.agriculturaltrade.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.List;

public class MyProductAdapter extends RecyclerView.Adapter<MyProductAdapter.ViewHolder>{

    private Context context;
    private List<MyProductModel> list;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public MyProductAdapter(Context context, List<MyProductModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyProductAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.my_product_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyProductAdapter.ViewHolder holder, int position) {

        MyProductModel product = list.get(position);

        Glide.with(context).load(list.get(position).getImg_url()).into(holder.mItemImg);
        holder.mName.setText(list.get(position).getName());
        holder.mCost.setText(String.valueOf(list.get(position).getPrice())  + " VNĐ");
        holder.updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UpdateMyProductActivity.class);
                intent.putExtra("myproduct", list.get(position));
                context.startActivity(intent);
            }
        });

        holder.delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("AddProduct").document(mAuth.getCurrentUser().getUid())
                        .collection("NewProducts")
                        .whereEqualTo("name", list.get(position).getName()) // Thay "field" và "value" bằng điều kiện tìm kiếm
                        .get()
                        .addOnSuccessListener(queryDocumentSnapshots -> {
                            for (DocumentSnapshot document : queryDocumentSnapshots) {
                                document.getReference().delete();
                            }
                            list.remove(position);
                            notifyDataSetChanged();
                            Log.d("Firestore", "Document đã được xóa!");
                        })
                        .addOnFailureListener(e -> {
                            Log.e("Firestore", "Lỗi khi truy vấn document", e);
                        });
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mItemImg;
        TextView mCost,mName;
        Button delBtn,updateBtn;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mItemImg = itemView.findViewById(R.id.item_image);
            mCost = itemView.findViewById(R.id.item_cost);
            mName = itemView.findViewById(R.id.item_name);
            delBtn = itemView.findViewById(R.id.btn_delete);
            updateBtn = itemView.findViewById(R.id.btn_update);
        }
    }
}
