package com.example.agriculturaltrade.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.agriculturaltrade.Activities.DetailedActivity;
import com.example.agriculturaltrade.Models.ShowAllModel;
import com.example.agriculturaltrade.R;

import java.util.ArrayList;
import java.util.List;

public class ShowAllAdapter extends RecyclerView.Adapter<ShowAllAdapter.ViewHolder> implements Filterable {

    private Context context;
    private List<ShowAllModel> list;
    private List<ShowAllModel> listSearch;

    public ShowAllAdapter(Context context, List<ShowAllModel> list, List<ShowAllModel> listSearch) {
        this.context = context;
        this.list = list;
        this.listSearch = listSearch;
    }

    @NonNull
    @Override
    public ShowAllAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.show_all_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ShowAllAdapter.ViewHolder holder, int position) {
        Glide.with(context).load(list.get(position).getImg_url()).into(holder.mItemImg);
        holder.mName.setText(list.get(position).getName());
        holder.mCost.setText(String.valueOf(list.get(position).getPrice())  + " VNĐ");
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
        ImageView mItemImg;
        TextView mCost,mName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mItemImg = itemView.findViewById(R.id.item_image);
            mCost = itemView.findViewById(R.id.item_cost);
            mName = itemView.findViewById(R.id.item_name);
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String strSearch = constraint.toString();
                if (strSearch.isEmpty()) {
                    listSearch = list;
                }else {
                    listSearch.clear();
                    List<ShowAllModel> listSearch1 = new ArrayList<>();
                    for (ShowAllModel model : list) {
                        if (model.getName().toLowerCase().contains(strSearch.toLowerCase())) {
                            listSearch1.add(model);
                        }
                    }
                    listSearch = listSearch1;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = listSearch;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                listSearch = (List<ShowAllModel>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}
