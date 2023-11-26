package com.example.final_case_social_web.controller;

import com.example.final_case_social_web.dto.UserDTO;
import com.example.final_case_social_web.model.User;
import com.example.final_case_social_web.service.UserService;
import com.example.final_case_social_web.service.pdf.CustomBase;
import com.example.final_case_social_web.service.pdf.CustomFontProvider;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@PropertySource("classpath:application.properties")
@CrossOrigin("*")
@RequestMapping("/api/pdfs")
@Slf4j
public class PdfController {

    @Autowired
    private UserService userService;

    @GetMapping("/export-pdf")
    public void exportPdf(HttpServletResponse response) throws IOException, DocumentException {
        response.setContentType(MediaType.APPLICATION_PDF_VALUE);
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=data.pdf; charset=utf-8");
        List<User> users = userService.findAllRoleUser();
        List<UserDTO> userDTOList = userService.copyListDTO(users);
        BaseFont unicode = BaseFont.createFont("src\\main\\resources\\fonts\\ARIALUNI.ttf",
                BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        Font font = new Font(unicode, 18);
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(response.getOutputStream())) {
            Document document = new Document();
            PdfWriter pdfWriter = PdfWriter.getInstance(document, byteArrayOutputStream);
            document.open();
            for (UserDTO userDTO : userDTOList) {
                Paragraph paragraph = new Paragraph();
                Chunk chunk = new Chunk(userDTO.getFullName());
                chunk.setFont(font);
                paragraph.add(chunk);
                document.add(paragraph);
            }
            document.close();
            pdfWriter.close();
            bufferedOutputStream.write(byteArrayOutputStream.toByteArray());
            bufferedOutputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/export-pdf2")
    public void exportPdfWithHTML(HttpServletResponse response) throws IOException, DocumentException {
        response.setContentType(MediaType.APPLICATION_PDF_VALUE);
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=data.pdf; charset=utf-8");
        List<User> users = userService.findAllRoleUser();
        List<UserDTO> userDTOList = userService.copyListDTO(users);
        Document document = new Document();
        PdfWriter pdfWriter = PdfWriter.getInstance(document, response.getOutputStream());
        // Lúc này phải open document
        document.open();
        Image image = Image.getInstance("src\\main\\resources\\fonts\\steam-logo.jpg");
        image.scaleToFit(100, 100);
        image.setAbsolutePosition(0, 0);
        pdfWriter.getDirectContent().addImage(image);
        StringBuilder html = new StringBuilder("<h1>Hello ข้อความ, world!</h1>");
        String table = "<table style=\"color: purple\" border=\"1px\" width=\"100%\">";
        String tableHeader = "<tr>\n" +
                "<th style=\"text-align: center\">&nbsp;STT&nbsp;</th>\n" +
                "<th style=\"text-align: center\">&nbsp;Tên đầy đủ&nbsp;</th>\n" +
                "<th style=\"text-align: center\">&nbsp;Email&nbsp;</th>\n" +
                "<th style=\"text-align: center\">&nbsp;Số điện thoại&nbsp;</th>\n" +
                "<th style=\"text-align: center\">&nbsp;Trạng thái&nbsp;</th>\n" +
                "</tr>";
        html.append(table).append(tableHeader);
        int num = 1;
        for (UserDTO userDTO : userDTOList) {
            String openTr = "<tr>";
            String stt = "<td>" + num + "</td>";
            String fullNameColumn = "<td>" + userDTO.getFullName() + "</td>";
            String emailColumn = "<td>" + userDTO.getEmail() + "</td>";
            String number = "<td>" + userDTO.getPhone() + "</td>";
            String statusColumn = "<td>" + userDTO.getStatus() + "</td>";
            String tableRowEnd = "</tr>";
            html.append(openTr).append(stt)
                    .append(fullNameColumn).append(emailColumn).append(number)
                    .append(statusColumn).append(tableRowEnd);
        }
        html.append("</table>");
        FontProvider fontProvider = new FontProvider() {
            @Override
            public boolean isRegistered(String fontName) {
                return false;
            }

            @Override
            public Font getFont(String fontName, String encoding, boolean embedded, float size, int style, BaseColor color) {
                BaseFont unicode = CustomBase.getFontArial();
                BaseColor color1 = new BaseColor(255, 244, 11);
                Font font = new Font(unicode, 10);
                font.setColor(color1);
                return font;
            }
        };
        XMLWorkerHelper.getInstance().parseXHtml(pdfWriter, document,
                new ByteArrayInputStream(html.toString().getBytes()), StandardCharsets.UTF_8, new CustomFontProvider());
        String test = "<h1>test color</h1><h1>test color 2</h1>";
        XMLWorkerHelper.getInstance().parseXHtml(pdfWriter, document,
                new ByteArrayInputStream(test.toString().getBytes()), StandardCharsets.UTF_8, fontProvider);
        document.close();
        pdfWriter.close();
        response.getOutputStream().flush();
    }
}
