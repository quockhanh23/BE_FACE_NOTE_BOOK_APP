package com.example.final_case_social_web.excel;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

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
