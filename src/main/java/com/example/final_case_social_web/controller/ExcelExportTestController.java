package com.example.final_case_social_web.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.metadata.data.DataFormatData;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.alibaba.excel.write.style.column.SimpleColumnWidthStyleStrategy;
import com.example.final_case_social_web.common.Common;
import com.example.final_case_social_web.excel.ExcelTest;
import com.example.final_case_social_web.model.TestData;
import com.example.final_case_social_web.repository.TestDataRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@RestController
@PropertySource("classpath:application.properties")
@CrossOrigin("*")
@RequestMapping("/api/test")
@Slf4j
public class ExcelExportTestController {

    @Autowired
    private TestDataRepository testDataRepository;

    private void setResponse(HttpServletResponse response, String type) {
        String fileName = System.currentTimeMillis() + type;
        response.setHeader("Content-disposition", "attachment; filename=" + fileName + ".xlsx");
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    }

    @GetMapping("/export-excel")
    public void exportExcel(HttpServletResponse response) throws IOException {
        final double startTime = System.currentTimeMillis();
        List<String[]> dataRows = testDataRepository.findAll()
                .stream().map(data -> new String[]{
                        data.getFirstName(), data.getLastName(), data.getAddress(), data.getEducation(),
                        data.getPhone(), data.getCountry(), data.getReligion(), data.getLicense(),
                        data.getVaccination(), data.getPassport()
                }).collect(Collectors.toList());
        final double elapsedTimeMillis = System.currentTimeMillis();
        Common.executionTime(startTime, elapsedTimeMillis);
        // Tạo workbook mới
        SXSSFWorkbook workbook = new SXSSFWorkbook();
        // Tạo sheet mới
        SXSSFSheet sheet = workbook.createSheet("Danh sách");
        // Ép kiểu để sử dụng XSSFDataValidationHelper
        XSSFSheet xssfSheet = workbook.getXSSFWorkbook().getSheet(sheet.getSheetName());
        // Tạo header row
        SXSSFRow headerRow = sheet.createRow(0);

        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
        headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerCellStyle.setAlignment(HorizontalAlignment.CENTER);
        headerCellStyle.setBorderRight(BorderStyle.THIN);
        headerCellStyle.setBorderBottom(BorderStyle.THIN);
        for (int i = 0; i < ExcelTest.headerNames.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(ExcelTest.headerNames[i]);
            cell.setCellStyle(headerCellStyle);
            writeValueDropDownList(xssfSheet, ExcelTest.headerNames[i], dataRows.size(), i);
            writeValueComment(xssfSheet, cell, ExcelTest.headerNames[i]);
        }
        sheet.setDefaultColumnWidth(40);
        CellStyle dataCellStyle = workbook.createCellStyle();
        dataCellStyle.setAlignment(HorizontalAlignment.CENTER);
        dataCellStyle.setWrapText(true);
        int rowNum = 1;
        for (String[] rowData : dataRows) {
            SXSSFRow row = sheet.createRow(rowNum++);
            int columnNum = 0;
            for (String field : rowData) {
                Cell cell = row.createCell(columnNum++);
                cell.setCellValue(field);
                cell.setCellStyle(dataCellStyle);
            }
        }

        setResponse(response, "SXSSFWorkbook");
        // Ghi workbook vào OutputStream của response
        OutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
        final double elapsedTimeMillisEnd = System.currentTimeMillis();
        Common.executionTime(startTime, elapsedTimeMillisEnd);
    }

    @GetMapping("/export-excel-2")
    public void exportExcel2(HttpServletResponse response) throws IOException {
        final double startTime = System.currentTimeMillis();
        List<String[]> dataRows = testDataRepository.findAll()
                .stream().map(data -> new String[]{
                        data.getFirstName(), data.getLastName(), data.getAddress(), data.getEducation(),
                        data.getPhone(), data.getCountry(), data.getReligion(), data.getLicense(),
                        data.getVaccination(), data.getPassport()
                }).collect(Collectors.toList());
        final double elapsedTimeMillis = System.currentTimeMillis();
        Common.executionTime(startTime, elapsedTimeMillis);
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Danh sách");
        XSSFRow headerRow = sheet.createRow(0);
        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
        headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerCellStyle.setAlignment(HorizontalAlignment.CENTER);
        headerCellStyle.setBorderRight(BorderStyle.THIN);
        headerCellStyle.setBorderBottom(BorderStyle.THIN);
        for (int i = 0; i < ExcelTest.headerNames.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(ExcelTest.headerNames[i]);
            cell.setCellStyle(headerCellStyle);
            writeValueDropDownList(sheet, ExcelTest.headerNames[i], dataRows.size(), i);
            writeValueComment(sheet, cell, ExcelTest.headerNames[i]);
        }
        sheet.setDefaultColumnWidth(40);
        CellStyle dataCellStyle = workbook.createCellStyle();
        dataCellStyle.setAlignment(HorizontalAlignment.CENTER);
        dataCellStyle.setWrapText(true);
        int rowNum = 1;
        for (String[] rowData : dataRows) {
            XSSFRow row = sheet.createRow(rowNum++);
            int columnNum = 0;
            for (String field : rowData) {
                Cell cell = row.createCell(columnNum++);
                cell.setCellValue(field);
                cell.setCellStyle(dataCellStyle);
            }
        }

        setResponse(response, "XSSFWorkbook");
        OutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
        final double elapsedTimeMillisEnd = System.currentTimeMillis();
        Common.executionTime(startTime, elapsedTimeMillisEnd);
    }

    @GetMapping("/export")
    public void exportExcelNew(HttpServletResponse response) throws IOException {
        final double startTime = System.currentTimeMillis();
        List<TestData> testData = testDataRepository.findAll();
        final double elapsedTimeMillis = System.currentTimeMillis();
        Common.executionTime(startTime, elapsedTimeMillis);
        setResponse(response, "EasyExcel");
        WriteFont font = new WriteFont();
        font.setFontName("Arial"); // Đổi font chữ thành Arial
        WriteCellStyle headStyle = new WriteCellStyle();
        headStyle.setWrapped(true);
        headStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headStyle.setFillPatternType(FillPatternType.SOLID_FOREGROUND);
        headStyle.setWriteFont(font);

        WriteCellStyle contentStyle = new WriteCellStyle();
        contentStyle.setWrapped(true);
        contentStyle.setWriteFont(font);
        contentStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        DataFormatData dataFormatData = new DataFormatData();
        dataFormatData.setFormat("@");
        headStyle.setDataFormatData(dataFormatData);
        contentStyle.setDataFormatData(dataFormatData);
        // Tạo một đối tượng HorizontalCellStyleStrategy để định dạng kiểu chữ cho header và content
        HorizontalCellStyleStrategy styleStrategy = new HorizontalCellStyleStrategy(headStyle, contentStyle);
        SimpleColumnWidthStyleStrategy simpleColumnWidthStyleStrategy = new SimpleColumnWidthStyleStrategy(40);
        EasyExcel.write(response.getOutputStream(), TestData.class)
                .registerWriteHandler(styleStrategy)
                .registerWriteHandler(simpleColumnWidthStyleStrategy).sheet("Test Data").doWrite(testData);
        final double elapsedTimeMillisEnd = System.currentTimeMillis();
        Common.executionTime(startTime, elapsedTimeMillisEnd);
    }

    @PostMapping("/import")
    public ResponseEntity<?> importExcel(@RequestParam("file") MultipartFile file) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
        XSSFSheet worksheet = workbook.getSheetAt(0);
        List<TestData> testData = new ArrayList<>();
        for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {
            XSSFRow row = worksheet.getRow(i);
            if (Objects.isNull(row)) continue;
            String firstName = row.getCell(0).getStringCellValue();
            String lastName = row.getCell(1).getStringCellValue();
            String address = row.getCell(2).getStringCellValue();
            String education = row.getCell(3).getStringCellValue();
            String phone = row.getCell(4).getStringCellValue();
            String country = row.getCell(5).getStringCellValue();
            String religion = row.getCell(6).getStringCellValue();
            String license = row.getCell(7).getStringCellValue();
            TestData data = new TestData(firstName, lastName, address, education, phone, country, religion, license);
            testData.add(data);
        }
        if (!CollectionUtils.isEmpty(testData)) {
            testDataRepository.saveAll(testData);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/import-2")
    public ResponseEntity<?> importExcel2(@RequestParam("file") MultipartFile file) throws IOException {
        Workbook workbook = WorkbookFactory.create(file.getInputStream());
        Sheet worksheet = workbook.getSheetAt(0);
        List<TestData> testData = StreamSupport.stream(worksheet.spliterator(), false)
                .skip(1).map(row -> {
                    String firstName = row.getCell(0).getStringCellValue();
                    String lastName = row.getCell(1).getStringCellValue();
                    String address = row.getCell(2).getStringCellValue();
                    String education = row.getCell(3).getStringCellValue();
                    String phone = row.getCell(4).getStringCellValue();
                    String country = row.getCell(5).getStringCellValue();
                    String religion = row.getCell(6).getStringCellValue();
                    String license = row.getCell(7).getStringCellValue();
                    return new TestData(firstName, lastName, address, education, phone, country, religion, license);
                })
                .collect(Collectors.toList());
        testDataRepository.saveAll(testData);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/create-data-test")
    public ResponseEntity<?> createDataTest() {
        List<TestData> list = Stream.generate(TestData::new)
                .limit(1000)
                .collect(Collectors.toList());
        testDataRepository.saveAll(list);
        return new ResponseEntity<>(HttpStatus.OK);
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
}
