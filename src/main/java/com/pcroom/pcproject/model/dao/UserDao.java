package com.pcroom.pcproject.model.dao;

import com.pcroom.pcproject.model.dto.UserDto;

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

    public List<UserDto> loadUsersFromDatabase() {
        List<UserDto> userList = new ArrayList<>();
        String query = "SELECT * FROM users";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("ID");
                String nickname = resultSet.getString("USER_ID");
                String name = resultSet.getString("NAME");
                int age = resultSet.getInt("AGE");
                String address = resultSet.getString("ADDRESS");
                String phoneNumber = resultSet.getString("PHONENUMBER");
                String email = resultSet.getString("EMAIL");
                String password = resultSet.getString("PASSWORD");
                UserDto userDto = new UserDto(id, nickname, name, age, address, phoneNumber, email, password);
                userList.add(userDto);
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

    public void addUserToDatabase(UserDto user) {
        String query = "INSERT INTO users (USER_ID, NAME, AGE, ADDRESS, PHONENUMBER, EMAIL, PASSWORD) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, user.getNickname());
            preparedStatement.setString(2, user.getName());
            preparedStatement.setInt(3, user.getAge());
            preparedStatement.setString(4, user.getAddress());
            preparedStatement.setString(5, user.getPhonenumber());
            preparedStatement.setString(6, user.getEmail());
            preparedStatement.setString(7, user.getPassword());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeUserFromDatabase(UserDto user) {
        String query = "DELETE FROM users WHERE ID = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, user.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateUserInDatabase(UserDto user) {
        String query = "UPDATE users SET USER_ID = ?, NAME = ?, AGE = ?, ADDRESS = ?, PHONENUMBER = ?, EMAIL = ?, PASSWORD = ? WHERE ID = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, user.getNickname());
            preparedStatement.setString(2, user.getName());
            preparedStatement.setInt(3, user.getAge());
            preparedStatement.setString(4, user.getAddress());
            preparedStatement.setString(5, user.getPhonenumber());
            preparedStatement.setString(6, user.getEmail());
            preparedStatement.setString(7, user.getPassword());
            preparedStatement.setInt(8, user.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
