package model;

import webserver.exception.BusinessException;

public class User {
    private String userId;
    private Long id;
    private String password;
    private String name;
    private String email;

    public User(String userId, String password, String name, String email) {
        if(userId.isBlank() || password.isBlank() || name.isBlank() || email.isBlank()){
            throw new BusinessException();
        }
        if(userId == null || password == null|| name == null || email == null){
            throw new BusinessException();
        }
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public void setId(Long id){
        this.id = id;
    }

    @Override
    public String toString() {
        return "User [userId=" + userId + ", password=" + password + ", name=" + name + ", email=" + email + "]";
    }
}
