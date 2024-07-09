package com.mrym.project;
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

    }
}