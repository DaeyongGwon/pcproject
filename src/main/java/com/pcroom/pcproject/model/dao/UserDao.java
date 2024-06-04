package com.pcroom.pcproject.model.dao;

import com.pcroom.pcproject.model.dto.UserDto;

import java.sql.*;
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
                String nickname = resultSet.getString("NICKNAME");
                String name = resultSet.getString("NAME");
                Date birthday = resultSet.getDate("BIRTHDAY");
                String address = resultSet.getString("ADDRESS");
                String phoneNumber = resultSet.getString("PHONENUMBER");
                String email = resultSet.getString("EMAIL");
                String password = resultSet.getString("PASSWORD");
                UserDto userDto = new UserDto(nickname, name, birthday, address, phoneNumber, email, password);
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
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void addUserToDatabase(UserDto user) {
        String query = "INSERT INTO users (NICKNAME, NAME, BIRTHDAY, ADDRESS, PHONENUMBER, EMAIL, PASSWORD) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, user.getNickname());
            preparedStatement.setString(2, user.getName());
            System.out.println(user.getBirthday().toString());
            preparedStatement.setString(3, user.getBirthday().toString());
            preparedStatement.setString(4, user.getAddress());
            preparedStatement.setString(5, user.getPhonenumber());
            preparedStatement.setString(6, user.getEmail());
            preparedStatement.setString(7, user.getPassword());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void updateUserInDatabase(UserDto user) {
        String query = "UPDATE users SET NICKNAME = ?, NAME = ?, BIRTHDAY = ?, ADDRESS = ?, PHONENUMBER = ?, EMAIL = ?, PASSWORD = ? WHERE ID = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, user.getNickname());
            preparedStatement.setString(2, user.getName());
            preparedStatement.setDate(3, user.getBirthday());
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
