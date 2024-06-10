package com.pcroom.pcproject.model.dao;

import com.pcroom.pcproject.model.dto.UserDto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static java.sql.DriverManager.getConnection;

public class UserDao {
    private static final String URL = "jdbc:oracle:thin:@localhost:1521:xe";
    private static final String USER = "pcroom";
    private static final String PASSWORD = "pcroom";

    public List<UserDto> loadUsersFromDatabase() {
        List<UserDto> userList = new ArrayList<>();
        String query = "SELECT * FROM users";
        try (Connection connection = getConnection(URL, USER, PASSWORD);
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
        try (Connection connection = getConnection(URL, USER, PASSWORD);
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
        try (Connection connection = getConnection(URL, USER, PASSWORD);
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
        try (Connection connection = getConnection(URL, USER, PASSWORD);
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
    // 토큰을 이용하여 사용자 정보를 가져오는 메서드
    public UserDto getUserByToken(String token) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        UserDto user = null;

        try {
            conn = getConnection(URL, USER, PASSWORD); // DB 연결 가져오기
            // SQL 문장 작성
            String sql = "SELECT * FROM USERS WHERE TOKEN = ?";
            stmt = conn.prepareStatement(sql);

            // SQL 문장의 매개변수 설정
            stmt.setString(1, token);

            // SQL 문장 실행
            rs = stmt.executeQuery();

            // 결과 처리
            if (rs.next()) {
                String nickname = rs.getString("NICKNAME");
                String name = rs.getString("NAME");
                Date birthday = rs.getDate("BIRTHDAY");
                String address = rs.getString("ADDRESS");
                String phoneNumber = rs.getString("PHONENUMBER");
                String email = rs.getString("EMAIL");

                // UserDto 객체 생성
                user = new UserDto(nickname, name, birthday, address, phoneNumber, email, ""); // 여기에 빈 문자열 추가
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 리소스 해제
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return user;
    }
    // 사용자의 닉네임으로 ID 값을 가져오는 메서드
    public static int getUserIdByNickname(String nickname) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int userId = -1; // 기본값으로 -1을 설정하여 오류 발생 시 인식할 수 있도록 함

        try {
            conn = getConnection(URL, USER, PASSWORD); // DB 연결 가져오기
            // SQL 문장 작성
            String sql = "SELECT ID FROM USERS WHERE NICKNAME = ?";
            stmt = conn.prepareStatement(sql);

            // SQL 문장의 매개변수 설정
            stmt.setString(1, nickname);

            // SQL 문장 실행
            rs = stmt.executeQuery();

            // 결과 처리
            if (rs.next()) {
                userId = rs.getInt("ID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 리소스 해제
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return userId;
    }

    // DB에 있는 토큰을 가져오는 메서드
    public String getTokenFromUser(String username) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection(URL, USER, PASSWORD); // DB 연결 가져오기
            // SQL 문장 작성
            String sql = "SELECT TOKEN FROM USERS WHERE NICKNAME = ?";
            stmt = conn.prepareStatement(sql);

            // SQL 문장의 매개변수 설정
            stmt.setString(1, username);

            // SQL 문장 실행
            rs = stmt.executeQuery();

            // 결과 처리
            if (rs.next()) {
                return rs.getString("TOKEN");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 토큰을 사용자에게 저장하는 메서드
    public void saveTokenToUser(String username, String token) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnection(URL, USER, PASSWORD); // DB 연결 가져오기
            // SQL 문장 작성
            String sql = "UPDATE USERS SET TOKEN = ? WHERE NICKNAME = ?";
            stmt = conn.prepareStatement(sql);

            // SQL 문장의 매개변수 설정
            stmt.setString(1, token);
            stmt.setString(2, username);

            // SQL 문장 실행
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 리소스 해제
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    // 사용자의 시작 시간을 가져오는 메서드
    public String getUserStartTime(String username) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String startTime = null;

        try {
            conn = getConnection(URL, USER, PASSWORD); // DB 연결 가져오기
            // SQL 문장 작성
            String sql = "SELECT START_TIME FROM TIMES WHERE ID = (SELECT ID FROM USERS WHERE NICKNAME = ?)";
            stmt = conn.prepareStatement(sql);

            // SQL 문장의 매개변수 설정
            stmt.setString(1, username);

            // SQL 문장 실행
            rs = stmt.executeQuery();

            // 결과 처리
            if (rs.next()) {
                startTime = rs.getString("START_TIME");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 리소스 해제
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return startTime;
    }


}
