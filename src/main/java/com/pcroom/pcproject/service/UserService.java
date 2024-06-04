package com.pcroom.pcproject.service;

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
    public void addUser(UserDto user) {
        allUsers.add(user);
        userDao.addUserToDatabase(user);
    }

    // 사용자 삭제
    public void removeUser(UserDto user) {
        allUsers.remove(user);
        userDao.removeUserFromDatabase(user);
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
}
