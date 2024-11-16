package com.example.agriculturaltrade.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.agriculturaltrade.Models.PopularProductModel;
import com.example.agriculturaltrade.R;

import java.util.List;

public class PopularProductAdapter extends RecyclerView.Adapter<PopularProductAdapter.ViewHolder> {

    private List<PopularProductModel> list;
    private Context context;

    public PopularProductAdapter( Context context, List<PopularProductModel> list) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public PopularProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.popular_product, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PopularProductAdapter.ViewHolder holder, int position) {
        Glide.with(context).load(list.get(position).getImg_url()).into(holder.popularImgUrl);
        holder.popularName.setText(list.get(position).getName());
        holder.popularPrice.setText(String.valueOf(list.get(position).getPrice()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView popularImgUrl;
        TextView popularName, popularPrice;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            popularImgUrl = itemView.findViewById(R.id.popular_products_img);
            popularName = itemView.findViewById(R.id.popular_products_name);
            popularPrice = itemView.findViewById(R.id.popular_products_price);
        }
    }
}
