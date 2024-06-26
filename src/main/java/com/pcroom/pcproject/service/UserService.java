package com.pcroom.pcproject.service;

import com.pcroom.pcproject.model.dao.TimeDao;
import com.pcroom.pcproject.model.dao.UserDao;
import com.pcroom.pcproject.model.dto.UserDto;
import java.util.ArrayList;
import java.util.List;

public class UserService {
    private final List<UserDto> allUsers = new ArrayList<>(); // 모든 사용자를 저장할 리스트
    private final UserDao userDao;

    public UserService() {
        this.userDao = new UserDao();
    }

    // 데이터베이스에서 사용자 데이터를 로드하고 리스트에 추가
    public void loadUserData() {
        List<UserDto> users = userDao.loadUsersFromDatabase();
        allUsers.addAll(users);
    }

    // 모든 사용자 반환
    public List<UserDto> getAllUsers() {
        return new ArrayList<>(allUsers);
    }

    // 사용자 추가
    public boolean addUser(UserDto user) {
        try {
            // 사용자를 데이터베이스에 추가하는 코드
            userDao.addUserToDatabase(user);
            // 사용자 추가가 성공하면 true 반환
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            // 사용자 추가가 실패하면 false 반환
            return false;
        }
    }

    // 사용자 삭제
    public void removeUser(UserDto user) {
        allUsers.remove(user);
//        userDao.removeUserFromDatabase(user);
    }

    // 사용자 업데이트
    public void updateUser(UserDto user) {
        userDao.updateUserInDatabase(user);
    }

    // 사용자 인증
    public boolean authenticateUser(String userId, String password) {
        boolean result = userDao.authenticateUser(userId, password);
        return result;
    }

    public void saveTokenToUser(String username, String token) {
        userDao.saveTokenToUser(username, token);
    }

    public UserDto getUserByToken(String token) {
        return userDao.getUserByToken(token);
    }

    public String getUserStartTime(String username) {
        TimeDao timeDao = new TimeDao();
        // UserDao의 메서드를 활용하여 사용자의 Start_Time 값을 가져옵니다.
        return timeDao.getUserStartTime(username);
    }
}
