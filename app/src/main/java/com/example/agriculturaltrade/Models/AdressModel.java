package com.example.agriculturaltrade.Models;

import android.widget.TextView;

public class AdressModel {
    String userAddress;
    boolean isSelected;

    public  AdressModel() {}

    public AdressModel(String userAddress, boolean isSelected) {
        this.userAddress = userAddress;
        this.isSelected = isSelected;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
