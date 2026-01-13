package model;

import webserver.exception.BusinessException;

public class User {
    private Long id;
    private String password;
    private String name;
    private String email;

    public User(String password, String name, String email) {
        if (password.isBlank() || name.isBlank() || email.isBlank()) {
            throw new BusinessException();
        }
        if (password == null || name == null || email == null) {
            throw new BusinessException();
        }
        this.password = password;
        this.name = name;
        this.email = email;
    }

    public Long getId() {
        return id;
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

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "User [userId=" + id + ", password=" + password + ", name=" + name + ", email=" + email + "]";
    }
}
