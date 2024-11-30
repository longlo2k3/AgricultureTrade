package com.example.agriculturaltrade.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agriculturaltrade.Models.AdressModel;
import com.example.agriculturaltrade.R;

import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {

    Context context;
    List<AdressModel> list;
    SelectedAddress selectedAddress;

    private RadioButton selectedRadioButton;

    public AddressAdapter(Context context, List<AdressModel> list, SelectedAddress selectedAddress) {
        this.context = context;
        this.list = list;
        this.selectedAddress = selectedAddress;
    }

    @NonNull
    @Override
    public AddressAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.address_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AddressAdapter.ViewHolder holder, int position) {
        holder.address.setText(list.get(position).getUserAddress());
        holder.radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (AdressModel addressModel : list) {
                    addressModel.setSelected(false);
                }
                list.get(position).setSelected(true);

                if (selectedRadioButton != null) {
                    selectedRadioButton.setChecked(false);
                }
                selectedRadioButton = (RadioButton) v;
                selectedRadioButton.setChecked(true);
                selectedAddress.setAddress(list.get(position).getUserAddress());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView address;
        RadioButton radioButton;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            address = itemView.findViewById(R.id.address_add);
            radioButton = itemView.findViewById(R.id.select_address);
        }
    }

    public interface SelectedAddress{
        void setAddress(String address);
    }
}
