package com.example.agriculturaltrade.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.agriculturaltrade.Activities.DetailedActivity;
import com.example.agriculturaltrade.Models.PopularProductModel;
import com.example.agriculturaltrade.R;

import java.io.Serializable;
import java.util.List;

// sử dụng ViewHolder để tái sử dụng và quản lý các view item.
public class PopularProductAdapter extends RecyclerView.Adapter<PopularProductAdapter.ViewHolder> {

    private List<PopularProductModel> list;
    private Context context;

    public PopularProductAdapter( Context context, List<PopularProductModel> list) {
        this.list = list;
        this.context = context;
    }


    // onCreateViewHolder sử dụng để tạo view item.
    @NonNull
    @Override
    public PopularProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //LayoutInflater sử dụng để convert 1 layout xml thành 1 view
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.popular_product, parent, false));
    }

    // onBindViewHolder sử dụng để  tái sử dụng và quản lý các view item.
    @Override
    public void onBindViewHolder(@NonNull PopularProductAdapter.ViewHolder holder, int position) {
        // Glide sử dụng để tạo hình anh tu url
        Glide.with(context).load(list.get(position).getImg_url()).into(holder.popularImgUrl);
        holder.popularName.setText(list.get(position).getName());
        holder.popularPrice.setText(String.valueOf(list.get(position).getPrice()));

        // Click về chi tiết sản phẩm
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailedActivity.class);
                intent.putExtra("detailed", list.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    //ViewHolder sử dụng để tạo view item.
    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView popularImgUrl;
        TextView popularName, popularPrice;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            popularImgUrl = itemView.findViewById(R.id.all_img);
            popularName = itemView.findViewById(R.id.all_product_name);
            popularPrice = itemView.findViewById(R.id.all_price);
        }
    }
}
