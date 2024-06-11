package com.pcroom.pcproject.model.dto;

public class OrderItemDTO {
    private int orderItemId;
    private int orderId;
    private int foodId;
    private int quantity;
    private double price;

    // Constructors
    public OrderItemDTO(int orderItemId, int orderId, int foodId, int quantity, double price) {
        this.orderItemId = orderItemId;
        this.orderId = orderId;
        this.foodId = foodId;
        this.quantity = quantity;
        this.price = price;
    }

    public OrderItemDTO(int orderId, int foodId, int quantity, double price) {
        this.orderId = orderId;
        this.foodId = foodId;
        this.quantity = quantity;
        this.price = price;
    }

    public OrderItemDTO() {
    }

    // Getters and Setters
    public int getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(int orderItemId) {
        this.orderItemId = orderItemId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getFoodId() {
        return foodId;
    }

    public void setFoodId(int foodId) {
        this.foodId = foodId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
