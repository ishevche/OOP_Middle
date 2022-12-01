package com.example.web_app;

import lombok.*;

import javax.persistence.*;

@Table
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter @Builder
public class Company {
    @Id @GeneratedValue
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
