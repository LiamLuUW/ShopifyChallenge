package com.liam.shopifychallenge.util;


/**
 * Created by Liam on 2018-09-20.
 */

public class StringUtil {
    public static String[] parseStringsToList(String strings){
        //remove all space
        strings = strings.replaceAll("\\s+","");
        //split by ,
        return strings.split(",");
    }
}
