package com.example.web_app;

import com.example.web_app.chain.DataBaseCompany;
import com.example.web_app.chain.ParserCompany;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("api/company")
public class CompanyController {
    private final CompanyService companyService;

    @Autowired
    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @RequestMapping(path = "{domain}")
    public Company getCompany(@PathVariable("domain") String domain){
        DataBaseCompany dataBaseCompany = new DataBaseCompany(domain, companyService);
        ParserCompany parserCompany = new ParserCompany(domain);
        dataBaseCompany.addChild(parserCompany);
        Company result = dataBaseCompany.getCompany();
        addCompany(result);
        return result;
    }

    @PostMapping
    public void addCompany(@RequestBody Company company) {
        companyService.addCompany(company);
    }
}
