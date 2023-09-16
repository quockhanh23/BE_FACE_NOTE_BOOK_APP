package com.example.final_case_social_web.excel;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelHelper {

    public CellStyle headerCellStyle(SXSSFWorkbook workbook) {
        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
        headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerCellStyle.setAlignment(HorizontalAlignment.CENTER);
        headerCellStyle.setBorderRight(BorderStyle.THIN);
        headerCellStyle.setBorderBottom(BorderStyle.THIN);
        return headerCellStyle;
    }

    public void writeHeader(SXSSFWorkbook workbook, SXSSFRow headerRow, XSSFSheet xssfSheet,
                            int dataSize, boolean dropDownList, boolean commentHeader) {
        for (int i = 0; i < ExcelTest.headerNames.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(ExcelTest.headerNames[i]);
            cell.setCellStyle(headerCellStyle(workbook));
            if (Boolean.TRUE.equals(dropDownList)) {
                writeValueDropDownList(xssfSheet, ExcelTest.headerNames[i], dataSize, i);
            }
            if (Boolean.TRUE.equals(commentHeader)) {
                writeValueComment(xssfSheet, cell, ExcelTest.headerNames[i]);
            }
        }
    }

    private void writeValueDropDownList(XSSFSheet sheet, String value, int dataSite, int indexColumnAddDropList) {
        Map<String, String[]> dropDownList = new HashMap<>();
        String[] test = {"1", "2", "3"};
        dropDownList.put("First Name", test);
        for (Map.Entry<String, String[]> entry : dropDownList.entrySet()) {
            if (entry.getKey() != null && entry.getKey().equals(value)) {
                String[] list = entry.getValue();
                if (list == null) continue;
                XSSFDataValidationHelper validationHelper = new XSSFDataValidationHelper(sheet);
                DataValidation dataValidation = validationHelper.createValidation(validationHelper.createExplicitListConstraint(list),
                        new CellRangeAddressList(1, dataSite, indexColumnAddDropList, indexColumnAddDropList));
                dataValidation.setShowErrorBox(false);
                dataValidation.setSuppressDropDownArrow(true);
                sheet.addValidationData(dataValidation);
            }
        }
    }

    private void writeValueComment(XSSFSheet sheet, Cell cell, String value) {
        Map<String, String> commentHeader = new HashMap<>();
        commentHeader.put("First Name", "Test");
        for (Map.Entry<String, String> entry : commentHeader.entrySet()) {
            if (entry.getKey() != null && entry.getKey().equals(value)) {
                XSSFDrawing drawing = sheet.createDrawingPatriarch();
                XSSFComment comment = drawing.createCellComment(new XSSFClientAnchor());
                comment.setString(new XSSFRichTextString(entry.getValue()));
                comment.setAuthor("User");
                cell.setCellComment(comment);
            }
        }
    }

    public CellStyle dataCellStyle(SXSSFWorkbook workbook) {
        CellStyle dataCellStyle = workbook.createCellStyle();
        dataCellStyle.setAlignment(HorizontalAlignment.CENTER);
        dataCellStyle.setWrapText(true);
        return dataCellStyle;
    }

    public void writeData(List<String[]> dataRows, SXSSFSheet sheet, SXSSFWorkbook workbook) {
        int rowNum = 1;
        for (String[] rowData : dataRows) {
            SXSSFRow row = sheet.createRow(rowNum++);
            int columnNum = 0;
            for (String field : rowData) {
                Cell cell = row.createCell(columnNum++);
                cell.setCellValue(field);
                cell.setCellStyle(dataCellStyle(workbook));
            }
        }
    }
}
