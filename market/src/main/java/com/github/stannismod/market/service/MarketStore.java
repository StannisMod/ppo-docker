package com.github.stannismod.market.service;

import com.github.stannismod.market.entity.Company;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MarketStore {

    private final CompanyRepository companyRepository;

    public MarketStore(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public Company addCompany(Company company) {
        if (companyRepository.findByName(company.getName()).isPresent()) {
            throw new RuntimeException("Company already exists");
        }
        return companyRepository.save(company);
    }

    public Company addStocks(String name, int stocks) {
        Company company = getCompany(name);
        company.addStocks(stocks);
        return companyRepository.save(company);
    }

    public Company getCompany(String name) {
        return companyRepository.findByName(name)
                                .orElseThrow(() -> new RuntimeException("Company not found"));
    }

    public Company buyStocks(String name, int stocks) {
        Company company = getCompany(name);
        company.buyStocks(stocks);
        return companyRepository.save(company);
    }

    public Company updateStockPrice(String name, double price) {
        Company company = getCompany(name);
        company.setPrice(price);
        return companyRepository.save(company);
    }

    public List<Company> getCompanies() {
        return companyRepository.findAll();
    }
}
