package com.mrym.project;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

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
                if (record.size() > 10) {
                    int userID = Integer.parseInt(record.get(0));
                    String userName = record.get(1).concat(record.get(2));
                    String userPassword = record.get(3);
                    double checkingAccount = Double.parseDouble(record.get(4));
                    double savingAccount = Double.parseDouble(record.get(5));
                    String userRole = record.get(6);
                    String checkingIBAN = record.get(7);
                    String savingIBAN = record.get(8);
                    String cardChecking = record.get(9);
                    String cardSaving = record.get(10);
                    int phoneNumber = Integer.parseInt(record.get(11));
                    if (userName.equals(details.getUserName())) {
                        return List.of(
                                //0
                                String.valueOf(userID),
                                //1
                                userName,
                                //2
                                userPassword,
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
                                String.valueOf(phoneNumber)
                        );
                    }
                }
            }
            return new ArrayList<>();
        }
    }
    public static List<String> getUserIBAN(String Iban) throws Exception {
        List<List<String>> database = new ArrayList<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("database.txt"))) {
            String user;
            while ((user = bufferedReader.readLine()) != null) {
                List<String> databaseDetails = List.of(user.split(","));
                database.add(databaseDetails);
            }
            for (List<String> record : database) {
                if (record.size() > 10) {
                    int userID = Integer.parseInt(record.get(0));
                    String userName = record.get(1).concat(record.get(2));
                    String userPassword = record.get(3);
                    double checkingAccount = Double.parseDouble(record.get(4));
                    double savingAccount = Double.parseDouble(record.get(5));
                    String userRole = record.get(6);
                    String checkingIBAN = record.get(7);
                    String savingIBAN = record.get(8);
                    String cardChecking = record.get(9);
                    String cardSaving = record.get(10);
                    int phoneNumber = Integer.parseInt(record.get(11));
                    if (checkingIBAN.equals(Iban) || savingIBAN.equals(Iban)) {
                        return List.of(
                                //0
                                String.valueOf(userID),
                                //1
                                userName,
                                //2
                                userPassword,
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
                                String.valueOf(phoneNumber)
                        );
                    }
                }
            }
            return new ArrayList<>();
        }
    }
    public static List<String> getUserPhoneNumber(int phone) throws Exception {
        List<List<String>> database = new ArrayList<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("database.txt"))) {
            String user;
            while ((user = bufferedReader.readLine()) != null) {
                List<String> databaseDetails = List.of(user.split(","));
                database.add(databaseDetails);
            }
            for (List<String> record : database) {
                if (record.size() > 10) {
                    int userID = Integer.parseInt(record.get(0));
                    String userName = record.get(1).concat(record.get(2));
                    String userPassword = record.get(3);
                    double checkingAccount = Double.parseDouble(record.get(4));
                    double savingAccount = Double.parseDouble(record.get(5));
                    String userRole = record.get(6);
                    String checkingIBAN = record.get(7);
                    String savingIBAN = record.get(8);
                    String cardChecking = record.get(9);
                    String cardSaving = record.get(10);
                    int phoneNumber = Integer.parseInt(record.get(11));
                    if (phoneNumber == phone) {
                        return List.of(//0
                                String.valueOf(userID),
                                //1
                                userName,
                                //2
                                userPassword,
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
                                String.valueOf(phoneNumber)
                        );
                    }
                }
            }
            return new ArrayList<>();
        }
    }

}


