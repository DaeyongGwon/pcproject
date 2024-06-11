package com.pcroom.pcproject.model.dao;

import com.pcroom.pcproject.model.dto.SeatDto;
import com.pcroom.pcproject.model.dto.UserDto;

import java.sql.*;



public class SeatAssignmentDAO {
    private static final String URL = "jdbc:oracle:thin:@localhost:1521:xe";
    private static final String USER = "pcroom";
    private static final String PASSWORD = "pcroom";

    // 좌석 할당 정보를 추가하는 메서드
    public static void assignSeat(int userId, int seatId, Timestamp startTime) throws SQLException {
        String sql = "INSERT INTO SEAT_ASSIGNMENTS (USER_ID, SEAT_ID, LOGIN_TIME) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, seatId);
            stmt.setTimestamp(3, startTime);
            stmt.executeUpdate();
        }
    }
    // 로그아웃 시 로그아웃 시간을 업데이트 하는 메서드
    public static void updateLogoutTime(int userId, Timestamp loginTime, Timestamp logoutTime) throws SQLException {
        String sql = "UPDATE SEAT_ASSIGNMENTS SET LOGOUT_TIME = ? WHERE USER_ID = ? AND LOGIN_TIME = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setTimestamp(1, logoutTime);
            stmt.setInt(2, userId);
            stmt.setTimestamp(3, loginTime);
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
    // 좌석 번호를 기준으로 사용자 정보를 가져오는 메서드
    public UserDto getUserBySeatNumber(int seatId) {
        UserDto user = null;
        String sql = "SELECT u.* FROM USERS u INNER JOIN SEAT_ASSIGNMENTS sa ON u.ID = sa.USER_ID WHERE sa.SEAT_ID = ?\n";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, seatId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String nickname = rs.getString("NICKNAME");
                    String name = rs.getString("NAME");
                    Date birthday = rs.getDate("BIRTHDAY");
                    String address = rs.getString("ADDRESS");
                    String phoneNumber = rs.getString("PHONENUMBER");
                    String email = rs.getString("EMAIL");
                    String password = rs.getString("PASSWORD");
                    user = new UserDto(nickname, name, birthday, address, phoneNumber, email, password);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }
}
