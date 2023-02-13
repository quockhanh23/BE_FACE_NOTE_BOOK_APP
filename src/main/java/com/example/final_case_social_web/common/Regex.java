package com.example.final_case_social_web.common;

public class Regex {
    public static final String CHECK_SPECIAL_CHARACTERS = "^[a-zA-Z0-9]*$";
    public static final String CHECK_SPACE = "";
    public static final String CHECK_NUMBER_PHONE = "^[0-9]{9,11}$";
    public static final String CHECK_EMAIL = "";
    public static final String CHECK_USER_NAME = "^[a-z|A-Z|0-9]{3,16}$";
    public static final String CHECK_PASSWORD = "^.{3,10}+$";
    public static final String CHECK_LENGTH_POST = "^.{1,500}+$";
    public static final String CHECK_LENGTH_COMMENT = "^.{1,200}+$";
    public static final String CHECK_LENGTH_DESCRIPTION_USER = "^.{1,100}+$";
    public static final String CHECK_LENGTH_LIFE_EVENT = "^.{1,50}+$";
}
