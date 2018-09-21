package com.liam.shopifychallenge.util;


/**
 * String utility
 */

public class StringUtil {
    public static String[] parseStringsToList(String strings){
        //remove all space
        strings = strings.replaceAll("\\s+","");
        //split by ,
        return strings.split(",");
    }
}
