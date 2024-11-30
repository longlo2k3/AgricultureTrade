package com.example.agriculturaltrade.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.agriculturaltrade.Activities.DetailedActivity;
import com.example.agriculturaltrade.Models.MyCartModel;
import com.example.agriculturaltrade.R;

import java.util.List;

public class MyCartAdapter extends RecyclerView.Adapter<MyCartAdapter.ViewHolder> {

    private Context context;
    private List<MyCartModel> list;

    int totalAmout = 0;

    public MyCartAdapter(Context context, List<MyCartModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyCartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext().getApplicationContext()).inflate(R.layout.my_cart_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyCartAdapter.ViewHolder holder, int position) {
        Glide.with(context).load(list.get(position).getImg_url()).into(holder.img_url);
        holder.currentTime.setText("Giờ đặt: "+list.get(position).getSaveCurrentTime());
        holder.currentDate.setText("Ngày đặt: "+list.get(position).getSaveCurrentDate());
        holder.productName.setText(list.get(position).getProductName());
        holder.productPrice.setText("Giá: "+String.valueOf(list.get(position).getProductPrice()) + " VNĐ");
        holder.totalQuanlity.setText("Số lượng: "+String.valueOf(list.get(position).getTotalQuanlity()));
        holder.totalPrice.setText("Giá tổng:"+String.valueOf(list.get(position).getTotalPrice()) + " VNĐ");

        // Tính tổng giá trị các sản phẩm trong giỏ hàng
        totalAmout += list.get(position).getTotalPrice();

        // Gửi một intent broadcast với action là "MyTotalAmount" và kèm theo một extra tên là "totalAmount" với giá trị là totalAmout
        //Intent này sẽ được nhận bởi một BroadcastReceiver nào đó, và có thể được sử dụng để update UI của ứng dụng
        Intent intent = new Intent("MyTotalAmount");
        intent.putExtra("totalAmount", totalAmout);

        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

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

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView currentTime, currentDate, productName, productPrice, totalQuanlity,totalPrice;
        ImageView img_url;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            currentTime = itemView.findViewById(R.id.current_time);
            img_url = itemView.findViewById(R.id.img_url);
//            img_url = String.valueOf(itemView.findViewById(R.id.img_url));
            currentDate = itemView.findViewById(R.id.current_date);
            productName = itemView.findViewById(R.id.product_name);
            productPrice = itemView.findViewById(R.id.product_price);
            totalQuanlity = itemView.findViewById(R.id.total_quantity);
            totalPrice = itemView.findViewById(R.id.total_price);

        }
    }
}
