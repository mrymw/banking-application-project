package com.mrym.project;
import java.time.LocalDate;
import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter User ID: ");
        int userID = input.nextInt();
        System.out.println("Enter User Name: ");
        String userName = input.next();
        System.out.println("Enter Password: ");
        String userPassword = input.next();
        LoginDetails details = new LoginDetails(userID, userName, userPassword);
        LoginAuthentication.authentication(details);
        FilterTransaction filterTransaction = new FilterTransaction();

        // Load transactions
        filterTransaction.filter(details);
        // Print transactions
        System.out.println("Today's Transactions: " + filterTransaction.filterToday());
        System.out.println("Yesterday's Transactions: " + filterTransaction.filterYesterday());
        System.out.println("Last 7 Days Transactions: " + filterTransaction.filterLast7Days());
        System.out.println("Last 30 Days Transactions: " + filterTransaction.filterLast30Days());
        System.out.println("Transactions from 2024-06-01 to 2024-06-30: " + filterTransaction.filterByDateRange(LocalDate.of(2024, 6, 1), LocalDate.of(2024, 6, 30)));
    }
}