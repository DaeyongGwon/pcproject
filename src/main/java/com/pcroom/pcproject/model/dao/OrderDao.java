package com.pcroom.pcproject.model.dao;

import com.pcroom.pcproject.model.dto.OrderDto;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderDao {
    private static final String URL = "jdbc:oracle:thin:@localhost:1521:xe";
    private static final String USER = "pcroom";
    private static final String PASSWORD = "pcroom";

    // 주문 추가
    public static void addOrder(OrderDto order) throws SQLException {
        String query = "INSERT INTO ORDERS (ORDERID, ITEM_NAME, USERID, ORDER_DATE, TOTAL_PRICE) VALUES (ORDER_ID_SEQ.NEXTVAL, ?, ?, ?, ?)";
        int orderId = -1; // 주문 ID를 저장할 변수 초기화
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(query)){
            ps.setString(1, order.getItemName());
            ps.setInt(2, order.getUserId());
            ps.setDate(3, new java.sql.Date(order.getOrderDate().getTime()));
            ps.setInt(4, order.getTotalPrice());
            ps.executeUpdate();

            // 시퀀스로부터 현재 값 얻기
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT ORDER_ID_SEQ.CURRVAL FROM DUAL")) {
                if (rs.next()) {
                    orderId = rs.getInt(1);
                }
            }
        }
        order.setOrderId(orderId);
    }


    // 주문 조회
    public static List<OrderDto> getOrders() throws SQLException {
        List<OrderDto> orders = new ArrayList<>();
        String query = "SELECT * FROM ORDERS";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(query)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String itemName = rs.getString("ITEM_NAME");
                int userId = rs.getInt("USERID");
                Date orderDate = rs.getDate("ORDER_DATE");
                int totalPrice = rs.getInt("TOTAL_PRICE");
                int orderId = rs.getInt("ORDERID");
                OrderDto order = new OrderDto(itemName ,orderId, userId, orderDate, totalPrice);
                orders.add(order);
            }
        }
        return orders;
    }
    // 모든 주문 조회
    public static List<OrderDto> getAllOrders() throws SQLException {
        List<OrderDto> allOrders = new ArrayList<>();
        String query = "SELECT ORDERS.*, USERS.NICKNAME FROM ORDERS JOIN USERS ON ORDERS.USERID = USERS.ID";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(query)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int orderId = rs.getInt("ORDERID");
                String itemName = rs.getString("ITEM_NAME");
                int userId = rs.getInt("USERID");
                String userNickname = rs.getString("NICKNAME");
                Date orderDate = rs.getDate("ORDER_DATE");
                int totalPrice = rs.getInt("TOTAL_PRICE");
                OrderDto order = new OrderDto(orderId, itemName, userId, userNickname, orderDate, totalPrice);
                allOrders.add(order);
            }
        }
        return allOrders;
    }

    // 특정 사용자의 주문 조회
    public static List<OrderDto> getUserOrders(int userId) throws SQLException {
        List<OrderDto> userOrders = new ArrayList<>();
        String query = "SELECT * FROM ORDERS WHERE USERID = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String itemName = rs.getString("ITEM_NAME");
                int orderId = rs.getInt("ORDERID");
                Date orderDate = rs.getDate("ORDER_DATE");
                int totalPrice = rs.getInt("TOTAL_PRICE");
                OrderDto order = new OrderDto(itemName, orderId, userId, orderDate, totalPrice);
                userOrders.add(order);
            }
        }
        return userOrders;
    }
}
