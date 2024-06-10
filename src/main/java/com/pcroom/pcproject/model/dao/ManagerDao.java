package com.pcroom.pcproject.model.dao;

import com.pcroom.pcproject.model.dto.ManagerDto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ManagerDao {
    private static final String URL = "jdbc:oracle:thin:@localhost:1521:xe";
    private static final String USER = "pcroom";
    private static final String PASSWORD = "pcroom";

    // 새로운 매니저 추가
    public void insertManager(ManagerDto manager) {
        String sql = "INSERT INTO MANAGERS (MANAGER_ID, M_NICKNAME, NAME, PHONE_NUMBER, EMAIL, PASSWORD) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, manager.getManagerId());
            pstmt.setString(2, manager.getNickname());
            pstmt.setString(3, manager.getName());
            pstmt.setString(4, manager.getPhoneNumber());
            pstmt.setString(5, manager.getEmail());
            pstmt.setString(6, manager.getPassword());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 매니저 아이디로 조회
    public ManagerDto getManagerById(int managerId) {
        String sql = "SELECT * FROM MANAGERS WHERE MANAGER_ID = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, managerId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                ManagerDto manager = new ManagerDto();
                manager.setManagerId(rs.getInt("MANAGER_ID"));
                manager.setNickname(rs.getString("M_NICKNAME"));
                manager.setName(rs.getString("NAME"));
                manager.setPhoneNumber(rs.getString("PHONE_NUMBER"));
                manager.setEmail(rs.getString("EMAIL"));
                manager.setPassword(rs.getString("PASSWORD"));
                return manager;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 모든 매니저 조회
    public List<ManagerDto> getAllManagers() {
        List<ManagerDto> managers = new ArrayList<>();
        String sql = "SELECT * FROM MANAGERS";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                ManagerDto manager = new ManagerDto();
                manager.setManagerId(rs.getInt("MANAGER_ID"));
                manager.setNickname(rs.getString("M_NICKNAME"));
                manager.setName(rs.getString("NAME"));
                manager.setPhoneNumber(rs.getString("PHONE_NUMBER"));
                manager.setEmail(rs.getString("EMAIL"));
                manager.setPassword(rs.getString("PASSWORD"));
                managers.add(manager);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return managers;
    }

    // 매니저 업데이트
    public void updateManager(ManagerDto manager) {
        String sql = "UPDATE MANAGERS SET M_NICKNAME = ?, NAME = ?, PHONE_NUMBER = ?, EMAIL = ?, PASSWORD = ? WHERE MANAGER_ID = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, manager.getNickname());
            pstmt.setString(2, manager.getName());
            pstmt.setString(3, manager.getPhoneNumber());
            pstmt.setString(4, manager.getEmail());
            pstmt.setString(5, manager.getPassword());
            pstmt.setInt(6, manager.getManagerId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 매니저 삭제
    public void deleteManager(int managerId) {
        String sql = "DELETE FROM MANAGERS WHERE MANAGER_ID = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, managerId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
