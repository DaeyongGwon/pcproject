// DAO 패키지
package com.pcroom.pcproject.model.dao;

import com.pcroom.pcproject.model.dto.OrderItemDTO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class OrderItemDAO {
    private static final String URL = "jdbc:oracle:thin:@localhost:1521:xe";
    private static final String USER = "pcroom";
    private static final String PASSWORD = "pcroom";
    private Connection connection;

    // DB 연결 메서드, 권대용 작성
    private void connect() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // DB 연결 해제 메서드, 권대용 작성
    private void disconnect() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    // 음식 주문 아이템 추가 메서드, 권대용 작성
    public void addOrderItem(OrderItemDTO orderItem) throws SQLException {
        connect();
        String query = "INSERT INTO ORDER_ITEMS (ORDER_ID, FOOD_ID, QUANTITY, PRICE) " +
                "VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, orderItem.getOrderId());
            statement.setInt(2, orderItem.getFoodId());
            statement.setInt(3, orderItem.getQuantity());
            statement.setDouble(4, orderItem.getPrice());
            statement.executeUpdate();
        }
        disconnect();
    }
}
