package com.github.stannismod.account.controller;

import com.github.stannismod.account.entity.Account;
import com.github.stannismod.account.service.AccountStore;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountStore accountStore;

    public AccountController(AccountStore accountStore) {
        this.accountStore = accountStore;
    }

    @PostMapping("/add-account")
    public Account addAccount(@RequestParam double balance) {
        return accountStore.addAccount(new Account(balance));
    }

    @PostMapping("/add-money/{id}")
    public Account addMoney(@PathVariable long id, @RequestParam double money) {
        return accountStore.addMoney(id, money);
    }

    @GetMapping("/get-account/{id}")
    public Account getAccount(@PathVariable long id) {
        return accountStore.getAccount(id);
    }

    @PostMapping("/buy-stocks/{id}")
    public Account buyStocks(@PathVariable long id, @RequestParam String company, @RequestParam int stocks) {
        return accountStore.buyStocks(id, company, stocks);
    }

    @PostMapping("/sell-stocks/{id}")
    public Account sellStocks(@PathVariable long id, @RequestParam String company, @RequestParam int stocks) {
        return accountStore.sellStocks(id, company, stocks);
    }

    @GetMapping("/stock-balance/{id}")
    public double balance(@PathVariable long id) {
        return accountStore.stockBalance(id);
    }
}
