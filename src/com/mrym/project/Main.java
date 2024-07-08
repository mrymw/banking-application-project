package com.mrym.project;
import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner input = new Scanner(System.in);
        System.out.println("enter username: ");
        String userName = input.next();
        System.out.println("enter password: ");
        String userPassword = input.next();
        LoginDetails details = new LoginDetails(userName, userPassword);
        LoginAuthentication.authentication(details);

    }
}