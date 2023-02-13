package com.example.final_case_social_web.common;

import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Common {

    public static boolean checkRegex(String input, String regex) {
        if (StringUtils.isEmpty(input) || StringUtils.isEmpty(regex)) {
            return false;
        }
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }

    public static void executionTime(double startTime, double elapsedTimeMillis) {
        System.out.println("Total execution time: " + (elapsedTimeMillis - startTime));
        System.out.println("Total execution time(s): " + (elapsedTimeMillis - startTime) / 1000);
    }

    public static String formatDate(String date) {
        if (StringUtils.isEmpty(date)) {
            return "";
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(date);
    }

    public static String formatToken(String authorization) {
        String tokenRequest;
        if (authorization.startsWith("Bearer ")) {
            tokenRequest = authorization.substring(7);
        } else {
            tokenRequest = authorization;
        }
        return tokenRequest;
    }
}
