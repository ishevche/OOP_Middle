package com.example.web_app;

import com.example.web_app.chain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("api/company")
public class CompanyController {
    private final CompanyService companyService;

    @Autowired
    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @RequestMapping(path = "/api/company/{domain}")
    public Company getCompany(@PathVariable("domain") String domain) throws IOException {
        DataBaseCompany dataBaseCompany = new DataBaseCompany(domain, companyService);
        ParserCompany parserCompany = new ParserCompany(domain);
        dataBaseCompany.addChild(parserCompany);
        return dataBaseCompany.getCompany();
    }

    @PostMapping
    public void addCompany(@RequestBody Company company) {
        companyService.addCompany(company);
    }
}
