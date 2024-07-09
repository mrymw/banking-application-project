package com.mrym.project;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class Customer extends UserDetails implements Bank {
    boolean isPositive;
    static String userName;
    static int userID;
    static String userCheckingCardType;
    static String userSavingCardType;
    List<String> userData = new ArrayList<>();
    static String fileName;
    private void displayTransactionData(String userName, int userID, String transactionType, double amount, double balanceAfter, String accountType) throws IOException {
        fileName = userName + userID + ".txt";
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName, true))) {
            //display current date of the transaction
            LocalTime localTime = LocalTime.now();
            LocalDate localDate = LocalDate.now();
            String transactionDetails = String.format("%s, %s, %s, %.2f, %.2f, %s%n", localDate, localTime,  transactionType, amount, balanceAfter, accountType);
            bufferedWriter.write(transactionDetails);
        }
    }
    @Override
    public void detailedAccountStatement(LoginDetails userDetails) throws Exception {
        List<String> details = StoredDatabase.getUserDetails(userDetails);
        if (details.isEmpty()) {
            System.out.println("user not found");
            return;
        }
        String userName = details.get(1);
        int userID = Integer.parseInt(details.get(0));
        double checkingAccountBalance = Double.parseDouble(details.get(3));
        double savingAccountBalance = Double.parseDouble(details.get(4));
        String fileName = userName + userID + ".txt";
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName))){
            String line;
            System.out.println("Detailed Account Statement: ");
            while((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
            }
            System.out.println("Closing balance for checking account: " + checkingAccountBalance);
            System.out.println("Closing balance for saving account: " + savingAccountBalance);
        }
    }
    public Customer (){super();}
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
                    userName = databaseDetails.get(1).concat(databaseDetails.get(2));
                    userID = Integer.parseInt(databaseDetails.get(0));
                    userCheckingCardType = databaseDetails.get(9);
                    userSavingCardType = databaseDetails.get(10);
                    if (details.getUserName().equals(userName)) {
                        userFound = true;
                        withdrawList = databaseDetails;
                        userData.add(userName);
                        userData.add(String.valueOf(userID));
                        userData.add(String.valueOf(userCheckingCardType));
                        userData.add(String.valueOf(userSavingCardType));
                    }
                }
                database.add(databaseDetails);
            }
        }
        if (userFound) {
            String userName = userData.get(0);
            int userID = Integer.parseInt(userData.get(1));
            String checkingCard = userData.get(2);
            String savingCard = userData.get(3);
            Accounts.checkingAccount(details);
            Accounts.savingAccount(details);
            double checkingBalance = Accounts.checkingBalance;
            double savingBalance = Accounts.savingBalance;
            System.out.println(checkingBalance);
            System.out.println(checkingCard);
            if (amountWithdraw < 0) {
                System.out.println("ERROR NEGATIVE NUMBER!");
                isPositive = false;
            }

            System.out.println("would you like to withdraw money from checking or saving account?");
            String answer = input.next().toLowerCase();
            if (answer.equals("checking")) {
                Map<String, Double> limits = Cards.getLimits(checkingCard);
                if (limits != null) {
                    Double withdrawLimit = limits.get("withdraw");
                    if (amountWithdraw > withdrawLimit) {
                        System.out.println("Withdrawal amount exceeds card limit.");
                        return false;
                    }
                } else {
                    System.out.println("Card type not recognized.");
                }
                double balanceWithdrawChecking;
                if (checkingBalance >= amountWithdraw) {
                    balanceWithdrawChecking = checkingBalance - amountWithdraw;
                    //updating amount in file
                    withdrawList.set(4, String.valueOf(balanceWithdrawChecking));
                    System.out.println("amount successfully withdrawn: \nbalance before: " + checkingBalance + "\nbalance after:  " + balanceWithdrawChecking);
                    //adding it in user file
                    displayTransactionData(userName, userID, "withdraw", amountWithdraw, balanceWithdrawChecking, "checking");
                } else {
                    overdraftProtection(details, amountWithdraw);
                }
            } else if (answer.equals("saving")) {
                Map<String, Double> limits = Cards.getLimits(savingCard);
                if (limits != null) {
                    Double withdrawLimit = limits.get("withdraw");
                    if (amountWithdraw > withdrawLimit) {
                        System.out.println("Withdrawal amount exceeds card limit.");
                        return false;
                    }
                } else {
                    System.out.println("Card type not recognized.");
                }
                double balanceWithdrawSaving;
                if (savingBalance >= amountWithdraw) {
                    balanceWithdrawSaving = savingBalance - amountWithdraw;
                    withdrawList.set(5, String.valueOf(balanceWithdrawSaving));
                    System.out.println("amount successfully withdrawn: \nbalance before: " + savingBalance + "\nbalance after:  " + balanceWithdrawSaving);
                    displayTransactionData(userName, userID, "withdraw", amountWithdraw, balanceWithdrawSaving, "saving");
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
                    userName = databaseDetails.get(1).concat(databaseDetails.get(2));
                    userID = Integer.parseInt(databaseDetails.get(0));
                    userCheckingCardType = databaseDetails.get(9);
                    userSavingCardType = databaseDetails.get(10);
                    if (details.getUserName().equals(userName)) {
                        userFound = true;
                        depositList = databaseDetails;
                        userData.add(userName);
                        userData.add(String.valueOf(userID));
                        userData.add(String.valueOf(userCheckingCardType));
                        userData.add(String.valueOf(userSavingCardType));
                    }
                }
                database.add(databaseDetails);
            }
        }
        if (!userFound) {
            System.out.println("User not found!");
        }
        Accounts.checkingAccount(details);
        Accounts.savingAccount(details);
        double checkingBalance = Accounts.checkingBalance;
        double savingBalance = Accounts.savingBalance;
        String userName = userData.get(0);
        int userID = Integer.parseInt(userData.get(1));
        String checkingCard = userData.get(2);
        String savingCard = userData.get(3);
        System.out.println(checkingBalance);
        if (amountDeposit < 0) {
            System.out.println("ERROR NEGATIVE NUMBER!");
            isPositive = false;
        }
        System.out.println("would you like to deposit to checking or saving account?");
        String answer = input.next().toLowerCase();
        if (answer.equals("checking")) {
            Map<String, Double> limits = Cards.getLimits(checkingCard);
            if (limits != null) {
                Double depositLimit = limits.get("deposit");
                if (amountDeposit > depositLimit) {
                    System.out.println("Deposit amount exceeds card limit.");
                    return false;
                }
            } else {
                System.out.println("Card type not recognized.");
            }
            double balanceDepositChecking;
            if (checkingBalance >= amountDeposit){
                balanceDepositChecking = checkingBalance + amountDeposit;
                depositList.set(4, String.valueOf(balanceDepositChecking));
                System.out.println("amount successfully deposit: \nbalance before: " + checkingBalance +"\nbalance after:  "+ balanceDepositChecking);
                displayTransactionData(userName, userID, "deposit", amountDeposit, balanceDepositChecking, "checking");

            }
        } else if (answer.equals("saving")) {
            Map<String, Double> limits = Cards.getLimits(savingCard);
            if (limits != null) {
                Double depositLimit = limits.get("deposit");
                if (amountDeposit > depositLimit) {
                    System.out.println("Deposit amount exceeds card limit.");
                    return false;
                }
            } else {
                System.out.println("Card type not recognized.");
            }
            double balanceDepositSaving;
            if (savingBalance >= amountDeposit) {
                balanceDepositSaving = savingBalance - amountDeposit;
                depositList.set(5, String.valueOf(balanceDepositSaving));
                System.out.println("amount successfully deposit: \nbalance before: " + savingBalance +"\nbalance after:  "+ balanceDepositSaving);
                displayTransactionData(userName, userID, "deposit", amountDeposit, balanceDepositSaving, "saving");

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
                    userName = databaseDetails.get(1).concat(databaseDetails.get(2));
                    userID = Integer.parseInt(databaseDetails.get(0));
                    userCheckingCardType = databaseDetails.get(9);
                    userSavingCardType = databaseDetails.get(10);
                    if (details.getUserName().equals(userName)) {
                        userFound = true;
                        transferList = databaseDetails;
                        userData.add(userName);
                        userData.add(String.valueOf(userID));
                        userData.add(String.valueOf(userCheckingCardType));
                        userData.add(String.valueOf(userSavingCardType));
                    }
                }
                database.add(databaseDetails);
            }
        }

        if (!userFound) {
            throw new Exception("User not found in database");
        }
        String checkingCard = userData.get(2);
        String savingCard = userData.get(3);
        String userName = userData.get(0);
        int userID = Integer.parseInt(userData.get(1));
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
                    Map<String, Double> limits = Cards.getLimits(checkingCard);
                    if (limits != null) {
                        Double transferLimit = limits.get("transferOwnAccount");
                        if (amountTransfer > transferLimit) {
                            System.out.println("Transfer amount exceeds card limit.");
                            return false;
                        }
                    } else {
                        System.out.println("Card type not recognized.");
                    }
                    double transferBalanceChecking = checkingBalance - amountTransfer;
                    double transferBalanceSaving = savingBalance + amountTransfer;
                    transferList.set(4, String.valueOf(transferBalanceChecking));
                    transferList.set(5, String.valueOf(transferBalanceSaving));
                    System.out.println("Balance after transferring from checking to saving: \nChecking: " + transferBalanceChecking +
                            "\nSaving: " + transferBalanceSaving);
                    displayTransactionData(userName, userID, "transfer", amountTransfer, transferBalanceChecking, "checking");
                    displayTransactionData(userName, userID, "transfer", amountTransfer, transferBalanceSaving, "saving");
                } else if (choice == 2) {
                    Map<String, Double> limits = Cards.getLimits(savingCard);
                    if (limits != null) {
                        Double transferLimit = limits.get("transferOwnAccount");
                        if (amountTransfer > transferLimit) {
                            System.out.println("Transfer amount exceeds card limit.");
                            return false;
                        }
                    } else {
                        System.out.println("Card type not recognized.");
                    }
                    double transferBalanceSaving = savingBalance - amountTransfer;
                    double transferBalanceChecking = checkingBalance + amountTransfer;
                    transferList.set(5, String.valueOf(transferBalanceSaving));
                    transferList.set(4, String.valueOf(transferBalanceChecking));
                    System.out.println("Balance after transferring from saving to checking: \nSaving: " + transferBalanceSaving +
                            "\nChecking: " + transferBalanceChecking);
                    displayTransactionData(userName, userID, "transfer", amountTransfer, transferBalanceSaving, "saving");
                    displayTransactionData(userName, userID, "transfer", amountTransfer, transferBalanceChecking, "checking");
                }
                break;
            case 2:
                System.out.println("From which account would you like to transfer?");
                System.out.println("1. Checking");
                System.out.println("2. Saving");
                int userTransferChoice = input.nextInt();
                if (userTransferChoice == 1) {
                    Map<String, Double> limits = Cards.getLimits(checkingCard);
                    if (limits != null) {
                        Double transferLimit = limits.get("transfer");
                        if (amountTransfer > transferLimit) {
                            System.out.println("Transfer amount exceeds card limit.");
                            return false;
                        }
                    } else {
                        System.out.println("Card type not recognized.");
                    }
                    System.out.println("1. Transfer through IBAN");
                    System.out.println("2. Transfer through Phone Number");
                    int transferChoice = input.nextInt();
                    if (transferChoice == 1) {
                        System.out.println("Enter IBAN number:");
                        String ibanNum = input.next().toUpperCase();
                        for (List<String> userRecord : database) {
                            String checkingIBAN = userRecord.get(7);
                            String savingIBAN = userRecord.get(8);
                            String anotherUserName = userRecord.get(1).concat(userRecord.get(2));
                            int anotherUserID = Integer.parseInt(userRecord.get(0));
                            if (ibanNum.equals(checkingIBAN) || ibanNum.equals(savingIBAN)) {
                                recipientFound = true;
                                if (ibanNum.equals(checkingIBAN)) {
                                    double anotherUserCheckingAccount = Double.parseDouble(userRecord.get(4)) + amountTransfer;
                                    userRecord.set(4, String.valueOf(anotherUserCheckingAccount));
                                    System.out.println("Amount transferred to: " + ibanNum + " checking account: " + anotherUserCheckingAccount);
                                    displayTransactionData(anotherUserName, anotherUserID, "transfer", amountTransfer, anotherUserCheckingAccount, "checking");
                                } else {
                                    double anotherUserSavingAccount = Double.parseDouble(userRecord.get(5)) + amountTransfer;
                                    userRecord.set(5, String.valueOf(anotherUserSavingAccount));
                                    System.out.println("Amount transferred to: " + ibanNum + " saving account: " + anotherUserSavingAccount);
                                    displayTransactionData(anotherUserName, anotherUserID, "transfer", amountTransfer, anotherUserSavingAccount, "saving");
                                }
                                double transferCurrentUserChecking = Double.parseDouble(transferList.get(4)) - amountTransfer;
                                transferList.set(4, String.valueOf(transferCurrentUserChecking));
                                displayTransactionData(userName, userID, "transfer", amountTransfer, transferCurrentUserChecking, "checking");
                                System.out.println("Your current balance for checking account is: " + transferCurrentUserChecking);
                                break;
                            }
                        }
                        if (!recipientFound) {
                            System.out.println("IBAN doesn't exist");
                        }
                    } else if (transferChoice == 2) {
                        Map<String, Double> choiceLimits = Cards.getLimits(savingCard);
                        if (choiceLimits != null) {
                            Double transferLimit = choiceLimits.get("transfer");
                            if (amountTransfer > transferLimit) {
                                System.out.println("Transfer amount exceeds card limit.");
                                return false;
                            }
                        } else {
                            System.out.println("Card type not recognized.");
                        }
                        System.out.println("Enter phone number:");
                        String phoneNum = input.next().toUpperCase();
                        for (List<String> userRecord : database) {
                            String recipientPhoneNum = userRecord.get(11);
                            String anotherUserName = userRecord.get(1).concat(userRecord.get(2));
                            int anotherUserID = Integer.parseInt(userRecord.get(0));
                            if (phoneNum.equals(recipientPhoneNum)) {
                                recipientFound = true;
                                double anotherUserCheckingAccount = Double.parseDouble(userRecord.get(4)) + amountTransfer;
                                userRecord.set(4, String.valueOf(anotherUserCheckingAccount));
                                displayTransactionData(anotherUserName, anotherUserID, "transfer", amountTransfer, anotherUserCheckingAccount, "checking");
                                double transferCurrentUserChecking = Double.parseDouble(transferList.get(4)) - amountTransfer;
                                transferList.set(4, String.valueOf(transferCurrentUserChecking));
                                System.out.println("Amount transferred to: " + phoneNum + " checking account");
                                System.out.println("Your current balance for checking account is: " + transferCurrentUserChecking);
                                displayTransactionData(userName, userID, "transfer", amountTransfer, transferCurrentUserChecking, "checking");
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
                            String anotherUserName = userRecord.get(1).concat(userRecord.get(2));
                            int anotherUserID = Integer.parseInt(userRecord.get(0));
                            if (ibanNum.equals(checkingIBAN) || ibanNum.equals(savingIBAN)) {
                                recipientFound = true;
                                if (ibanNum.equals(checkingIBAN)) {
                                    double anotherUserCheckingAccount = Double.parseDouble(userRecord.get(4)) + amountTransfer;
                                    userRecord.set(4, String.valueOf(anotherUserCheckingAccount));
                                    System.out.println("Amount transferred to: " + ibanNum + " checking account: " + anotherUserCheckingAccount);
                                    displayTransactionData(anotherUserName, anotherUserID, "transfer", amountTransfer, anotherUserCheckingAccount, "checking");
                                } else {
                                    double anotherUserSavingAccount = Double.parseDouble(userRecord.get(5)) + amountTransfer;
                                    userRecord.set(5, String.valueOf(anotherUserSavingAccount));
                                    System.out.println("Amount transferred to: " + ibanNum + " saving account: " + anotherUserSavingAccount);
                                    displayTransactionData(anotherUserName, anotherUserID, "transfer", amountTransfer, anotherUserSavingAccount, "saving");

                                }
                                double transferCurrentSavingAccount = Double.parseDouble(transferList.get(5)) - amountTransfer;
                                transferList.set(5, String.valueOf(transferCurrentSavingAccount));
                                System.out.println("Your current balance for saving account is: " + transferCurrentSavingAccount);
                                displayTransactionData(userName, userID, "transfer", amountTransfer, transferCurrentSavingAccount, "saving");
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
                            String recipientPhoneNum = userRecord.get(11);
                            String anotherUserName = userRecord.get(1).concat(userRecord.get(2));
                            int anotherUserID = Integer.parseInt(userRecord.get(0));
                            if (phoneNum.equals(recipientPhoneNum)) {
                                recipientFound = true;
                                double anotherUserSavingAccount = Double.parseDouble(userRecord.get(5)) + amountTransfer;
                                userRecord.set(5, String.valueOf(anotherUserSavingAccount));
                                displayTransactionData(anotherUserName, anotherUserID, "transfer", amountTransfer, anotherUserSavingAccount, "saving");
                                double transferCurrentSavingAccount = Double.parseDouble(transferList.get(5)) - amountTransfer;
                                transferList.set(5, String.valueOf(transferCurrentSavingAccount));
                                System.out.println("Amount transferred to: " + phoneNum + " saving account");
                                System.out.println("Your current balance for saving account is: " + transferCurrentSavingAccount);
                                displayTransactionData(userName, userID, "transfer", amountTransfer, transferCurrentSavingAccount, "saving");
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
    public void overdraftProtection(LoginDetails loginDetails, double amountWithdraw) throws Exception {
        List<String> userData = new ArrayList<>();
        List<List<String>> database = new ArrayList<>();
        boolean userFound = false;

        // Read from the database file
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("database.txt"))) {
            String user;
            while ((user = bufferedReader.readLine()) != null) {
                List<String> databaseDetails = new ArrayList<>(Arrays.asList(user.split(",")));
                if (databaseDetails.size() > 12) { // Assuming this is the minimum size to contain necessary data
                    String userName = databaseDetails.get(1).concat(databaseDetails.get(2));
                    int userID = Integer.parseInt(databaseDetails.get(0));
                    if (loginDetails.getUserName().equals(userName)) {
                        userFound = true;
                        userData.add(userName);
                        userData.add(String.valueOf(userID));
                        database.add(databaseDetails); // Add to database for potential update
                    }
                }
            }
        }

        if (userFound) {
            String userName = userData.get(0);
            int userID = Integer.parseInt(userData.get(1));
            double checkingBalance = Double.parseDouble(database.get(0).get(4)); // Assuming index 4 is checking account balance
            boolean isAccountActive = Boolean.parseBoolean(database.get(0).get(14)); // Assuming index 14 is account active status


            if (!isAccountActive) {
                System.out.println("Account is deactivated. Deposit money to activate it.");
                return;
            }
            // Implement overdraft protection logic
            if (checkingBalance < 0 && amountWithdraw > 100) {
                System.out.println("Cannot withdraw more than $100 if account balance is negative.");
            } else if (checkingBalance < 0 && amountWithdraw > Math.abs(checkingBalance)) {
                double overdraftCharge = amountWithdraw + 35;
                double balanceAfterOverdraft = checkingBalance - overdraftCharge;

                // Update balance in database
                database.get(0).set(4, String.valueOf(balanceAfterOverdraft));

                // Log transaction
                displayTransactionData(userName, userID, "overdraft", overdraftCharge, balanceAfterOverdraft, "checking");

                System.out.println("You have withdrawn more than available from your account: " + amountWithdraw + ". Your current balance after overdraft protection fee is: " + balanceAfterOverdraft);
            }
        }
        // Write updated data back to the database file
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("database.txt"))) {
            for (List<String> record : database) {
                bufferedWriter.write(String.join(",", record));
                bufferedWriter.newLine();
            }
        }
    }
}
