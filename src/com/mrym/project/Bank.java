package com.mrym.project;

public interface Bank {
    boolean withdraw(LoginDetails details, double amountWithdraw) throws Exception;
    boolean deposit(LoginDetails details, double amountDeposit) throws Exception;
    boolean transfer(LoginDetails details, double amountTransfer) throws Exception;
    void overdraftProtection(LoginDetails loginDetails, double amountWithdraw, String choice) throws Exception;
    void showBalance();
}
