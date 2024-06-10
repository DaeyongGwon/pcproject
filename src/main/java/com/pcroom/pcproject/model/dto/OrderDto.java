package com.pcroom.pcproject.model.dto;

import java.util.Date;

public class OrderDto {
    private int orderId;
    private int userId;
    private String itemName;
    private Date orderDate;
    private int totalPrice;

    // 생성자, 게터 및 세터
    public OrderDto() {}

    public OrderDto(String itemName ,int userId, Date orderDate, int totalPrice) {
        this.itemName = itemName;
        this.userId = userId;
        this.orderDate = orderDate;
        this.totalPrice = totalPrice;
    }

    public OrderDto(String itemName, int orderId, int userId, Date orderDate, int totalPrice) {
        this.itemName = itemName;
        this.orderId = orderId;
        this.userId = userId;
        this.orderDate = orderDate;
        this.totalPrice = totalPrice;
    }

    // 게터 및 세터
    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }
}
