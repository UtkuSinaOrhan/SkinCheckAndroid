package com.example.newskincheckapp;

public class Product {
    private String name;
    private int imageResId;
    private String url;


    public Product(String name, int imageResId, String url) {
        this.name = name;
        this.imageResId = imageResId;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public int getImageResId() {
        return imageResId;
    }

    public String getUrl() {
        return url;
    }
}
