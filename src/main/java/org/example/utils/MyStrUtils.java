package org.example.utils;

public class MyStrUtils {

    public static String delBlank(String str){
        return str.replaceAll("[　+| \\s]+", " ");
    }

}
