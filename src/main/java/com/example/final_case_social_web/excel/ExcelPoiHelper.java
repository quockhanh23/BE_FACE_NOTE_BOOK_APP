package com.example.final_case_social_web.excel;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class ExcelPoiHelper<T> {

    private static int MAX_ROW_EMPTY_ALLOW = 30;

    public Map<Integer, T> readExcel(List<RequestFileExcel> files, String[] header) {
        Map<Integer, T> data = new HashMap<>();
        try {
            for (RequestFileExcel file : files) {
                if (file.getFileName().endsWith(".xls")) {
                    this.readHSSFWorkbook(file.getContent(), data, header);
                } else if (file.getFileName().endsWith(".xlsx") || file.getFileName().endsWith(".xlsm")) {
                    this.readXSSFWorkbook(file.getContent(), data, header);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    private Map<Integer, T> readHSSFWorkbook(byte[] bytes, Map<Integer, T> data, String[] header) {
        HSSFWorkbook workbook;
        try {
            workbook = new HSSFWorkbook(new ByteArrayInputStream(bytes));
            Sheet sheet = workbook.getSheetAt(0);
            int indexKey = data.size();
            ExcelComponent.checkHeaderInFileExcel(sheet, header.length);
            for (int i = sheet.getFirstRowNum() + 1; i <= sheet.getLastRowNum(); i++) {
                HSSFRow row = (HSSFRow) sheet.getRow(i);
                if (!ExcelComponent.checkEmptyRow(row)) {
                    data.put(indexKey++, convertRowToObject(row));
                } else {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    private Map<Integer, T> readXSSFWorkbook(byte[] bytes, Map<Integer, T> data, String[] header) {
        XSSFWorkbook workbook;
        try {
            InputStream stream = new ByteArrayInputStream(bytes);
            workbook = new XSSFWorkbook(stream);
            Sheet sheet = workbook.getSheetAt(0);
            int indexKey = data.size();
            ExcelComponent.checkHeaderInFileExcel(sheet, header.length);
            int countRowEmpty = 0;
            for (int i = sheet.getFirstRowNum() + 1; i <= sheet.getLastRowNum(); i++) {
                XSSFRow row = (XSSFRow) sheet.getRow(i);
                if (countRowEmpty >= MAX_ROW_EMPTY_ALLOW) {
                    break;
                }
                if (!ExcelComponent.checkEmptyRow(row)) {
                    data.put(indexKey++, convertRowToObject(row));
                    countRowEmpty = 0;
                } else {
                    countRowEmpty++;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return data;
    }

    protected abstract T convertRowToObject(Row row);
}
