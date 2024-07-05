package com.mrym.project;
import java.util.List;
import java.util.Scanner;
public class LoginAuthentication {
    private static final int maxAttempts = 3;
    static String userName;
    static String userPassword;
    static String userRole;
    public static void authentication(LoginDetails userDetails) throws Exception {
        Scanner input = new Scanner(System.in);
        List<String> details = StoredDatabase.getUserDetails(userDetails);
        if (details.isEmpty()) {
            System.out.println("user not found");
            return;
        }
        userName = details.get(1);
        userPassword = details.get(2);
        userRole = details.get(5);
        if (userName.equals(userDetails.getUserName()) && userPassword.equals(userDetails.getUserPassword())) {
            System.out.println("user found");
            if (userRole.equals("C")) {
                userCustomer(userDetails);
            } else if (userRole.equals("B")) {
                userBanker(userDetails);
            }
        } else {
            for (int i = 1; i <= maxAttempts; i++) {
                System.out.println("Incorrect Password, Try Again!");
                System.out.println("username: " + "attempt " + i);
                String attemptUsername = input.next();
                System.out.println("password: " + "attempt " + i);
                String attemptPassword = input.next();
                if (userName.equals(attemptUsername) && userPassword.equals(attemptPassword)) {
                    if (userRole.equals("C")) {
                        userCustomer(userDetails);
                    } else if (userRole.equals("B")) {
                        userBanker(userDetails);
                    }
                    return;
                }
            }
            System.out.println("Maximum number of attempts reached please try again after 1 min");
            try {
            Thread.sleep(60000);
            } catch (InterruptedException e) {
                System.out.println(e);
            }
        }
    }
    public static void userBanker (LoginDetails details) throws Exception {
        Banker banker = new Banker();
        Scanner input = new Scanner(System.in);
        System.out.println("Welcome Banker");
        System.out.println("What would you like to do today?");
        System.out.println("1. Add a new customer");
        System.out.println("2. Withdraw Money");
        System.out.println("3. Deposit Money");
        System.out.println("4. Transfer Money");
        int answer = input.nextInt();
        switch (answer) {
            case 1:
                System.out.println("Add Customer Details: ");
                System.out.println("Customer ID: ");
                int id = input.nextInt();
                System.out.println("Customer First Name: ");
                String first = input.next();
                System.out.println("Customer Last Name: ");
                String last = input.next();
                System.out.println("Customer Initial Password: ");
                String password = input.next();
                System.out.println("Customer Checking Account Balance: ");
                double checking = input.nextDouble();
                System.out.println("Customer Saving Account Balance: ");
                double saving = input.nextDouble();
                UserDetails userDetails = new UserDetails(id, first, last, password, checking, saving);
                try {
                    Banker.addCustomer(userDetails);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                break;
            case 2:
                System.out.println("Withdraw Amount: ");
                double amountWithdraw = input.nextDouble();
                banker.withdraw(details, amountWithdraw);
                break;
            case 3:
                System.out.println("Deposit Amount: ");
                double amountDeposit = input.nextDouble();
                banker.deposit(details, amountDeposit);
                break;
            case 4:
                System.out.println("Transfer Amount: ");
                double amountTransfer = input.nextDouble();
                banker.transfer(details, amountTransfer);
                break;
            default:
                System.out.println("Invalid Option");
        }
    }
    public static void userCustomer (LoginDetails details) throws Exception {
        Scanner input = new Scanner(System.in);
        System.out.println("Welcome Customer");
        System.out.println("What would you like to do today?");
        System.out.println("1. Withdraw Money");
        System.out.println("2. Deposit Money");
        System.out.println("3. Transfer Money");
        int answer = input.nextInt();
        Customer customer = new Customer();
        switch (answer) {
            case 1:
                System.out.println("Withdraw Amount: ");
                double amountWithdraw = input.nextDouble();
                customer.withdraw(details, amountWithdraw);
                break;
            case 2:
                System.out.println("Deposit Amount: ");
                double amountDeposit = input.nextDouble();
                customer.deposit(details, amountDeposit);
                break;
            case 3:
                System.out.println("Transfer Amount: ");
                double amountTransfer = input.nextDouble();
                customer.transfer(details, amountTransfer);
                break;
            default:
                System.out.println("Invalid Option");
        }
    }
}
