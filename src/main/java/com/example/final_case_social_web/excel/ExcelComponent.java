package com.example.final_case_social_web.excel;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
        return StringUtils.isEmpty(value) ? "" : value;
    }

    private void cellCheck(Cell cell, Object value) {
        cell.setCellValue(String.valueOf(value));
    }
}
