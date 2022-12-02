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
        Optional<Company> company = companyService.getCompany(domain);

        if (company.isPresent()){
            return company.get();
        }
        else {
            Parser parser = new Parser();
            return Company.builder()
                    .name(parser.getCompanyName(domain))
                    .facebook(parser.getCompanyFacebook(domain))
                    .twitter(parser.getCompanyTwitter(domain))
                    .domainName(domain)
                    .logoLink(parser.getCompanyLogo(domain))
                    .iconLink(parser.getCompanyIcon(domain))
                    .emplooyees(parser.getCompanyEmployees(domain))
                    .address(parser.getCompanyAddress(domain))
                    .build();
        }
    }

    @PostMapping
    public void addCompany(@RequestBody Company company) {
        companyService.addCompany(company);
    }
}