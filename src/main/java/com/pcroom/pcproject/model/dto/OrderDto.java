package com.pcroom.pcproject.model.dto;

import java.util.Date;

public class OrderDto {
    private int orderId;
    private int userId;
    private Date orderDate;
    private int totalPrice;

    // 생성자, 게터 및 세터
    public OrderDto() {}

    public OrderDto(int userId, Date orderDate, int totalPrice) {
        this.userId = userId;
        this.orderDate = orderDate;
        this.totalPrice = totalPrice;
    }

    // 게터 및 세터
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
