package com.mrym.project;
public class UserDetails {
    private int userID;
    private String userFirstName;
    private String userLastName;
    private String userPassword;
    private double checkingAccountBalance;
    private double savingAccountBalance;

    public UserDetails(int userID, String userFirstName, String userLastName,String userPassword, double checkingAccountBalance, double savingAccountBalance) {
        this.userID = userID;
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
        this.userPassword = userPassword;
        this.checkingAccountBalance = checkingAccountBalance;
        this.savingAccountBalance = savingAccountBalance;
    }

    public UserDetails() {

    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }

    public double getCheckingAccountBalance() {
        return checkingAccountBalance;
    }

    public void setCheckingAccountBalance(double checkingAccountBalance) {
        this.checkingAccountBalance = checkingAccountBalance;
    }

    public double getSavingAccountBalance() {
        return savingAccountBalance;
    }

    public void setSavingAccountBalance(double savingAccountBalance) {
        this.savingAccountBalance = savingAccountBalance;
    }

}
