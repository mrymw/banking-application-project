package com.mrym.project;
import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class StoredDatabase {
    public static List<String> getUserDetails(LoginDetails details) throws Exception {
        List<List<String>> database = new ArrayList<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("database.txt"))) {
            String user;
            while ((user = bufferedReader.readLine()) != null) {
                List<String> databaseDetails = List.of(user.split(","));
                database.add(databaseDetails);
            }
            for (List<String> record : database) {
                if (record.size() > 12) {
                    int userID = Integer.parseInt(record.get(0));
                    String userName = record.get(1).concat(record.get(2));
                    String userPassword = record.get(3);
                    //performing decryption
                    Decryption decryption = new Decryption();
                    decryption.initFromStrings("w/cTbFoEzK05F5EnhWjCIw==", "/8StF0Ez9/YSizIL");
                    String decryptPassword = decryption.decrypt(userPassword);
                    double checkingAccount = Double.parseDouble(record.get(4));
                    double savingAccount = Double.parseDouble(record.get(5));
                    String userRole = record.get(6);
                    String checkingIBAN = record.get(7);
                    String savingIBAN = record.get(8);
                    String cardChecking = record.get(9);
                    String cardSaving = record.get(10);
                    String phoneNumber = record.get(11);
                    int checkingCardPin = Integer.parseInt(record.get(12));
                    int savingCardPin = Integer.parseInt(record.get(13));
                    if (userName.equals(details.getUserName())) {
                        return List.of(
                                //0
                                String.valueOf(userID),
                                //1
                                userName,
                                //2
                                decryptPassword,
                                //3
                                String.valueOf(checkingAccount),
                                //4
                                String.valueOf(savingAccount),
                                //5
                                userRole,
                                //6
                                checkingIBAN,
                                //7
                                savingIBAN,
                                //8
                                cardChecking,
                                //9
                                cardSaving,
                                //10
                                phoneNumber,
                                //11
                                String.valueOf(checkingCardPin),
                                //12
                                String.valueOf(savingCardPin)
                        );
                    }
                }
            }
            return new ArrayList<>();
        }
    }
    public void setCardPin(LoginDetails details) throws Exception {
        List<String> cardPin = new ArrayList<>();
        int checkingPinList = 0;
        int savingPinList = 0;
        Scanner input = new Scanner(System.in);
        List<List<String>> database = new ArrayList<>();
        boolean userFound = false;
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("database.txt"))) {
            String user;
            while ((user = bufferedReader.readLine()) != null) {
                List<String> databaseDetails = new ArrayList<>(Arrays.asList(user.split(",")));
                if (databaseDetails.size() > 12) {
                    int userID = Integer.parseInt(databaseDetails.get(0));
                    String userName = databaseDetails.get(1).concat(databaseDetails.get(2));
                    String userPassword = databaseDetails.get(3);
                    // Perform decryption
                    Decryption decryption = new Decryption();
                    decryption.initFromStrings("w/cTbFoEzK05F5EnhWjCIw==", "/8StF0Ez9/YSizIL");
                    String decryptPassword = decryption.decrypt(userPassword);
                    if (details.getUserName().equals(userName)) {
                        userFound = true;
                        cardPin = databaseDetails;
                        checkingPinList = Integer.parseInt(databaseDetails.get(12));
                        savingPinList = Integer.parseInt(databaseDetails.get(13));
                    }
                }
                database.add(databaseDetails);
            }
        }

        if (userFound) {
            if (checkingPinList == 0 && savingPinList == 0) {
                System.out.println("\nWelcome! " + details.getUserName() + " \n Your pin hasn't been set, please follow the steps to set your pin: ");
                System.out.println("Enter Checking Card Pin: ");
                int checkingPin = input.nextInt();
                System.out.println("Please enter card pin again to confirm: ");
                int reentered = input.nextInt();
                if (checkingPin == reentered) {
                    cardPin.set(12, String.valueOf(checkingPin));
                    System.out.println("Pin successfully saved!");
                }

                System.out.println("Enter Saving Card Pin: ");
                int savingPin = input.nextInt();
                System.out.println("Please enter card pin again to confirm: ");
                int savingAgain = input.nextInt();
                if (savingPin == savingAgain) {
                    cardPin.set(13, String.valueOf(savingPin));
                    System.out.println("Pin successfully saved!");
                }
            } else {
                System.out.println("\nWould you like to reset your pin? Yes/No");
                String answer = input.next().toLowerCase();
                if (answer.equals("yes")) {
                    System.out.println("Please input which account card you would like to reset the pin for: \n1. Checking Card \n2. Saving Card");
                    int choice = input.nextInt();
                    switch (choice) {
                        case 1:
                            System.out.println("Enter your new checking card pin: ");
                            int resetPinChecking = input.nextInt();
                            System.out.println("Please re-enter pin to confirm: ");
                            int reenterPinChecking = input.nextInt();
                            if (resetPinChecking == reenterPinChecking) {
                                cardPin.set(12, String.valueOf(resetPinChecking));
                                System.out.println("Successfully changed pin number!");
                            }
                            break;
                        case 2:
                            System.out.println("Enter your new saving card pin: ");
                            int resetPinSaving = input.nextInt();
                            System.out.println("Please re-enter pin to confirm: ");
                            int reenterPinSaving = input.nextInt();
                            if (resetPinSaving == reenterPinSaving) {
                                cardPin.set(13, String.valueOf(resetPinSaving));
                                System.out.println("Successfully changed pin number!");
                            }
                            break;
                        default:
                            System.out.println("Invalid choice.");
                            break;
                    }
                } else {
                    System.out.println("Happy Browsing!");
                }
            }
            try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("database.txt"))) {
                for (List<String> record : database) {
                    bufferedWriter.write(String.join(",", record));
                    bufferedWriter.newLine();
                }
            }
        } else {
            System.out.println("User not found.");
        }
    }

}



