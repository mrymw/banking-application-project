package com.mrym.project;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.*;

public class Banker extends UserDetails implements Bank {
    boolean isPositive;
    protected ArrayList<String> transactionHistory = new ArrayList<>();

    public Banker() {
        super();
    }

    public static void addCustomer(UserDetails details) throws Exception {
        String[] addDetails = {String.valueOf(details.getUserID()), details.getUserFirstName(), details.getUserLastName(), details.getUserPassword(), String.valueOf(details.getCheckingAccountBalance()), String.valueOf(details.getSavingAccountBalance()), "C", String.valueOf(details.getCheckingIBAN()), String.valueOf(details.getSavingIBAN()), String.valueOf(details.getCheckingCards()), String.valueOf(details.getSavingCards()), String.valueOf(details.getPhoneNumber())};
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("database.txt", true));
        StringJoiner stringJoiner = new StringJoiner(",");
        String joinDetails = "";
        for (String customer : addDetails) {
            joinDetails = String.valueOf(stringJoiner.add(customer));
        }
        bufferedWriter.write(joinDetails);
        bufferedWriter.close();
        System.out.println("Customer Successfully Added to database.txt");
    }

    @Override
    public boolean withdraw(LoginDetails details, double amountWithdraw) throws Exception {
        Scanner input = new Scanner(System.in);
        Accounts.checkingAccount(details);
        Accounts.savingAccount(details);
        double checkingBalance = Accounts.checkingBalance;
        double savingBalance = Accounts.savingBalance;
        System.out.println(checkingBalance);
        //checks if the withdrawn amount entered is greater than zero
        if (amountWithdraw < 0) {
            System.out.println("ERROR NEGATIVE NUMBER!");
            isPositive = false;
        }
        System.out.println("would you like to withdraw money from checking or saving account?");
        String answer = input.next().toLowerCase();
        if (answer.equals("checking")) {
            double balanceWithdrawChecking;
            if (checkingBalance >= amountWithdraw) {
                balanceWithdrawChecking = checkingBalance - amountWithdraw;
                transactionHistory.add("amount withdrawn from checking account: ");
                transactionHistory.add(Arrays.toString(new double[]{balanceWithdrawChecking}));
                System.out.println("amount successfully withdrawn: \nbalance before: " + checkingBalance + "\nbalance after:  " + balanceWithdrawChecking);
            } else {
                overdraftProtection(details, amountWithdraw, "checking");
            }
        } else if (answer.equals("saving")) {
            double balanceWithdrawSaving;
            if (savingBalance >= amountWithdraw) {
                balanceWithdrawSaving = savingBalance - amountWithdraw;
                transactionHistory.add("amount withdrawn from saving account: ");
                transactionHistory.add(Arrays.toString(new double[]{balanceWithdrawSaving}));
                System.out.println("amount successfully withdrawn: \nbalance before: " + savingBalance + "\nbalance after:  " + balanceWithdrawSaving);
            } else {
                overdraftProtection(details, amountWithdraw, "saving");
            }
        }
        return isPositive;
    }

    @Override
    public boolean deposit(LoginDetails details, double amountDeposit) throws Exception {
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
            if (checkingBalance >= amountDeposit) {
                balanceDepositChecking = checkingBalance + amountDeposit;
                transactionHistory.add("amount deposit to checking account: ");
                transactionHistory.add(Arrays.toString(new double[]{balanceDepositChecking}));
                System.out.println("amount successfully deposit: \nbalance before: " + checkingBalance + "\nbalance after:  " + balanceDepositChecking);
            }
        } else if (answer.equals("saving")) {
            double balanceDepositSaving;
            if (savingBalance >= amountDeposit) {
                balanceDepositSaving = savingBalance - amountDeposit;
                transactionHistory.add("amount deposit to saving account: ");
                transactionHistory.add(Arrays.toString(new double[]{balanceDepositSaving}));
                System.out.println("amount successfully deposit: \nbalance before: " + savingBalance + "\nbalance after:  " + balanceDepositSaving);
            }
        }
        return isPositive;
    }

    @Override
    public boolean transfer(LoginDetails details, double amountTransfer) throws Exception {
        List<String> transferDetails = StoredDatabase.getUserDetails(details);
        Scanner input = new Scanner(System.in);
        Accounts.checkingAccount(details);
        Accounts.savingAccount(details);
        double checkingBalance = Accounts.checkingBalance;
        double savingBalance = Accounts.savingBalance;
        System.out.println(checkingBalance);
        if (amountTransfer < 0) {
            System.out.println("ERROR NEGATIVE NUMBER!");
            isPositive = false;
        }
        System.out.println("where would you like to transfer money to?");
        System.out.println("1. own account");
        System.out.println("2. another customer");
        int answer = input.nextInt();
        switch (answer) {
            case 1:
                System.out.println("select your choice:");
                System.out.println("1. from checking to saving");
                System.out.println("2. from saving to checking");
                int choice = input.nextInt();
                if (choice == 1) {
                    double transferBalanceChecking, transferBalanceSaving;
                    transferBalanceChecking = checkingBalance - amountTransfer;
                    transferBalanceSaving = savingBalance + amountTransfer;
                    System.out.println("balance after transfering from checking to saving: \nchecking: " + transferBalanceChecking +
                            "\nsaving: " + transferBalanceSaving);
                } else if (choice == 2) {
                    double transferBalanceChecking, transferBalanceSaving;
                    transferBalanceSaving = savingBalance - amountTransfer;
                    transferBalanceChecking = checkingBalance + amountTransfer;
                    System.out.println("balance after transfering from saving to checking: " + "\nsaving: " + transferBalanceSaving + "\nchecking: " + transferBalanceChecking);
                }
                break;
            case 2:
                System.out.println("from which account would you like to transfer from?");
                System.out.println("1. checking");
                System.out.println("2. saving");
                int userTransferChoice = input.nextInt();
                if (userTransferChoice == 1) {
                    System.out.println("1. transfer through IBAN");
                    System.out.println("2. transfer through Phone Number");
                    int transferChoice = input.nextInt();
                    if (transferChoice == 1) {
                        System.out.println("enter IBAN number");
                        String ibanNum = input.next().toUpperCase();
                        //
                        List<String> getAnotherUserIban = StoredDatabase.getUserIBAN(ibanNum);
                        String checkingIBAN = getAnotherUserIban.get(6);
                        String savingIBAN = getAnotherUserIban.get(7);
                        double checking = Double.parseDouble(getAnotherUserIban.get(3));
                        double saving = Double.parseDouble(getAnotherUserIban.get(4));
                        if (ibanNum.equals(checkingIBAN)) {
                            double transferToAnother = checking + amountTransfer;
                            System.out.println("amount transferred to: " + ibanNum + " checking account");
                        } else if (ibanNum.equals(savingIBAN)) {
                            double transferToAnother = saving + amountTransfer;
                            System.out.println("amount transferred to: " + ibanNum + " saving account");
                        } else {
                            System.out.println("IBAN doesn't exist");
                        }
                        double transferChecking = Double.parseDouble(transferDetails.get(3));
                        double currentBalance = transferChecking - amountTransfer;
                        System.out.println("your current balance for checking account is: " + currentBalance);
                    } else if (transferChoice == 2) {
                        System.out.println("enter phone number");
                        String phoneNum = input.next().toUpperCase();
                        //
                        List<String> getAnotherUserPhone = StoredDatabase.getUserPhoneNumber(phoneNum);
                        double checking = Double.parseDouble(getAnotherUserPhone.get(3));
                        double transferToAnother = checking + amountTransfer;
                        System.out.println("amount transferred to: " + phoneNum);
                        double transferChecking = Double.parseDouble(transferDetails.get(3));
                        double currentBalance = transferChecking - amountTransfer;
                        System.out.println("your current balance for checking account is: " + currentBalance);
                    }
                }
                if (userTransferChoice == 2) {
                    System.out.println("1. transfer through IBAN");
                    System.out.println("2. transfer through Phone Number");
                    int transferChoice = input.nextInt();
                    if (transferChoice == 1) {
                        System.out.println("enter IBAN number");
                        String ibanNum = input.next().toUpperCase();
                        //
                        List<String> getAnotherUserIban = StoredDatabase.getUserIBAN(ibanNum);
                        String checkingIBAN = getAnotherUserIban.get(6);
                        String savingIBAN = getAnotherUserIban.get(7);
                        double checking = Double.parseDouble(getAnotherUserIban.get(3));
                        double saving = Double.parseDouble(getAnotherUserIban.get(4));
                        if (ibanNum.equals(checkingIBAN)) {
                            double transferToAnother = checking + amountTransfer;
                            System.out.println("amount transferred to: " + ibanNum + " checking account");
                        } else if (ibanNum.equals(savingIBAN)) {
                            double transferToAnother = saving + amountTransfer;
                            System.out.println("amount transferred to: " + ibanNum + " saving account");
                        } else {
                            System.out.println("IBAN doesn't exist");
                        }
                        double transferSaving = Double.parseDouble(transferDetails.get(4));
                        double currentBalance = transferSaving - amountTransfer;
                        System.out.println("your current balance for saving account is: " + currentBalance);
                    } else if (transferChoice == 2) {
                        System.out.println("enter phone number");
                        String phoneNum = input.next().toUpperCase();
                        //
                        List<String> getAnotherUserPhone = StoredDatabase.getUserPhoneNumber(phoneNum);
                        double saving = Double.parseDouble(getAnotherUserPhone.get(4));
                        double transferToAnother = saving + amountTransfer;
                        System.out.println("amount transferred to: " + phoneNum);
                        double transferSaving = Double.parseDouble(transferDetails.get(4));
                        double currentBalance = transferSaving - amountTransfer;
                        System.out.println("your current balance for saving account is: " + currentBalance);
                    }
                }
                break;
            default:
                System.out.println("ERROR! INVALID CHOICE");
        }
        return false;
    }

    @Override
    public void showBalance() {
    }
    @Override
    public void overdraftProtection(LoginDetails loginDetails, double amountWithdraw, String choice) throws Exception {
        switch (choice) {
            case "checking":
                double checkingBalance = Accounts.checkingBalance;

                if (checkingBalance < 0 && amountWithdraw > checkingBalance) {
                    double overdraftCharge = amountWithdraw + 35;
                    double balanceWithdrawChecking = checkingBalance - overdraftCharge;
                    System.out.println("you have withdrawn more than available from your account: " + amountWithdraw + "your current balance after overdraft protection fee is: " + balanceWithdrawChecking);
                } else if (checkingBalance < amountWithdraw && amountWithdraw >= 100) {
                    System.out.println("Can not withdraw more than 100$ if account balance is negative!!");
                }
            case "saving":
                double savingBalance = Accounts.savingBalance;
                if (savingBalance < 0 && amountWithdraw > 100) {
                    System.out.println("Can not withdraw more than 100$ if account balance is negative!!");
                } else if (savingBalance < 0 && amountWithdraw > savingBalance) {
                    double overdraftCharge = amountWithdraw + 35;
                    double balanceWithdrawSaving = savingBalance - overdraftCharge;
                    System.out.println("you have withdrawn more than available from your account: " + amountWithdraw + "your current balance after overdraft protection fee is: " + balanceWithdrawSaving);
                }
            default:
                System.out.println("ERROR!");
        }
    }

}
