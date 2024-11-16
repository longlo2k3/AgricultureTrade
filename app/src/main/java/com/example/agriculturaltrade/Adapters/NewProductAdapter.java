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
import com.example.agriculturaltrade.Models.NewProductModel;
import com.example.agriculturaltrade.R;

import java.util.List;

public class NewProductAdapter extends RecyclerView.Adapter<NewProductAdapter.Viewholder> {

    private List<NewProductModel> list;
    private Context context;

    public NewProductAdapter( Context context, List<NewProductModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public NewProductAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Viewholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.new_product, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NewProductAdapter.Viewholder holder, int position) {
        Glide.with(context).load(list.get(position).getImg_url()).into(holder.newImg);
        holder.newName.setText(list.get(position).getName());
        holder.newPrice.setText(String.valueOf(list.get(position).getPrice()) );
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class  Viewholder extends RecyclerView.ViewHolder {
        ImageView newImg;
        TextView newName,newPrice;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            newImg = itemView.findViewById(R.id.new_img);
            newName = itemView.findViewById(R.id.new_product_name);
            newPrice = itemView.findViewById(R.id.new_price);
        }
    }
}
