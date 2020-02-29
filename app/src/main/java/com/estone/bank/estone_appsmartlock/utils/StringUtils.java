package com.estone.bank.estone_appsmartlock.utils;

public class StringUtils {

    public static boolean contrast(String str1, String str2) {
        if(str1 == null && str2 != null)
            return false;
        else if(str1 != null && str2 == null)
            return false;
        else if(str1 == null && str2 == null)
            return true;
        else return str1.equals(str2);
    }

    public static boolean isStringNULL(String str) {
        return str == null || str.equals("") || str.equals("null");
    }
}
