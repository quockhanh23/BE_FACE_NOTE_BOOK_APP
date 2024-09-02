package com.example.final_case_social_web.component;

import com.example.final_case_social_web.model.User;
import com.example.final_case_social_web.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Component
public class QRCodeService {

    @Autowired
    private UserRepository userRepository;

    private String writeValueAsString() throws JsonProcessingException {
        List<User> userList = userRepository.findAll();
        if (CollectionUtils.isEmpty(userList)) return "";
        User user = userList.get(0);
        ObjectMapper objectMapper = new ObjectMapper();
        // Chuyển đổi đối tượng thành JSON
        return objectMapper.writeValueAsString(user);
    }

    public ByteArrayOutputStream generateQRCode() throws WriterException, IOException {
        String value = writeValueAsString();
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(value, BarcodeFormat.QR_CODE, 200, 200);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);

        return outputStream;
    }
}
