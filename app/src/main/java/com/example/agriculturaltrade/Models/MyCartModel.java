package com.example.agriculturaltrade.Models;

import java.io.Serializable;

public class MyCartModel implements Serializable {
    String saveCurrentDate;
    String img_url;
    String saveCurrentTime;
    String productName;
    String productPrice;
    String totalQuanlity;
    Integer totalPrice;

    public MyCartModel() {

    }

    public MyCartModel(String saveCurrentDate, String img_url, String saveCurrentTime, String productName, String productPrice, String totalQuanlity, int totalPrice) {
        this.saveCurrentDate = saveCurrentDate;
        this.img_url = img_url;
        this.saveCurrentTime = saveCurrentTime;
        this.productName = productName;
        this.productPrice = productPrice;
        this.totalQuanlity = totalQuanlity;
        this.totalPrice = totalPrice;
    }

    public String getSaveCurrentDate() {
        return saveCurrentDate;
    }

    public void setSaveCurrentDate(String saveCurrentDate) {
        this.saveCurrentDate = saveCurrentDate;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getSaveCurrentTime() {
        return saveCurrentTime;
    }

    public void setSaveCurrentTime(String saveCurrentTime) {
        this.saveCurrentTime = saveCurrentTime;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getTotalQuanlity() {
        return totalQuanlity;
    }

    public void setTotalQuanlity(String totalQuanlity) {
        this.totalQuanlity = totalQuanlity;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }
}
