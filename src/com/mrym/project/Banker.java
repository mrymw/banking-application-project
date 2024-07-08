package com.mrym.project;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;

public class Banker extends UserDetails implements Bank {
    boolean isPositive;
    final static int overdraftAttempt = 2;
    protected ArrayList<String> transactionHistory = new ArrayList<>();
    public Banker() {super();}
    public static void addCustomer(UserDetails details) throws Exception {
        String[] addDetails = {String.valueOf(details.getUserID()), details.getUserFirstName(), details.getUserLastName(), details.getUserPassword(), String.valueOf(details.getCheckingAccountBalance()), String.valueOf(details.getSavingAccountBalance()), "C", String.valueOf(details.getCheckingIBAN()), String.valueOf(details.getSavingIBAN()), String.valueOf(details.getCheckingCards()), String.valueOf(details.getSavingCards()), String.valueOf(details.getPhoneNumber()), String.valueOf(0), String.valueOf(0)};
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
        List<String> withdrawList = new ArrayList<>();
        Scanner input = new Scanner(System.in);
        List<List<String>> database = new ArrayList<>();
        boolean userFound = false;
        //reading the file
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("database.txt"))) {
            String user;
            while ((user = bufferedReader.readLine()) != null) {
                List<String> databaseDetails = new ArrayList<>(Arrays.asList(user.split(",")));
                if (databaseDetails.size() > 12) {
                    String userName = databaseDetails.get(1).concat(databaseDetails.get(2));
                    if (details.getUserName().equals(userName)) {
                        userFound = true;
                        withdrawList = databaseDetails;
                    }
                }
                database.add(databaseDetails);
            }
        }
        if (userFound) {
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
                if (checkingBalance >= amountWithdraw) {
                    balanceWithdrawChecking = checkingBalance - amountWithdraw;
                    //updating amount in file
                    withdrawList.set(4, String.valueOf(balanceWithdrawChecking));
                    transactionHistory.add("amount withdrawn from checking account: ");
                    transactionHistory.add(Arrays.toString(new double[]{balanceWithdrawChecking}));
                    System.out.println("amount successfully withdrawn: \nbalance before: " + checkingBalance + "\nbalance after:  " + balanceWithdrawChecking);
                } else {
                    overdraftProtection(details, amountWithdraw);
                }
            } else if (answer.equals("saving")) {
                double balanceWithdrawSaving;
                if (savingBalance >= amountWithdraw) {
                    balanceWithdrawSaving = savingBalance - amountWithdraw;
                    withdrawList.set(5, String.valueOf(balanceWithdrawSaving));
                    transactionHistory.add("amount withdrawn from saving account: ");
                    transactionHistory.add(Arrays.toString(new double[]{balanceWithdrawSaving}));
                    System.out.println("amount successfully withdrawn: \nbalance before: " + savingBalance + "\nbalance after:  " + balanceWithdrawSaving);
                } else {
                    System.out.println("AN ERROR OCCURED PLEASE TRY AGAIN LATER!");
                }
            }
            try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("database.txt"))) {
                for (List<String> record : database) {
                    bufferedWriter.write(String.join(",", record));
                    bufferedWriter.newLine();
                }
            }
        } else {
            System.out.println("user not found");
        }
        return isPositive;
    }
    @Override
    public boolean deposit(LoginDetails details, double amountDeposit) throws Exception{
        List<String> depositList = new ArrayList<>();
        Scanner input = new Scanner(System.in);
        List<List<String>> database = new ArrayList<>();
        boolean userFound = false;
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("database.txt"))) {
            String user;
            while ((user = bufferedReader.readLine()) != null) {
                List<String> databaseDetails = new ArrayList<>(Arrays.asList(user.split(",")));
                if (databaseDetails.size() > 12) {
                    String userName = databaseDetails.get(1).concat(databaseDetails.get(2));
                    if (details.getUserName().equals(userName)) {
                        userFound = true;
                        depositList = databaseDetails;
                    }
                }
                database.add(databaseDetails);
            }
        }
        Accounts.checkingAccount(details);
        Accounts.savingAccount(details);
        double checkingBalance = Accounts.checkingBalance;
        double savingBalance = Accounts.savingBalance;
        System.out.println(checkingBalance);
        if (amountDeposit < 0) {
            System.out.println("ERROR NEGATIVE NUMBER!");
            isPositive = false;
        }
        System.out.println("would you like to deposit to checking or saving account?");
        String answer = input.next().toLowerCase();
        if (answer.equals("checking")) {
            double balanceDepositChecking;
            if (checkingBalance >= amountDeposit){
                balanceDepositChecking = checkingBalance + amountDeposit;
                depositList.set(4, String.valueOf(balanceDepositChecking));
                transactionHistory.add("amount deposit to checking account: ");
                transactionHistory.add(Arrays.toString(new double[] {balanceDepositChecking}));
                System.out.println("amount successfully deposit: \nbalance before: " + checkingBalance +"\nbalance after:  "+ balanceDepositChecking);
            }
        } else if (answer.equals("saving")) {
            double balanceDepositSaving;
            if (savingBalance >= amountDeposit) {
                balanceDepositSaving = savingBalance - amountDeposit;
                depositList.set(5, String.valueOf(balanceDepositSaving));
                transactionHistory.add("amount deposit to saving account: ");
                transactionHistory.add(Arrays.toString(new double[] {balanceDepositSaving}));
                System.out.println("amount successfully deposit: \nbalance before: " + savingBalance +"\nbalance after:  "+ balanceDepositSaving);
            }
        }
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("database.txt"))) {
            for (List<String> record : database) {
                bufferedWriter.write(String.join(",", record));
                bufferedWriter.newLine();
            }
        }
        return isPositive;
    }
    //in this method a user can transfer from their own accounts which is checking to saving or saving to checking
    //they can also choose to transfer to another user account from their checking or saving account
    //the transfer can happen through IBAN number of either or the phone number as they both are classified as unique

    @Override
    public boolean transfer(LoginDetails details, double amountTransfer) throws Exception {
        List<String> transferList = new ArrayList<>();
        List<List<String>> database = new ArrayList<>();
        boolean userFound = false;
        boolean recipientFound = false;
        // Reading the database file and finding the user
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("database.txt"))) {
            String user;
            while ((user = bufferedReader.readLine()) != null) {
                List<String> databaseDetails = new ArrayList<>(Arrays.asList(user.split(",")));
                if (databaseDetails.size() > 12) {
                    String userName = databaseDetails.get(1).concat(databaseDetails.get(2));
                    if (details.getUserName().equals(userName)) {
                        userFound = true;
                        transferList = databaseDetails;
                    }
                }
                database.add(databaseDetails);
            }
        }

        if (!userFound) {
            throw new Exception("User not found in database");
        }
        Accounts.checkingAccount(details);
        Accounts.savingAccount(details);
        double checkingBalance = Accounts.checkingBalance;
        double savingBalance = Accounts.savingBalance;

        if (amountTransfer < 0) {
            System.out.println("ERROR: NEGATIVE NUMBER!");
            return false;
        }
        System.out.println("Where would you like to transfer money to?");
        System.out.println("1. Own account");
        System.out.println("2. Another customer");
        Scanner input = new Scanner(System.in);
        int answer = input.nextInt();
        switch (answer) {
            case 1:
                System.out.println("Select your choice:");
                System.out.println("1. From checking to saving");
                System.out.println("2. From saving to checking");
                int choice = input.nextInt();
                if (choice == 1) {
                    double transferBalanceChecking = checkingBalance - amountTransfer;
                    double transferBalanceSaving = savingBalance + amountTransfer;
                    transferList.set(4, String.valueOf(transferBalanceChecking));
                    transferList.set(5, String.valueOf(transferBalanceSaving));
                    System.out.println("Balance after transferring from checking to saving: \nChecking: " + transferBalanceChecking +
                            "\nSaving: " + transferBalanceSaving);
                } else if (choice == 2) {
                    double transferBalanceSaving = savingBalance - amountTransfer;
                    double transferBalanceChecking = checkingBalance + amountTransfer;
                    transferList.set(5, String.valueOf(transferBalanceSaving));
                    transferList.set(4, String.valueOf(transferBalanceChecking));
                    System.out.println("Balance after transferring from saving to checking: \nSaving: " + transferBalanceSaving +
                            "\nChecking: " + transferBalanceChecking);
                }
                break;
            case 2:
                System.out.println("From which account would you like to transfer?");
                System.out.println("1. Checking");
                System.out.println("2. Saving");
                int userTransferChoice = input.nextInt();
                if (userTransferChoice == 1) {
                    System.out.println("1. Transfer through IBAN");
                    System.out.println("2. Transfer through Phone Number");
                    int transferChoice = input.nextInt();
                    if (transferChoice == 1) {
                        System.out.println("Enter IBAN number:");
                        String ibanNum = input.next().toUpperCase();
                        for (List<String> userRecord : database) {
                            String checkingIBAN = userRecord.get(7);
                            String savingIBAN = userRecord.get(8);
                            if (ibanNum.equals(checkingIBAN) || ibanNum.equals(savingIBAN)) {
                                recipientFound = true;
                                if (ibanNum.equals(checkingIBAN)) {
                                    double anotherUserCheckingAccount = Double.parseDouble(userRecord.get(4)) + amountTransfer;
                                    userRecord.set(4, String.valueOf(anotherUserCheckingAccount));
                                    System.out.println("Amount transferred to: " + ibanNum + " checking account: " + anotherUserCheckingAccount);
                                } else {
                                    double anotherUserSavingAccount = Double.parseDouble(userRecord.get(5)) + amountTransfer;
                                    userRecord.set(5, String.valueOf(anotherUserSavingAccount));
                                    System.out.println("Amount transferred to: " + ibanNum + " saving account: " + anotherUserSavingAccount);
                                }
                                double transferCurrentUserChecking = Double.parseDouble(transferList.get(4)) - amountTransfer;
                                transferList.set(4, String.valueOf(transferCurrentUserChecking));
                                System.out.println("Your current balance for checking account is: " + transferCurrentUserChecking);
                                break;
                            }
                        }
                        if (!recipientFound) {
                            System.out.println("IBAN doesn't exist");
                        }
                    } else if (transferChoice == 2) {
                        System.out.println("Enter phone number:");
                        String phoneNum = input.next().toUpperCase();
                        for (List<String> userRecord : database) {
                            String recipientPhoneNum = userRecord.get(11); // Assuming phone number is at index 8
                            if (phoneNum.equals(recipientPhoneNum)) {
                                recipientFound = true;
                                double anotherUserCheckingAccount = Double.parseDouble(userRecord.get(4)) + amountTransfer;
                                userRecord.set(4, String.valueOf(anotherUserCheckingAccount));
                                double transferCurrentUserChecking = Double.parseDouble(transferList.get(4)) - amountTransfer;
                                transferList.set(4, String.valueOf(transferCurrentUserChecking));
                                System.out.println("Amount transferred to: " + phoneNum + " checking account");
                                System.out.println("Your current balance for checking account is: " + transferCurrentUserChecking);
                                break;
                            }
                        }
                        if (!recipientFound) {
                            System.out.println("Phone number doesn't exist");
                        }
                    }
                } else if (userTransferChoice == 2) {
                    System.out.println("1. Transfer through IBAN");
                    System.out.println("2. Transfer through Phone Number");
                    int transferChoice = input.nextInt();

                    if (transferChoice == 1) {
                        System.out.println("Enter IBAN number:");
                        String ibanNum = input.next().toUpperCase();
                        for (List<String> userRecord : database) {
                            String checkingIBAN = userRecord.get(7);
                            String savingIBAN = userRecord.get(8);
                            if (ibanNum.equals(checkingIBAN) || ibanNum.equals(savingIBAN)) {
                                recipientFound = true;
                                if (ibanNum.equals(checkingIBAN)) {
                                    double anotherUserCheckingAccount = Double.parseDouble(userRecord.get(4)) + amountTransfer;
                                    userRecord.set(4, String.valueOf(anotherUserCheckingAccount));
                                    System.out.println("Amount transferred to: " + ibanNum + " checking account: " + anotherUserCheckingAccount);
                                } else {
                                    double anotherUserSavingAccount = Double.parseDouble(userRecord.get(5)) + amountTransfer;
                                    userRecord.set(5, String.valueOf(anotherUserSavingAccount));
                                    System.out.println("Amount transferred to: " + ibanNum + " saving account: " + anotherUserSavingAccount);
                                }
                                double transferCurrentSavingAccount = Double.parseDouble(transferList.get(5)) - amountTransfer;
                                transferList.set(5, String.valueOf(transferCurrentSavingAccount));
                                System.out.println("Your current balance for saving account is: " + transferCurrentSavingAccount);
                                break;
                            }
                        }
                        if (!recipientFound) {
                            System.out.println("IBAN doesn't exist");
                        }
                    } else if (transferChoice == 2) {
                        System.out.println("Enter phone number:");
                        String phoneNum = input.next().toUpperCase();
                        for (List<String> userRecord : database) {
                            String recipientPhoneNum = userRecord.get(12); // Assuming phone number is at index 8
                            if (phoneNum.equals(recipientPhoneNum)) {
                                recipientFound = true;
                                double anotherUserSavingAccount = Double.parseDouble(userRecord.get(5)) + amountTransfer;
                                userRecord.set(5, String.valueOf(anotherUserSavingAccount));
                                double transferCurrentSavingAccount = Double.parseDouble(transferList.get(5)) - amountTransfer;
                                transferList.set(5, String.valueOf(transferCurrentSavingAccount));
                                System.out.println("Amount transferred to: " + phoneNum + " saving account");
                                System.out.println("Your current balance for saving account is: " + transferCurrentSavingAccount);
                                break;
                            }
                        }
                        if (!recipientFound) {
                            System.out.println("Phone number doesn't exist");
                        }
                    }
                }
                break;
            default:
                System.out.println("ERROR: INVALID CHOICE");
        }
        // Writing the updated database back to the file
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("database.txt"))) {
            for (List<String> record : database) {
                bufferedWriter.write(String.join(",", record));
                bufferedWriter.newLine();
            }
        }
        return true;
    }
    @Override
    public void showBalance() {}
    @Override
    public void overdraftProtection(LoginDetails loginDetails, double amountWithdraw) throws Exception {
        double checkingBalance = Accounts.checkingBalance;
        if (checkingBalance < 0 && amountWithdraw > 100) {
            System.out.println("Can not withdraw more than 100$ if account balance is negative!!");
        } else if (checkingBalance < 0 && amountWithdraw > checkingBalance) {
            double overdraftCharge = amountWithdraw + 35;
            double balanceWithdrawChecking = checkingBalance - overdraftCharge;
            System.out.println("you have withdrawn more than available from your account: " + amountWithdraw + "your current balance after overdraft protection fee is: " + balanceWithdrawChecking);
        }
    }

}
