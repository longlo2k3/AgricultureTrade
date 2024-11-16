package com.example.agriculturaltrade.Models;

public class PopularProductModel {
    String img_url;
    String name;
    String description;
    String rating;
    Integer  price;
    public PopularProductModel() {
    }
    public PopularProductModel(String img_url, String name, String description, String rating, Integer price) {
        this.img_url = img_url;
        this.name = name;
        this.description = description;
        this.rating = rating;
        this.price = price;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
}
