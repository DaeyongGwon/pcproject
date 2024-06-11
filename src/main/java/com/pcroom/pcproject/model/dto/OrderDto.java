package com.pcroom.pcproject.model.dto;

import java.util.Date;

public class OrderDto {
    private int orderId;
    private String itemName;
    private int userId;
    private String userNickname; // For getAllOrders method
    private Date orderDate;
    private int totalPrice;
    private int seatNumber; // New field

    // Constructors
    public OrderDto(String itemName, int userId, Date orderDate, int totalPrice, int seatNumber) {
        this.itemName = itemName;
        this.userId = userId;
        this.orderDate = orderDate;
        this.totalPrice = totalPrice;
        this.seatNumber = seatNumber;
    }

    public OrderDto(String itemName, int userId, java.sql.Date orderDate, int totalPrice) {
        this.itemName = itemName;
        this.userId = userId;
        this.orderDate = orderDate;
        this.totalPrice = totalPrice;
    }

    public OrderDto(int orderId, String itemName, int userId, String userNickname, Date orderDate, int totalPrice) {
        this.orderId = orderId;
        this.itemName = itemName;
        this.userId = userId;
        this.userNickname = userNickname;
        this.orderDate = orderDate;
        this.totalPrice = totalPrice;
    }

    public OrderDto(String itemName, int userId, java.sql.Date orderDate, int totalPrice, int seatNumber, int seatNumber1) {
        this.itemName = itemName;
        this.userId = userId;
        this.orderDate = orderDate;
        this.totalPrice = totalPrice;
        this.seatNumber = seatNumber1;
    }

    // Getters and setters
    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
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

    public int getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }
}
