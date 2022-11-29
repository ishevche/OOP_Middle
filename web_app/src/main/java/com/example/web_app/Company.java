package com.example.web_app;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor
public class Company {
    private String domainName;
    private String name;
    private String twitter;
    private String facebook;
    private String logoLink;
    private String iconLink;
    private int emplooyeesMin;
    private int emplooyeesMax;
    private String address;
}
