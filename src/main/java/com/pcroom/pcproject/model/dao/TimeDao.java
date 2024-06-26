package com.pcroom.pcproject.model.dao;

import com.pcroom.pcproject.model.dto.TimeDto;
import java.sql.*;

public class TimeDao {
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

    // 시간을 조회하는 메서드, 권대용 작성
    public TimeDto getTimeByUserId(int userId) {
        TimeDto timeDto = null;
        String query = "SELECT * FROM TIMES WHERE ID = ?";

        try {
            connect(); // DB 연결
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                timeDto = new TimeDto();
                timeDto.setId(rs.getInt("ID"));
                timeDto.setRemainingTime(rs.getInt("REMAINING_TIME"));
                timeDto.setLastChecked(rs.getTimestamp("LAST_CHECKED"));
                timeDto.setStartTime(rs.getTimestamp("START_TIME"));
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            disconnect(); // DB 연결 해제
        }

        return timeDto;
    }

    // 시간을 삽입하는 메서드, 권대용 작성
    public void insertTime(TimeDto time) {
        String query = "INSERT INTO TIMES (ID, REMAINING_TIME, LAST_CHECKED, START_TIME) VALUES (?, ?, ?, ?)";

        try {
            connect(); // DB 연결
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, time.getId());
            stmt.setInt(2, time.getRemainingTime());
            stmt.setTimestamp(3, time.getLastChecked());
            stmt.setTimestamp(4, time.getStartTime());

            stmt.executeUpdate();

            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            disconnect(); // DB 연결 해제
        }
    }

    // 시작 시간을 업데이트하는 메서드, 권대용 작성
    public void updateStartTime(int userId, Timestamp startTime) {
        String query = "UPDATE TIMES SET START_TIME = ? WHERE ID = ?";

        try {
            connect(); // DB 연결
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setTimestamp(1, startTime);
            stmt.setInt(2, userId);

            stmt.executeUpdate();

            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            disconnect(); // DB 연결 해제
        }
    }

    // 종료 시간을 업데이트하는 메서드, 권대용 작성
    public void updateEndTime(int userId, Timestamp endTime) {
        String query = "UPDATE TIMES SET LAST_CHECKED = ? WHERE ID = ?";

        try {
            connect(); // DB 연결
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setTimestamp(1, endTime);
            stmt.setInt(2, userId);
            stmt.executeUpdate();

            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            disconnect(); // DB 연결 해제
        }
    }

    // 시간을 업데이트하는 메서드, 권대용 작성
    public void updateTime(TimeDto time) {
        String query = "UPDATE TIMES SET REMAINING_TIME = ? WHERE ID = ?";

        try {
            connect(); // DB 연결
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, time.getRemainingTime());
            stmt.setInt(2, time.getId());

            stmt.executeUpdate();

            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            disconnect(); // DB 연결 해제
        }
    }

    // 사용자의 잔여 시간을 1분씩 감소시키는 메서드, 권대용 작성
    public static void decrementRemainingTime(int userId) throws SQLException {
        String sql = "UPDATE TIMES SET REMAINING_TIME = REMAINING_TIME - 1 WHERE ID = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.executeUpdate();
        }
    }

    // 사용자의 시작 시간을 가져오는 메서드, 권대용 작성
    public String getUserStartTime(String username) {
        String startTime = null;
        String sql = "SELECT START_TIME FROM TIMES WHERE ID = (SELECT ID FROM USERS WHERE NICKNAME = ?)";

        try {
            connect(); // DB 연결
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                startTime = rs.getString("START_TIME");
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            disconnect(); // DB 연결 해제
        }

        return startTime;
    }

    // 사용자의 남은 시간을 가져오는 메서드, 권대용 작성
    public int getUserRemainingTime(String nickname) {
        int remainingTime = 0;
        String sql = "SELECT REMAINING_TIME FROM TIMES WHERE ID = (SELECT ID FROM USERS WHERE NICKNAME = ?)";

        try {
            connect(); // DB 연결
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, nickname);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                remainingTime = rs.getInt("REMAINING_TIME");
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            disconnect(); // DB 연결 해제
        }

        return remainingTime;
    }
}
