package com.github.stannismod.account.entity;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Entity
public class Account {
    @Id
    @GeneratedValue
    private long id;

    private double balance;

    @ElementCollection
    @CollectionTable(name = "account_to_stocks",
            joinColumns = {@JoinColumn(name = "account_id", referencedColumnName = "id")})
    @MapKeyColumn(name = "stock_name")
    private final Map<String, Integer> stocks = new HashMap<>();

    public Account() {}

    public Account(double balance) {
        this.balance = balance;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public Map<String, Integer> getStocks() {
        return stocks;
    }

    public long getId() {
        return id;
    }

    public void addMoney(double money) {
        this.balance += money;
    }

    public void spendMoney(double money) {
        this.addMoney(-money);
    }

    public void setId(final long id) {
        this.id = id;
    }
}
