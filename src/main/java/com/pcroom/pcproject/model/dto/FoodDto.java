package com.pcroom.pcproject.model.dto;

import java.util.List;

public class FoodDto {
    private String title;
    private String description;
    private int price;
    private int oldPrice;
    private List<String> labels;
    private String imagePath;

    // 생성자
    public FoodDto(String title, String description, int price, int oldPrice, List<String> labels, String imagePath) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.oldPrice = oldPrice;
        this.labels = labels;
        this.imagePath = imagePath;
    }

    // 게터와 세터
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getOldPrice() {
        return oldPrice;
    }

    public void setOldPrice(int oldPrice) {
        this.oldPrice = oldPrice;
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
