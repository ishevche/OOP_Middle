package com.example.web_app.chain;

import com.example.web_app.Company;
import com.example.web_app.CompanyService;

import java.util.Optional;

public class DataBaseCompany extends Container {
    public DataBaseCompany(String domain, CompanyService companyService) {
        super(domain, companyService);
    }

    public Company getCompany() {
        Optional<Company> company = getCompanyService().getCompany(getDomain());
        return company.orElseGet(child::getCompany);
    }
}
