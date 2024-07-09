package com.mrym.project;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FilterTransaction {
    private List<Transaction> userTransactions = new ArrayList<>();

    public List<Transaction> filter(LoginDetails userDetails) throws Exception {
        List<String> details = StoredDatabase.getUserDetails(userDetails);
        if (details.isEmpty()) {
            System.out.println("User not found");
            return userTransactions;
        }

        String userName = details.get(1);
        int userID = Integer.parseInt(details.get(0));
        String fileName = userName + userID + ".txt";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd, HH:mm:ss.SSSSSS");

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] data = line.split(", ");
                if (data.length == 6) {
                    try {
                        LocalDateTime dateTime = LocalDateTime.parse(data[0] + ", " + data[1], formatter);
                        String transactionType = data[2];
                        double amount = Double.parseDouble(data[3]);
                        double balanceAfter = Double.parseDouble(data[4]);
                        String accountType = data[5];
                        Transaction transaction = new Transaction(dateTime, transactionType, amount, balanceAfter, accountType);
                        userTransactions.add(transaction);
                    } catch (Exception e) {
                        System.out.println("Error parsing transaction: " + line);
                    }
                } else {
                    System.out.println("Invalid transaction format: " + line);
                }
            }
        }

        return userTransactions;
    }

    public List<Transaction> filterToday() {
        LocalDate todayDate = LocalDate.now();
        return userTransactions.stream()
                .filter(transaction -> transaction.getDate().toLocalDate().isEqual(todayDate))
                .collect(Collectors.toList());
    }

    public List<Transaction> filterYesterday() {
        LocalDate yesterdayDate = LocalDate.now().minusDays(1);
        return userTransactions.stream()
                .filter(transaction -> transaction.getDate().toLocalDate().isEqual(yesterdayDate))
                .collect(Collectors.toList());
    }

    public List<Transaction> filterLast7Days() {
        LocalDate sevenDaysAgo = LocalDate.now().minusDays(7);
        return userTransactions.stream()
                .filter(transaction -> !transaction.getDate().toLocalDate().isBefore(sevenDaysAgo))
                .collect(Collectors.toList());
    }

    public List<Transaction> filterLast30Days() {
        LocalDate thirtyDaysAgo = LocalDate.now().minusDays(30);
        return userTransactions.stream()
                .filter(transaction -> !transaction.getDate().toLocalDate().isBefore(thirtyDaysAgo))
                .collect(Collectors.toList());
    }

    public List<Transaction> filterByDateRange(LocalDate startDate, LocalDate endDate) {
        return userTransactions.stream()
                .filter(transaction -> !transaction.getDate().toLocalDate().isBefore(startDate) && !transaction.getDate().toLocalDate().isAfter(endDate))
                .collect(Collectors.toList());
    }

}
