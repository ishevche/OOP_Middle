package com.example.web_app;

import com.fasterxml.jackson.databind.JsonSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

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
        
        return dataBaseCompany.getCompany();
    }

    @PostMapping
    public void addCompany(@RequestBody Company company) {
        companyService.addCompany(company);
    }
}
