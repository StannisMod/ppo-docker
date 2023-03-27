package com.github.stannismod.market.entity;

import com.github.stannismod.market.controller.StockException;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Company {

    @Id
    @GeneratedValue
    private long id;

    private String name;
    private int stocks;
    private double price;

    public Company() {}

    public Company(String name, int stocks, double price) {
        this.name = name;
        this.stocks = stocks;
        this.price = price;
    }

    public void addStocks(int stocks) {
        this.stocks += stocks;
    }

    public void buyStocks(int stocks) {
        if (this.stocks < stocks) {
            throw new StockException("Not enough stocks");
        }
        this.stocks -= stocks;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStocks() {
        return stocks;
    }

    public void setStocks(int stocks) {
        this.stocks = stocks;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double stockPrice) {
        this.price = stockPrice;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
