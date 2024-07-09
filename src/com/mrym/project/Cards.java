package com.mrym.project;
import java.util.HashMap;
import java.util.Map;
public class Cards {
    static Map<String, Map<String, Double>> cardLimits = new HashMap<>();
    static {
        // Initialize card limits
        mastercard();
        mastercardTitanium();
        mastercardPlatinum();
    }
    public static void mastercard() {
        Map<String, Double> mastercardLimits = new HashMap<>();
        mastercardLimits.put("withdraw", 5000.0);
        mastercardLimits.put("transfer", 10000.0);
        mastercardLimits.put("transferOwnAccount", 20000.0);
        mastercardLimits.put("deposit", 100000.0);
        cardLimits.put("MASTERCARD", mastercardLimits);
    }
    public static void mastercardTitanium() {
        Map<String, Double> mastercardTitaniumLimits = new HashMap<>();
        mastercardTitaniumLimits.put("withdraw", 10000.0);
        mastercardTitaniumLimits.put("transfer", 20000.0);
        mastercardTitaniumLimits.put("transferOwnAccount", 40000.0);
        mastercardTitaniumLimits.put("deposit", 100000.0);
       cardLimits.put("MASTERCARDTITANIUM", mastercardTitaniumLimits);
    }
    public static void mastercardPlatinum() {
        Map<String, Double> mastercardPlatinumLimits = new HashMap<>();
        mastercardPlatinumLimits.put("withdraw", 20000.0);
        mastercardPlatinumLimits.put("transfer", 40000.0);
        mastercardPlatinumLimits.put("transferOwnAccount", 80000.0);
        mastercardPlatinumLimits.put("deposit", 100000.0);
        cardLimits.put("MASTERCARDPLATINUM", mastercardPlatinumLimits);
    }
    public static Map<String, Double> getLimits(String cardType) {
        return cardLimits.get(cardType);
    }

}
