package com.pcroom.pcproject.model.dao;

import com.pcroom.pcproject.model.dto.UserDto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDao {
    private static final String URL = "jdbc:oracle:thin:@localhost:1521:xe";
    private static final String USER = "pcroom";
    private static final String PASSWORD = "pcroom";
    private Connection connection;

    // DB 연결 메서드, 권대용 작성
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
    // 데이터베이스에서 모든 사용자 정보를 가져옴, 권대용 작성
    public List<UserDto> loadUsersFromDatabase() {
        List<UserDto> userList = new ArrayList<>();
        String query = "SELECT * FROM users";
        try {
            connect(); // DB 연결
            PreparedStatement preparedStatement = connection.prepareStatement(query);
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

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            disconnect(); // DB 연결 해제
        }
        return userList;
    }
    // 사용자 인증을 수행, 권대용 작성
    public boolean authenticateUser(String nickname, String password) {
        String query = "SELECT * FROM users WHERE nickname = ? AND password = ?";
        try {
            connect(); // DB 연결
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, nickname);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            boolean result = resultSet.next();

            resultSet.close();
            preparedStatement.close();

            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            disconnect(); // DB 연결 해제
        }
    }
    // 데이터베이스에 사용자를 추가, 권대용 작성
    public void addUserToDatabase(UserDto user) {
        String query = "INSERT INTO users (NICKNAME, NAME, BIRTHDAY, ADDRESS, PHONENUMBER, EMAIL, PASSWORD) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            connect(); // DB 연결
            PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, user.getNickname());
            preparedStatement.setString(2, user.getName());
            preparedStatement.setDate(3, user.getBirthday());
            preparedStatement.setString(4, user.getAddress());
            preparedStatement.setString(5, user.getPhonenumber());
            preparedStatement.setString(6, user.getEmail());
            preparedStatement.setString(7, user.getPassword());
            preparedStatement.executeUpdate();

            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            disconnect(); // DB 연결 해제
        }
    }
    // 데이터베이스에 있는 사용자 정보를 업데이트, 권대용 작성
    public void updateUserInDatabase(UserDto user) {
        String query = "UPDATE users SET NICKNAME = ?, NAME = ?, BIRTHDAY = ?, ADDRESS = ?, PHONENUMBER = ?, EMAIL = ?, PASSWORD = ? WHERE ID = ?";
        try {
            connect(); // DB 연결
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, user.getNickname());
            preparedStatement.setString(2, user.getName());
            preparedStatement.setDate(3, user.getBirthday());
            preparedStatement.setString(4, user.getAddress());
            preparedStatement.setString(5, user.getPhonenumber());
            preparedStatement.setString(6, user.getEmail());
            preparedStatement.setString(7, user.getPassword());
            preparedStatement.setInt(8, user.getId());
            preparedStatement.executeUpdate();

            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            disconnect(); // DB 연결 해제
        }
    }

    // 토큰을 이용하여 사용자 정보를 가져오는 메서드, 권대용 작성
    public UserDto getUserByToken(String token) {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        UserDto user = null;

        try {
            connect(); // DB 연결
            // SQL 문장 작성
            String sql = "SELECT * FROM USERS WHERE TOKEN = ?";
            stmt = connection.prepareStatement(sql);

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
            disconnect(); // DB 연결 해제
        }
        return user;
    }

    // 사용자의 닉네임으로 ID 값을 가져오는 메서드, 권대용 작성
    public int getUserIdByNickname(String nickname) {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int userId = -1; // 기본값으로 -1을 설정하여 오류 발생 시 인식할 수 있도록 함

        try {
            connect(); // DB 연결
            // SQL 문장 작성
            String sql = "SELECT ID FROM USERS WHERE NICKNAME = ?";
            stmt = connection.prepareStatement(sql);

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
            disconnect(); // DB 연결 해제
        }
        return userId;
    }

    // DB에 있는 토큰을 가져오는 메서드, 권대용 작성
    public String getTokenFromUser(String username) {
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            connect(); // DB 연결
            // SQL 문장 작성
            String sql = "SELECT TOKEN FROM USERS WHERE NICKNAME = ?";
            stmt = connection.prepareStatement(sql);

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
            disconnect(); // DB 연결 해제
        }
        return null;
    }

    // 토큰을 사용자에게 저장하는 메서드, 권대용 작성
    public void saveTokenToUser(String username, String token) {
        PreparedStatement stmt = null;

        try {
            connect(); // DB 연결
            // SQL 문장 작성
            String sql = "UPDATE USERS SET TOKEN = ? WHERE NICKNAME = ?";
            stmt = connection.prepareStatement(sql);

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
            disconnect(); // DB 연결 해제
        }
    }

}
