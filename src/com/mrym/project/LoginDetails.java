package com.mrym.project;

public class LoginDetails {
    private String userName;
    private String userPassword;
    private int userId;
    public LoginDetails(int userId, String userName, String userPassword) {
        this.userId=userId;
        this.userName = userName;
        this.userPassword = userPassword;
    }
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }
}
