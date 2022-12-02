package com.example.web_app;

import lombok.Getter;

@Getter
public class ParserCompany{
    private final String domain;

    public ParserCompany(String domain) {
        this.domain = domain;
    }

    public Company getCompany() {
        Parser parser = new Parser(domain);
        parser.fetchInformation();
        return parser.buildCompany();
    }
}
