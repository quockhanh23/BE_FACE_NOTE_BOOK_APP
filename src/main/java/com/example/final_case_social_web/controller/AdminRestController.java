package com.example.final_case_social_web.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.example.final_case_social_web.common.Common;
import com.example.final_case_social_web.common.Constants;
import com.example.final_case_social_web.common.MessageResponse;
import com.example.final_case_social_web.dto.GroupPostDTO;
import com.example.final_case_social_web.dto.PostCheck;
import com.example.final_case_social_web.dto.UserDTO;
import com.example.final_case_social_web.excel.ExcelTest;
import com.example.final_case_social_web.model.*;
import com.example.final_case_social_web.notification.ResponseNotification;
import com.example.final_case_social_web.repository.GroupPostRepository;
import com.example.final_case_social_web.repository.ReportRepository;
import com.example.final_case_social_web.repository.TestDataRepository;
import com.example.final_case_social_web.repository.TheGroupRepository;
import com.example.final_case_social_web.service.PostService;
import com.example.final_case_social_web.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.jasper.tagplugins.jstl.core.If;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@RestController
@PropertySource("classpath:application.properties")
@CrossOrigin("*")
@RequestMapping("/api/admins")
@Slf4j
public class AdminRestController {
    @Autowired
    private UserService userService;
    @Autowired
    private PostService postService;
    @Autowired
    private TheGroupRepository theGroupRepository;
    @Autowired
    private GroupPostRepository groupPostRepository;
    @Autowired
    private ReportRepository reportRepository;
    @Autowired
    private TestDataRepository testDataRepository;

    // Xem tất cả user
    @GetMapping("/adminAction")
    public ResponseEntity<?> adminAction(@RequestParam Long idAdmin,
                                         @RequestParam String type,
                                         @RequestHeader("Authorization") String authorization) {
        if (!userService.errorToken(authorization, idAdmin)) {
            return new ResponseEntity<>(new ResponseNotification(HttpStatus.UNAUTHORIZED.toString(),
                    Constants.TOKEN, Constants.TOKEN + " " + MessageResponse.NO_VALID.toLowerCase()),
                    HttpStatus.UNAUTHORIZED);
        }
        if (Objects.isNull(userService.checkAdmin(idAdmin))) {
            return new ResponseEntity<>(ResponseNotification.
                    responseMessage(Constants.IdCheck.ID_ADMIN, idAdmin), HttpStatus.UNAUTHORIZED);
        }
        if ("user".equals(type)) {
            List<User> users = userService.findAllRoleUser();
            List<UserDTO> userDTOList = userService.copyListDTO(users);
            return new ResponseEntity<>(userDTOList, HttpStatus.OK);
        }
        if ("post".equals(type)) {
            Iterable<Post2> post2List = postService.findAll();
            List<Post2> list = (List<Post2>) post2List;
            List<PostCheck> postChecks = new ArrayList<>();
            list.forEach(post2 -> {
                PostCheck postCheck = new PostCheck();
                BeanUtils.copyProperties(post2, postCheck);
                postCheck.setIdUser(post2.getUser().getId());
                postCheck.setFullName(post2.getUser().getFullName());
                postChecks.add(postCheck);
            });
            postChecks.sort((p1, p2) -> (p2.getCreateAt().compareTo(p1.getCreateAt())));
            return new ResponseEntity<>(postChecks, HttpStatus.OK);
        }
        if ("group".equals(type)) {
            List<TheGroup> theGroupList = theGroupRepository.findAll();
            if (CollectionUtils.isEmpty(theGroupList)) {
                theGroupList = new ArrayList<>();
            }
            return new ResponseEntity<>(theGroupList, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/getAllGroupPost")
    public ResponseEntity<?> getAllGroupPost(@RequestParam Long idGroup, @RequestParam Long idAdmin,
                                             @RequestHeader("Authorization") String authorization) {
        if (!userService.errorToken(authorization, idAdmin)) {
            return new ResponseEntity<>(new ResponseNotification(HttpStatus.UNAUTHORIZED.toString(),
                    Constants.TOKEN, Constants.TOKEN + " " + MessageResponse.NO_VALID.toLowerCase()),
                    HttpStatus.UNAUTHORIZED);
        }
        if (userService.checkAdmin(idAdmin) == null) {
            return new ResponseEntity<>(ResponseNotification.
                    responseMessage(Constants.IdCheck.ID_ADMIN, idAdmin), HttpStatus.UNAUTHORIZED);
        }
        List<GroupPost> list = groupPostRepository.findAllByTheGroupId(idGroup);
        List<GroupPostDTO> groupPostDTOList = new ArrayList<>();
        for (GroupPost groupPost : list) {
            GroupPostDTO groupPostDTO = new GroupPostDTO();
            BeanUtils.copyProperties(groupPostDTO, groupPost);
            groupPostDTO.setGroupName(groupPost.getTheGroup().getGroupName());
            groupPostDTO.setUserName(groupPost.getUser().getUsername());
            groupPostDTOList.add(groupPostDTO);
        }
        return new ResponseEntity<>(groupPostDTOList, HttpStatus.OK);
    }

    @GetMapping("/findById")
    public ResponseEntity<?> findById(@RequestParam Long idUser) {
        Optional<User> userOptional = userService.findById(idUser);
        if (!userOptional.isPresent()) {
            return new ResponseEntity<>(ResponseNotification.
                    responseMessage(Constants.IdCheck.ID_USER, idUser), HttpStatus.NOT_FOUND);
        }
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(userOptional.get(), userDTO);
        List<ReportViolations> violations = reportRepository
                .findAllByIdViolateAndType(userOptional.get().getId(), Constants.REPOST_TYPE_USER);
        userDTO.setNumberRepost(violations.size());
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    // Cấm user, kích hoạt tài khoản
    @DeleteMapping("/actionUser")
    public ResponseEntity<?> actionUser(@RequestParam Long idAdmin,
                                        @RequestParam Long idUser,
                                        @RequestParam String type,
                                        @RequestHeader("Authorization") String authorization) {
        if (!userService.errorToken(authorization, idAdmin)) {
            return new ResponseEntity<>(new ResponseNotification(HttpStatus.UNAUTHORIZED.toString(),
                    Constants.TOKEN, Constants.TOKEN + " " + MessageResponse.NO_VALID.toLowerCase()),
                    HttpStatus.UNAUTHORIZED);
        }
        if (userService.checkAdmin(idAdmin) == null) {
            return new ResponseEntity<>(ResponseNotification.
                    responseMessage(Constants.IdCheck.ID_ADMIN, idAdmin), HttpStatus.UNAUTHORIZED);
        }
        Optional<User> optionalUser = userService.findById(idUser);
        if (!optionalUser.isPresent()) {
            return new ResponseEntity<>(ResponseNotification.
                    responseMessage(Constants.IdCheck.ID_USER, idUser), HttpStatus.NOT_FOUND);
        }
        if (userService.checkAdmin(idAdmin).toString().substring(17, 27).equals(Constants.Roles.ROLE_ADMIN)) {
            if ("baned".equals(type)) {
                optionalUser.get().setStatus(Constants.STATUS_BANED);
            }
            if ("active".equals(type)) {
                optionalUser.get().setStatus(Constants.STATUS_ACTIVE);
            }
            userService.save(optionalUser.get());
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(new ResponseNotification(HttpStatus.BAD_REQUEST.toString(),
                MessageResponse.NO_VALID, MessageResponse.DESCRIPTION),
                HttpStatus.BAD_REQUEST);
    }

    // Xoá bài viết trong database, khóa bài viết
    @DeleteMapping("/actionPost")
    public ResponseEntity<?> actionPost(@RequestParam Long idAdmin,
                                        @RequestParam Long idPost,
                                        @RequestParam String type,
                                        @RequestHeader("Authorization") String authorization) {
        if (!userService.errorToken(authorization, idAdmin)) {
            return new ResponseEntity<>(new ResponseNotification(HttpStatus.UNAUTHORIZED.toString(),
                    Constants.TOKEN, Constants.TOKEN + " " + MessageResponse.NO_VALID.toLowerCase()),
                    HttpStatus.UNAUTHORIZED);
        }
        if (userService.checkAdmin(idAdmin) == null) {
            return new ResponseEntity<>(ResponseNotification.
                    responseMessage(Constants.IdCheck.ID_ADMIN, idAdmin), HttpStatus.UNAUTHORIZED);
        }
        Optional<Post2> postOptional = postService.findById(idPost);
        if (!postOptional.isPresent()) {
            return new ResponseEntity<>(ResponseNotification.
                    responseMessage(Constants.IdCheck.ID_POST, idPost), HttpStatus.NOT_FOUND);
        }
        if (userService.checkAdmin(idAdmin).toString().substring(17, 27).equals(Constants.Roles.ROLE_ADMIN)) {
            if ("delete".equals(type)) {
                postService.delete(postOptional.get());
            }
            if ("lock".equals(type)) {
                postOptional.get().setStatus(Constants.STATUS_PRIVATE);
                postService.save(postOptional.get());
            }
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(new ResponseNotification(HttpStatus.BAD_REQUEST.toString(),
                MessageResponse.NO_VALID, MessageResponse.DESCRIPTION),
                HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/searchAllPeople")
    public ResponseEntity<?> searchAllPeople(@RequestParam Long idUser,
                                             @RequestParam(required = false) String searchText,
                                             @RequestHeader("Authorization") String authorization) {
        if (!userService.errorToken(authorization, idUser)) {
            return new ResponseEntity<>(new ResponseNotification(HttpStatus.UNAUTHORIZED.toString(),
                    Constants.TOKEN, Constants.TOKEN + " " + MessageResponse.NO_VALID.toLowerCase()),
                    HttpStatus.UNAUTHORIZED);
        }
        if (userService.checkAdmin(idUser) == null) {
            return new ResponseEntity<>(ResponseNotification.
                    responseMessage(Constants.IdCheck.ID_ADMIN, idUser), HttpStatus.UNAUTHORIZED);
        }
        List<User> users = userService.findAllByEmailOrUsername(searchText);
        List<UserDTO> userDTOList = userService.copyListDTO(users);
        return new ResponseEntity<>(userDTOList, HttpStatus.OK);
    }

    @GetMapping("/file")
    public ResponseEntity<?> getFile(@RequestParam Long idUser,
                                     @RequestParam String typeFile, HttpServletResponse response) throws IOException {
        response.setContentType("multipart/octet-stream");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + LocalDateTime.now());
        if ("txt".equalsIgnoreCase(typeFile)) {
            FileWriter fileWriter = new FileWriter("test.txt");
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write("test");
            bufferedWriter.close();
            fileWriter.close();
        }
        if ("csv".equalsIgnoreCase(typeFile)) {
            FileWriter fileWriter = new FileWriter("test.csv");
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write("test");
            bufferedWriter.close();
            fileWriter.close();
        }
        return new ResponseEntity<>(HttpStatus.OK);
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

        response.setHeader("Content-disposition", "attachment; filename=" + System.currentTimeMillis() + ".xlsx");
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

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
        List<Object[]> dataRows = testDataRepository.findAll()
                .stream().map(data -> new Object[]{
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
        }
        sheet.setDefaultColumnWidth(40);
        CellStyle dataCellStyle = workbook.createCellStyle();
        dataCellStyle.setAlignment(HorizontalAlignment.CENTER);
        dataCellStyle.setWrapText(true);
        int rowNum = 1;
        for (Object[] rowData : dataRows) {
            XSSFRow row = sheet.createRow(rowNum++);
            int columnNum = 0;
            for (Object field : rowData) {
                Cell cell = row.createCell(columnNum++);
                cell.setCellValue((String) field);
                cell.setCellStyle(dataCellStyle);
            }
        }

        response.setHeader("Content-disposition", "attachment; filename=" + System.currentTimeMillis() + ".xlsx");
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        OutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
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

    @GetMapping("/export")
    public void exportExcelNew(HttpServletResponse response) throws IOException {
        String fileName = "test-data.xlsx";
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        WriteCellStyle headStyle = new WriteCellStyle();
        WriteFont font = new WriteFont();
        font.setFontName("Arial"); // Đổi font chữ thành Arial
        headStyle.setWrapped(true);
        headStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex()); // Đổi màu header thành xám
        headStyle.setFillPatternType(FillPatternType.SOLID_FOREGROUND);
        headStyle.setWriteFont(font);
        // Tạo một đối tượng HorizontalCellStyleStrategy để định dạng kiểu chữ cho header
        HorizontalCellStyleStrategy styleStrategy = new HorizontalCellStyleStrategy(headStyle, headStyle);
        final double startTime = System.currentTimeMillis();
        List<TestData> testData = testDataRepository.findAll();
        final double elapsedTimeMillis = System.currentTimeMillis();
        Common.executionTime(startTime, elapsedTimeMillis);
        EasyExcel.write(response.getOutputStream(), TestData.class)
                .registerWriteHandler(styleStrategy)
                .sheet("Test Data")
                .doWrite(testData);
        final double elapsedTimeMillisEnd = System.currentTimeMillis();
        Common.executionTime(startTime, elapsedTimeMillisEnd);
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
