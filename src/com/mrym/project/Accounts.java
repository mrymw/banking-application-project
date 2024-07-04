package com.mrym.project;
import java.util.List;
public class Accounts {
    static double checkingBalance;
    static double savingBalance;
    static TypesOfCards typesOfCards;
    public static double checkingAccount(LoginDetails details) throws Exception {
        boolean cBalanceExists;
        List<String> checking = StoredDatabase.getUserDetails(details);
        if (checking.get(3) != null) {
            checkingBalance = Double.parseDouble(checking.get(3));
            typesOfCards = TypesOfCards.MASTERCARD;
            cBalanceExists = true;
        } else {
            System.out.println("there is no money/ deposit money");
        }
        return checkingBalance;
    }
    public static double savingAccount(LoginDetails details) throws Exception {
        boolean sBalanceExists;
        List<String> saving = StoredDatabase.getUserDetails(details);
        if (saving.get(4) != null) {
            savingBalance = Double.parseDouble(saving.get(4));
            typesOfCards = TypesOfCards.MASTERCARD;
            sBalanceExists = true;
        } else {
            System.out.println("there is no money/ deposit money");
        }
        return savingBalance;
    }



}
