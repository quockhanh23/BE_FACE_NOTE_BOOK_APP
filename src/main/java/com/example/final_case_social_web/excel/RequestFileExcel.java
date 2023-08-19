package com.example.final_case_social_web.excel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestFileExcel implements Serializable {
    private byte[] content;
    private String fileName;
    private String fileExt;
}
