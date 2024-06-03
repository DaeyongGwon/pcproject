package com.pcroom.pcproject.model;

import com.pcroom.pcproject.model.dao.UserDao;

import java.util.ArrayList;
import java.util.List;

public class UserModel {
    private final List<UserItem> allUsers = new ArrayList<>(); // 모든 사용자를 저장할 리스트
    private final UserDao userDao;

    public UserModel() {
        this.userDao = new UserDao();
    }

    // 데이터베이스에서 사용자 데이터를 로드하고 리스트에 추가
    public void loadUserData() {
        List<UserItem> users = userDao.loadUsersFromDatabase();
        allUsers.addAll(users);
    }

    // 모든 사용자 반환
    public List<UserItem> getAllUsers() {
        return new ArrayList<>(allUsers);
    }

//    // 사용자 추가
//    public void addUser(UserItem user) {
//        allUsers.add(user);
//        userDao.addUserToDatabase(user);
//    }
//
//    // 사용자 삭제
//    public void removeUser(UserItem user) {
//        allUsers.remove(user);
//        userDao.removeUserFromDatabase(user);
//    }

//    // 사용자 업데이트
//    public void updateUser(UserItem user) {
//        userDao.updateUserInDatabase(user);
//    }

    // 사용자 인증
    public boolean authenticateUser(String userId, String password) {
        return userDao.authenticateUser(userId, password);
    }
}
