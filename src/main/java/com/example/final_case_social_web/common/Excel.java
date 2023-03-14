package com.example.final_case_social_web.common;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.util.List;

public class Excel {

    public byte[] exportDataToExcel(List<Object> objects) {
        byte[] bytes = new byte[0];
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            if (!CollectionUtils.isEmpty(objects)) {
                XSSFWorkbook xssfWorkbook = createDataSheet(objects);
                xssfWorkbook.write(outputStream);
                bytes = outputStream.toByteArray();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bytes;
    }

    public XSSFWorkbook createDataSheet(List<Object> objects) throws IllegalAccessException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet(Constants.Excel.USER_SHEET);
        int rowCount = 0;
        Row headerRow = sheet.createRow(rowCount);
        for (int col = 0; col < Constants.Excel.EXCEL_USER_HEADER.length; col++) {
            createCellStyle(workbook, headerRow, col, Constants.Excel.EXCEL_USER_HEADER[col], Constants.Excel.HEADER);
        }
        Field[] fieldsOfFieldClass = Object.class.getDeclaredFields();
        for (Object entity : objects) {
            Row row = sheet.createRow(++rowCount);
            int columnCount = 0;
            for (Field field : fieldsOfFieldClass) {
                if (field.getName().equals("Id"))
                    continue;
                Object value = field.get(entity);
                createCellStyle(workbook, row, columnCount++, value, Constants.Excel.CONTENT);
            }
        }
        sheet.autoSizeColumn(30);
        return workbook;
    }

    public void createCellStyle(XSSFWorkbook workbook, Row row, int columnCount, Object value, String type) {
        Cell cell = row.createCell(columnCount);
        cellCheck(cell, value);
        XSSFCellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        if (Constants.Excel.HEADER.equals(type)) {
            font.setFontHeight(15);
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        }
        if (Constants.Excel.CONTENT.equals(type)) {
            font.setFontHeight(13);
        }
        style.setFont(font);
        setDefaultStyle(style);
        cell.setCellStyle(style);
    }

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
