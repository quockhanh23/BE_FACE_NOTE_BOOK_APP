package com.example.final_case_social_web.common;

import com.example.final_case_social_web.object.FieldsCheckWords;
import com.example.final_case_social_web.notification.ResponseNotification;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.Field;
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
        log.warn("Total execution time(ms): " + (elapsedTimeMillis - startTime));
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

    public static ResponseEntity<?> handlerWordsLanguage(Object ob) {
        FieldsCheckWords obTransfer = new FieldsCheckWords();
        BeanUtils.copyProperties(ob, obTransfer);
        try {
            Field[] fieldsOfFieldClass = FieldsCheckWords.class.getDeclaredFields();
            String[] dirtyWords = {"FUCK", "ĐỊT", "LỒN", "ĐMM", "ĐCM", "CU", "CẶC", "DÁI"};
            for (int i = 0; i < fieldsOfFieldClass.length; i++) {
                Field field = fieldsOfFieldClass[i];
                field.setAccessible(true);
                Object value = field.get(obTransfer);
                if (null == value) value = "";
                int check = handleWords(value.toString().toUpperCase(), dirtyWords);
                if (check == 1) {
                    return new ResponseEntity<>(new ResponseNotification(HttpStatus.BAD_REQUEST.toString(),
                            convertFieldName(field.getName())), HttpStatus.BAD_REQUEST);
                }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ResponseNotification(HttpStatus.INTERNAL_SERVER_ERROR.toString(),
                    "Có lỗi xảy ra: ", e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private static int handleWords(String value, String[] dirtyWords) {
        boolean containsDirtyWord = StringUtils.containsAny(value, dirtyWords);
        if (containsDirtyWord) return 1;
        return 0;
    }

    private static String convertFieldName(String field) {
        String content = " chứa những từ ngữ không phù hợp";
        if (field.equals(Constants.FieldsCheckWords.FIELD_USER_NAME)) {
            return "Tên đăng nhập" + content;
        }
        if (field.equals(Constants.FieldsCheckWords.FIELD_FULL_NAME)) {
            return "Tên đầy đủ" + content;
        }
        if (field.equals(Constants.FieldsCheckWords.FIELD_EMAIL)) {
            return "Email" + content;
        }
        if (field.equals(Constants.FieldsCheckWords.FIELD_ADDRESS)) {
            return "Địa chỉ" + content;
        }
        if (field.equals(Constants.FieldsCheckWords.FIELD_EDUCATION)) {
            return "Học vấn" + content;
        }
        if (field.equals(Constants.FieldsCheckWords.FIELD_FAVORITE)) {
            return "Sở thích" + content;
        }
        if (field.equals(Constants.FieldsCheckWords.FIELD_CONTENT)) {
            return "Nội dung" + content;
        }
        if (field.equals(Constants.FieldsCheckWords.FIELD_DESCRIPTION)) {
            return "Nội dung" + content;
        }
        if (field.equals(Constants.FieldsCheckWords.FIELD_GROUP_NAME)) {
            return "Tên nhóm" + content;
        }
        if (field.equals(Constants.FieldsCheckWords.FIELD_WORK)) {
            return "Công việc" + content;
        }
        return field + content;
    }

    public static String addEscapeOnSpecialCharactersWhenSearch(String input) {
        if (StringUtils.isEmpty(input)) return input;
        if (input.contains("%") && !input.contains("\\%")) {
            input = input.replace("%", "\\%");
        }
        if (input.contains("_") && !input.contains("\\_")) {
            input = input.replace("_", "\\_");
        }
        return input;
    }
}
