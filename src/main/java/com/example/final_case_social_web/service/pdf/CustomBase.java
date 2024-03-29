package com.example.final_case_social_web.service.pdf;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BaseFont;

import java.io.IOException;

public class CustomBase {
    public static BaseFont getFontArial() {
        BaseFont unicode;
        try {
            unicode = BaseFont.createFont("src\\main\\resources\\fonts\\ARIALUNI.ttf",
                    BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        } catch (DocumentException | IOException e) {
            throw new RuntimeException(e);
        }
        return unicode;
    }
}
