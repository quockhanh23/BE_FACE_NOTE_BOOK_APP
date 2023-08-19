package com.example.final_case_social_web.excel;

import com.example.final_case_social_web.common.Constants;
import com.example.final_case_social_web.model.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class ExcelComponent {

    public static void checkHeaderInFileExcel(Sheet sheet, int headerLength) {
        List<String> stringList = new ArrayList<>();
        for (int i = 0; i < sheet.getRow(0).getPhysicalNumberOfCells(); i++) {
            if (!StringUtils.isEmpty(sheet.getRow(0).getCell(i).toString().trim())) {
                stringList.add(sheet.getRow(0).getCell(i).toString());
            }
        }
        if (stringList.size() != headerLength) {
        }
    }

    public static boolean checkEmptyRow(Row row) {
        try {
            StringBuilder sb = new StringBuilder();
            DataFormatter dataFormatter = new DataFormatter();
            if (Objects.isNull(row)) {
                return true;
            }
            for (int j = 0; j < row.getLastCellNum(); j++) {
                Cell cell = row.getCell(j);
                sb.append(nullToBlank(dataFormatter.formatCellValue(cell).trim()));
            }
            return StringUtils.isBlank(sb.toString().replaceAll(" ", ""));
        } catch (Exception e) {
            return false;
        }
    }

    public static String nullToBlank(String value) {
        if (StringUtils.isEmpty(value)) {
            return "";
        } else {
            return value;
        }
    }

//    public XSSFWorkbook createDataSheet(List<User> users) throws IllegalAccessException {
//        XSSFWorkbook workbook = new XSSFWorkbook();
//        XSSFSheet sheet = workbook.createSheet(Constants.Excel.USER_SHEET);
//        int rowCount = 0;
//        Row headerRow = sheet.createRow(rowCount);
//        String[] values = new String[]{"Red", "Green", "Blue"};
//        DataValidationHelper validationHelper = new XSSFDataValidationHelper((XSSFSheet) sheet);
//        DataValidation dataValidation = validationHelper.createValidation(
//                validationHelper.createExplicitListConstraint(values),
//                new CellRangeAddressList(0, users.size(), 10, 10));
//        dataValidation.setShowErrorBox(false);
//        dataValidation.setSuppressDropDownArrow(true);
//        sheet.addValidationData(dataValidation);
//
//        for (int col = 0; col < Constants.Excel.EXCEL_USER_HEADER.length; col++) {
//            createCellStyle(workbook, headerRow, col, Constants.Excel.EXCEL_USER_HEADER[col], Constants.Excel.HEADER);
//        }
//        Field[] fieldsOfFieldClass = Object.class.getDeclaredFields();
//        for (Object entity : users) {
//            Row row = sheet.createRow(++rowCount);
//            int columnCount = 0;
//            for (Field field : fieldsOfFieldClass) {
//                if (field.getName().equals("Id"))
//                    continue;
//                Object value = field.get(entity);
//                createCellStyle(workbook, row, columnCount++, value, Constants.Excel.CONTENT);
//            }
//        }
//        sheet.autoSizeColumn(30);
//        return workbook;
//    }

    private static void createCellHeader(XSSFWorkbook workbook, XSSFSheet sheet, Row row, int columnCount, Object value) {
        Cell cell = row.createCell(columnCount);
        cell.setCellValue((String) value);
        XSSFCellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(16);
        style.setFont(font);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        cell.setCellStyle(style);
        sheet.autoSizeColumn(columnCount);
    }

    private static void createCell(XSSFWorkbook workbook, XSSFSheet sheet, Row row, int columnCount, Object value) {
        Cell cell = row.createCell(columnCount);

        XSSFCellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);


        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else if (value instanceof Long) {
            cell.setCellValue((Long) value);
        } else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
        sheet.autoSizeColumn(columnCount);
    }

//    public void createCellStyle(XSSFWorkbook workbook, Row row, int columnCount, Object value, String type) {
//        Cell cell = row.createCell(columnCount);
//        cellCheck(cell, value);
//        XSSFCellStyle style = workbook.createCellStyle();
//        XSSFFont font = workbook.createFont();
//        if (Constants.Excel.HEADER.equals(type)) {
//            font.setFontHeight(15);
//            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
//        }
//        if (Constants.Excel.CONTENT.equals(type)) {
//            font.setFontHeight(13);
//        }
//        style.setFont(font);
//        setDefaultStyle(style);
//        cell.setCellStyle(style);
//    }

    private void setDefaultStyle(XSSFCellStyle style) {
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
    }

    private void cellCheck(Cell cell, Object value) {
        if (value instanceof Long) {
            cell.setCellValue((Long) value);
        } else if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else if (value instanceof Double) {
            cell.setCellValue((Double) value);
        } else {
            cell.setCellValue(String.valueOf(value));
        }
    }
}
