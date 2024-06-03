package com.pcroom.pcproject.model.dao;

import com.pcroom.pcproject.model.UserItem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDao {
    private static final String URL = "jdbc:oracle:thin:@localhost:1521:xe";
    private static final String USER = "pcroom";
    private static final String PASSWORD = "pcroom";

    public List<UserItem> loadUsersFromDatabase() {
        List<UserItem> userList = new ArrayList<>();
        String query = "SELECT * FROM users";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("ID");
                String userId = resultSet.getString("USER_ID");
                String name = resultSet.getString("NAME");
                int age = resultSet.getInt("AGE");
                String address = resultSet.getString("ADDRESS");
                String phoneNumber = resultSet.getString("PHONENUMBER");
                String email = resultSet.getString("EMAIL");
                String password = resultSet.getString("PASSWORD");
                UserItem userItem = new UserItem(id, userId, name, age, address, phoneNumber, email, password);
                userList.add(userItem);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userList;
    }

    public boolean authenticateUser(String nickname, String password) {
        String query = "SELECT * FROM users WHERE nickname = ? AND password = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, nickname);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next(); // 결과가 존재하면 true 반환
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
