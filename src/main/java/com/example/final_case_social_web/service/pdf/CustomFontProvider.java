package com.example.final_case_social_web.service.pdf;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontProvider;
import com.itextpdf.text.pdf.BaseFont;

import java.io.IOException;

public class CustomFontProvider implements FontProvider {

    @Override
    public boolean isRegistered(String fontName) {
        return false;
    }

    @Override
    public Font getFont(String fontName, String encoding, boolean embedded, float size, int style, BaseColor color) {
        BaseFont unicode;
        try {
            unicode = BaseFont.createFont("src\\main\\resources\\fonts\\ARIALUNI.ttf",
                    BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        } catch (DocumentException | IOException e) {
            throw new RuntimeException(e);
        }
        BaseColor color1 = new BaseColor(255, 0, 0);
        Font font = new Font(unicode, 18);
        font.setColor(color1);
        return font;
    }
}
