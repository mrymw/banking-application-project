package com.mrym.project;
public class UserDetails {
    private int userID;
    private String userFirstName;
    private String userLastName;
    private String userPassword;
    private double checkingAccountBalance;
    private double savingAccountBalance;
    private String checkingIBAN;
    private String savingIBAN;
    private TypesOfCards checkingCards;
    private TypesOfCards savingCards;
    private String phoneNumber;

    public UserDetails(int userID, String userFirstName, String userLastName, String userPassword, double checkingAccountBalance, double savingAccountBalance, String checkingIBAN, String savingIBAN, TypesOfCards checkingCards, TypesOfCards savingCards, String phoneNumber) {
        this.userID = userID;
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
        this.userPassword = userPassword;
        this.checkingAccountBalance = checkingAccountBalance;
        this.savingAccountBalance = savingAccountBalance;
        this.checkingIBAN = checkingIBAN;
        this.savingIBAN = savingIBAN;
        this.checkingCards = checkingCards;
        this.savingCards = savingCards;
        this.phoneNumber = phoneNumber;
    }

    public UserDetails() {
    }

    public String getCheckingIBAN() {
        return checkingIBAN;
    }

    public void setCheckingIBAN(String checkingIBAN) {
        this.checkingIBAN = checkingIBAN;
    }

    public String getSavingIBAN() {
        return savingIBAN;
    }

    public void setSavingIBAN(String savingIBAN) {
        this.savingIBAN = savingIBAN;
    }

    public TypesOfCards getCheckingCards() {
        return checkingCards;
    }

    public void setCheckingCards(TypesOfCards checkingCards) {
        this.checkingCards = checkingCards;
    }

    public TypesOfCards getSavingCards() {
        return savingCards;
    }

    public void setSavingCards(TypesOfCards savingCards) {
        this.savingCards = savingCards;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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
