package com.example.web_app;

import java.util.Optional;

public class DataBaseCompany extends ParserCompany{
    private final CompanyService companyService;

    public DataBaseCompany(String domain, CompanyService companyService) {
        super(domain);
        this.companyService = companyService;
    }

    public Company getCompany() {
        Optional<Company> company = companyService.getCompany(getDomain());
        return company.orElseGet(super::getCompany);
    }
}
