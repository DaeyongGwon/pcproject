package com.pcroom.pcproject.model.dto;

public class UserDto {
    private int id;
    private String nickname;
    private String name;
    private int age;
    private String address;
    private String phonenumber;
    private String email;
    private String password;

    public UserDto(int id, String nickname, String name, int age, String address, String phonenumber, String email, String password) {
        this.id = id;
        this.nickname = nickname;
        this.name = name;
        this.age = age;
        this.address = address;
        this.phonenumber = phonenumber;
        this.email = email;
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}