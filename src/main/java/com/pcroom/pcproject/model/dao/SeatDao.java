package com.pcroom.pcproject.model.dao;

import com.pcroom.pcproject.model.dto.SeatDto;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SeatDao {
    private static final String URL = "jdbc:oracle:thin:@localhost:1521:xe";
    private static final String USER = "pcroom";
    private static final String PASSWORD = "pcroom";
    private Connection connection;


    // DB 연결 메서드
    private void connect() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // DB 연결 해제 메서드
    private void disconnect() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static int getSeatIdByUserId(int userId) {
        int seatId = -1;
        String query = "SELECT SEAT_ID FROM SEATS WHERE  = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                seatId = rs.getInt("SEAT_ID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return seatId;
    }

    // DB에서 모든 좌석 정보를 가져오는 메서드
    public List<SeatDto> getAllSeats() {
        List<SeatDto> seatList = new ArrayList<>();
        connect(); // DB 연결
        String query = "SELECT * FROM SEATS";
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                SeatDto seat = new SeatDto(rs.getInt("SEAT_ID"), rs.getInt("SEAT_NUMBER"), rs.getInt("ACTIVE"));
                seatList.add(seat);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            disconnect(); // DB 연결 해제
        }
        return seatList;
    }
    // DB에서 좌석 번호로 좌석 정보를 가져오는 메서드
    public SeatDto getSeatByNumber(int seatNumber) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        SeatDto seat = null;

        try {
            connect(); // DB 연결
            String sql = "SELECT * FROM seats WHERE seat_number = ?";
            pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, seatNumber);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                seat = new SeatDto(rs.getInt("SEAT_ID"), rs.getInt("SEAT_NUMBER"), rs.getInt("ACTIVE"));
                // 필요한 다른 필드들도 설정할 수 있습니다.
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }
            disconnect(); // DB 연결 해제
        }

        return seat;
    }

    // 좌석 상태를 업데이트하는 메서드
    public void updateSeatStatus(int seatNumber, int activeStatus) {
        connect(); // DB 연결
        String query = "UPDATE SEATS SET ACTIVE = ? WHERE SEAT_NUMBER = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, activeStatus);
            stmt.setInt(2, seatNumber);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            disconnect(); // DB 연결 해제
        }
    }

}
