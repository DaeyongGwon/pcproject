package com.pcroom.pcproject.model.dao;

import java.sql.*;


public class SeatAssignmentDAO {
    private static final String URL = "jdbc:oracle:thin:@localhost:1521:xe";
    private static final String USER = "pcroom";
    private static final String PASSWORD = "pcroom";

    // 좌석 할당 정보를 추가하는 메서드
    public static void assignSeat(int userId, int seatId) throws SQLException {
        String sql = "INSERT INTO SEAT_ASSIGNMENTS (USER_ID, SEAT_ID) VALUES (?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, seatId);
            stmt.executeUpdate();
        }
    }

    // 좌석 할당 정보를 삭제하는 메서드
    public static void unassignSeat(int userId) throws SQLException {
        String sql = "DELETE FROM SEAT_ASSIGNMENTS WHERE USER_ID = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.executeUpdate();
        }
    }

}
