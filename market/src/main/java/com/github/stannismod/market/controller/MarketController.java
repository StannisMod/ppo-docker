package com.github.stannismod.market.controller;

import com.github.stannismod.market.entity.Company;
import com.github.stannismod.market.service.MarketStore;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/market")
public class MarketController {

    private final MarketStore marketStore;

    public MarketController(MarketStore marketStore) {
        this.marketStore = marketStore;
    }

    @PostMapping("/add-company")
    public Company addCompany(@RequestParam String name, @RequestParam int stocks, @RequestParam double price) {
        return marketStore.addCompany(new Company(name, stocks, price));
    }

    @PostMapping("/sell-stocks/{name}")
    public Company addStocks(@PathVariable String name, @RequestParam int stocks) {
        return marketStore.addStocks(name, stocks);
    }

    @GetMapping("/get-companies")
    public List<Company> getCompany() {
        return marketStore.getCompanies();
    }

    @GetMapping("/get-company/{name}")
    public Company getCompany(@PathVariable String name) {
        return marketStore.getCompany(name);
    }

    @PostMapping("/buy-stocks/{name}")
    public Company buyStocks(@PathVariable String name, @RequestParam int stocks) {
        return marketStore.buyStocks(name, stocks);
    }

    @PostMapping("/update-price/{name}")
    public Company updateStockPrice(@PathVariable String name, @RequestParam double price) {
        return marketStore.updateStockPrice(name, price);
    }
}
