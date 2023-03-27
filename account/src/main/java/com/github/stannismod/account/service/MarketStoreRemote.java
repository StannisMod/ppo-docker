package com.github.stannismod.account.service;

import com.github.stannismod.account.controller.StockException;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class MarketStoreRemote {

    private final String HOST = "http://localhost:8080/market";
    private final RestTemplate restTemplate;

    public MarketStoreRemote(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public void buyStocks(String name, int stocks) {
        restTemplate.postForObject(HOST + "/buy-stocks/{name}?stocks={stocks}", null, String.class, name, stocks);
    }

    public void sellStocks(String name, int stocks) {
        restTemplate.postForObject(HOST + "/sell-stocks/{name}?stocks={stocks}", null, String.class, name, stocks);
    }

    public double getStockPrice(String name) {
        Map<String, Object> response = restTemplate.getForObject(HOST + "/get-company/{name}", HashMap.class, name);
        if (response == null) {
            throw new StockException("NULL response for company lookup");
        }
        return (double) response.get("price");
    }
}
