package com.example.final_case_social_web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmailContent {
    public String email;
    public String username;
    public String password;
    public String title;
    public String content;
}
