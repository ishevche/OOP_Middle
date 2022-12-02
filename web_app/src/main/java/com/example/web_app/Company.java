package com.example.web_app;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Table
@Entity
@NoArgsConstructor
@Getter @Setter @AllArgsConstructor
public class Company {
    @Id
    @GeneratedValue
    private Long id;
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
