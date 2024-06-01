package com.example.final_case_social_web.xml;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "dog")
@AllArgsConstructor
@NoArgsConstructor
public class Dog {
    private String dogName;
    private String color;
    @XmlElement
    public String getDogName() {
        return dogName;
    }

    public void setName(String dogName) {
        this.dogName = dogName;
    }
    @XmlElement
    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}

