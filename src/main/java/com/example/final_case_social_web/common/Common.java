package com.example.final_case_social_web.common;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class Common {

    public static boolean checkRegex(String input, String regex) {
        if (StringUtils.isEmpty(input) || StringUtils.isEmpty(regex)) {
            return false;
        }
        try {
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(input);
            return matcher.matches();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void executionTime(double startTime, double elapsedTimeMillis) {
        log.info(Constants.MESSAGE_STRIKE_THROUGH);
        log.warn("Total execution time: " + (elapsedTimeMillis - startTime));
        log.warn("Total execution time(s): " + (elapsedTimeMillis - startTime) / 1000);
        log.info(Constants.MESSAGE_STRIKE_THROUGH);
    }

    public static String formatDate(String date) {
        if (StringUtils.isEmpty(date)) {
            return "";
        }
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            return formatter.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
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
