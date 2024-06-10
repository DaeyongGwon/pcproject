package com.pcroom.pcproject.model.dao;

import com.pcroom.pcproject.model.dto.TimeDto;
import java.sql.*;

public class TimeDao {
    private static final String URL = "jdbc:oracle:thin:@localhost:1521:xe";
    private static final String USER = "pcroom";
    private static final String PASSWORD = "pcroom";

    // 시간을 조회하는 메서드
    public TimeDto getTimeByUserId(int userId) {
        TimeDto timeDto = null;
        String query = "SELECT * FROM TIMES WHERE ID = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    timeDto = new TimeDto();
                    timeDto.setId(rs.getInt("ID"));
                    timeDto.setRemainingTime(rs.getInt("REMAINING_TIME"));
                    timeDto.setLastChecked(rs.getTimestamp("LAST_CHECKED"));
                    timeDto.setStartTime(rs.getTimestamp("START_TIME"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return timeDto;
    }

    // 시간을 삽입하는 메서드
    public void insertTime(TimeDto time) {
        String query = "INSERT INTO TIMES (ID, REMAINING_TIME, LAST_CHECKED, START_TIME) VALUES (?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, time.getId());
            stmt.setInt(2, time.getRemainingTime());
            stmt.setTimestamp(3, time.getLastChecked());
            stmt.setTimestamp(4, time.getStartTime());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 시간을 업데이트하는 메서드
    public void updateTime(TimeDto time) {
        String query = "UPDATE TIMES SET REMAINING_TIME = ?, LAST_CHECKED = ?, START_TIME = ? WHERE ID = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, time.getRemainingTime());
            stmt.setTimestamp(2, time.getLastChecked());
            stmt.setTimestamp(3, time.getStartTime());
            stmt.setInt(4, time.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
