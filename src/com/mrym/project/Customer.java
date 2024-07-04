package com.mrym.project;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Customer extends UserDetails implements Bank {
    boolean isPositive;
    protected ArrayList<String> transactionHistory = new ArrayList<>();
    public Customer (){super();}
    @Override
    public boolean withdraw(LoginDetails details, double amountWithdraw) throws Exception {
        Scanner input = new Scanner(System.in);
        Accounts.checkingAccount(details);
        Accounts.savingAccount(details);
        double checkingBalance = Accounts.checkingBalance;
        double savingBalance = Accounts.savingBalance;
        System.out.println(checkingBalance);
        if (amountWithdraw < 0) {
            System.out.println("ERROR NEGATIVE NUMBER!");
            isPositive = false;
        }
        System.out.println("would you like to withdraw money from checking or saving account?");
        String answer = input.next().toLowerCase();
        if (answer.equals("checking")) {
            double balanceWithdrawChecking;
            if (checkingBalance >= amountWithdraw){
                balanceWithdrawChecking = checkingBalance - amountWithdraw;
                transactionHistory.add("amount withdrawn from checking account: ");
                transactionHistory.add(Arrays.toString(new double[] {balanceWithdrawChecking}));
                System.out.println("amount successfully withdrawn: \nbalance before: " + checkingBalance +"\nbalance after:  "+ balanceWithdrawChecking);
            }
        } else if (answer.equals("saving")) {
            double balanceWithdrawSaving;
            if (savingBalance >= amountWithdraw) {
                balanceWithdrawSaving = savingBalance - amountWithdraw;
                transactionHistory.add("amount withdrawn from saving account: ");
                transactionHistory.add(Arrays.toString(new double[] {balanceWithdrawSaving}));
                System.out.println("amount successfully withdrawn: \nbalance before: " + savingBalance +"\nbalance after:  "+ balanceWithdrawSaving);
            }
        }
        return isPositive;
    }

    @Override
    public boolean deposit(LoginDetails details, double amountDeposit) throws Exception{
        Scanner input = new Scanner(System.in);
        Accounts.checkingAccount(details);
        Accounts.savingAccount(details);
        double checkingBalance = Accounts.checkingBalance;
        double savingBalance = Accounts.savingBalance;
        System.out.println(checkingBalance);
        if (amountDeposit < 0) {
            System.out.println("ERROR NEGATIVE NUMBER!");
            isPositive = false;
        }
        System.out.println("would you like to withdraw money from checking or saving account?");
        String answer = input.next().toLowerCase();
        if (answer.equals("checking")) {
            double balanceDepositChecking;
            if (checkingBalance >= amountDeposit){
                balanceDepositChecking = checkingBalance + amountDeposit;
                transactionHistory.add("amount deposit to checking account: ");
                transactionHistory.add(Arrays.toString(new double[] {balanceDepositChecking}));
                System.out.println("amount successfully deposit: \nbalance before: " + checkingBalance +"\nbalance after:  "+ balanceDepositChecking);
            }
        } else if (answer.equals("saving")) {
            double balanceDepositSaving;
            if (savingBalance >= amountDeposit) {
                balanceDepositSaving = savingBalance - amountDeposit;
                transactionHistory.add("amount deposit to saving account: ");
                transactionHistory.add(Arrays.toString(new double[] {balanceDepositSaving}));
                System.out.println("amount successfully deposit: \nbalance before: " + savingBalance +"\nbalance after:  "+ balanceDepositSaving);
            }
        }
        return isPositive;
    }

    @Override
    public boolean transfer(LoginDetails details, double amountTransfer) throws Exception {
        return false;
    }

    @Override
    public void showBalance() {

    }

}
