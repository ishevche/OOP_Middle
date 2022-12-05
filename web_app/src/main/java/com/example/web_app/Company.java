package com.example.web_app;


import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.*;


@Table
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter @Builder
public class Company {
    @Id @GeneratedValue
    private Long id;
    @Column(unique=true)
    private String domainName;
    private String name;
    private String twitter;
    private String facebook;
    private String logoLink;
    private String iconLink;
    private String employees;
    private String address;
}
