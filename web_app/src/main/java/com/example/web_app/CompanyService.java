package com.example.web_app;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@Service
public class CompanyService {
    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public Optional<Company> getCompany(String domain_name) {
        return companyRepository.findCompaniesByDomainName(domain_name);
    }

    public void addCompany(@RequestBody Company company){
        companyRepository.save(company);
    }
}
