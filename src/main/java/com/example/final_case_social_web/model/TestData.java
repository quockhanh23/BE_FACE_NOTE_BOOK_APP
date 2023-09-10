package com.example.final_case_social_web.model;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Random;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ExcelIgnore
    private Long id;
    @ExcelProperty("First Name")
    private String firstName = String.valueOf(new Random(5).toString());
    @ExcelProperty("Last Name")
    private String lastName = String.valueOf(new Random(5).toString());
    @ExcelProperty("Address")
    private String address = String.valueOf(new Random(5).toString());
    @ExcelProperty("Education")
    private String education = String.valueOf(new Random(5).toString());
    @ExcelProperty("Phone")
    private String phone = String.valueOf(new Random(5).toString());
    @ExcelProperty("Country")
    private String country = String.valueOf(new Random(5).toString());
    @ExcelProperty("Religion")
    private String religion = String.valueOf(new Random(5).toString());
    @ExcelProperty("License")
    private String license = String.valueOf(new Random(5).toString());
    @ExcelProperty("Vaccination")
    private String vaccination = String.valueOf(new Random(5).toString());
    @ExcelProperty("Passport")
    private String passport = String.valueOf(new Random(5).toString());

    public TestData(String firstName, String lastName, String address, String education, String phone, String country,
                    String religion, String license) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.education = education;
        this.phone = phone;
        this.country = country;
        this.religion = religion;
        this.license = license;
    }
}
