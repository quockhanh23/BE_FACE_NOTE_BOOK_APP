package com.example.final_case_social_web.xml;

import org.apache.commons.lang3.StringUtils;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonBuilder {
    public static String initRequestRecovery(String startDate, String endDate, String startTime, String endTime, String clientId) {
        StringBuilder recMsg = new StringBuilder();
        recMsg.append("<RequestRecovery>");
        recMsg.append(String.format("<StartDate>%s</StartDate>", startDate));
        recMsg.append(String.format("<EndDate>%s</EndDate>", endDate));
        recMsg.append(String.format("<StartTime>%s</StartTime>", startTime));
        recMsg.append(String.format("<EndTime>%s</EndTime>", endTime));
        recMsg.append(String.format("<ClientId>%s</ClientId>", clientId));
        recMsg.append("</RequestRecovery>");
        return recMsg.toString();
    }

    public static List<String> generateDateRange(String startDateStr, String endDateStr) {
        if (null == startDateStr || null == endDateStr) return null;
        List<String> dateRange = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDate startDate = LocalDate.parse(startDateStr, formatter);
        LocalDate endDate = LocalDate.parse(endDateStr, formatter);

        while (!startDate.isAfter(endDate)) {
            dateRange.add(startDate.format(formatter));
            startDate = startDate.plusDays(1);
        }
        return dateRange;
    }

    public static long checkTheDistance(String issuedDate, String expiredDate) {
        if (StringUtils.isEmpty(issuedDate) || StringUtils.isEmpty(expiredDate)) return -999999999;
        try {
            return subtractDate(issuedDate, expiredDate);
        } catch (Exception e) {
            return -999999999;
        }
    }

    private static long subtractDate(String date1, String date2) {
        long diff = 0;
        LocalDate d1 = LocalDate.parse(date1, DateTimeFormatter.ISO_LOCAL_DATE);
        LocalDate d2 = LocalDate.parse(date2, DateTimeFormatter.ISO_LOCAL_DATE);
        Duration different = Duration.between(d1.atStartOfDay(), d2.atStartOfDay());
        diff = different.toDays();
        return diff;
    }

    public static Map<String, Object> buildRecRequestMessage(String startDate, String endDate,
                                                             String startTime, String endTime, String clientId) {
        Map<String, Object> objectMap = new HashMap<>();
        String startDateFormat = formatDate(startDate);
        String endDateFormat = formatDate(endDate);
        long day = checkTheDistance(startDateFormat, endDateFormat);
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("{");
        if (day > 7) {
            jsonBuilder.append("\"status\": \"nok\",");
            jsonBuilder.append("\"message\": \"Bursa only returns recovery message within 7 days\",");
            jsonBuilder.append("\"body\": []");
        } else if (day > 0) {
            jsonBuilder.append("\"status\": \"ok\",");
            jsonBuilder.append("\"body\": [");
            List<String> listDate = generateDateRange(startDateFormat, endDateFormat);
            if (listDate != null) {
                for (int i = 0; i < listDate.size(); i++) {
                    String date = listDate.get(i).replaceAll("-", "");
                    String data = initRequestRecovery(date, endDate, "1", "1", "1");
                    jsonBuilder.append("\"");
                    jsonBuilder.append(data);
                    jsonBuilder.append("\"");
                    if (i + 1 < listDate.size()) {
                        jsonBuilder.append(",");
                    }
                }
            }
            jsonBuilder.append("]");
        }
        jsonBuilder.append("}");
        objectMap.put("data", jsonBuilder.toString());
        return objectMap;
    }

    public static String formatDate(String dateStr) {
        Pattern pattern = Pattern.compile("^[0-9]*$");
        Matcher matcher = pattern.matcher(dateStr);
        if (!matcher.matches()) throw new RuntimeException("Error not number");
        if (dateStr.length() != 8) throw new RuntimeException("Error maxlength");
        try {
            String year = dateStr.substring(0, 4);
            String month = dateStr.substring(4, 6);
            String day = dateStr.substring(6);
            return year + "-" + month + "-" + day;
        } catch (Exception e) {
            throw new RuntimeException("Error");
        }
    }

    public static void main(String[] args) {
        String startDate = "êtrtre";
        String endDate = "20001230";
        Map<String, Object> map = buildRecRequestMessage(startDate, endDate, "1", "1", "1");
        System.out.println(map.get("data"));
    }
}
