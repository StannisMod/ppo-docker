package com.github.stannismod.account.service;

import com.github.stannismod.account.controller.StockException;
import com.github.stannismod.account.entity.Account;
import org.springframework.stereotype.Service;

@Service
public class AccountStore {

    private final AccountRepository accountRepository;
    private final MarketStoreRemote marketStoreRemote;

    public AccountStore(AccountRepository accountRepository, MarketStoreRemote marketStoreRemote) {
        this.accountRepository = accountRepository;
        this.marketStoreRemote = marketStoreRemote;
    }

    public Account addAccount(Account account) {
        return accountRepository.save(account);
    }

    public Account addMoney(long accountId, double money) {
        Account account = getAccount(accountId);
        account.addMoney(money);
        return accountRepository.save(account);
    }

    public Account buyStocks(long accountId, String company, int stocks) {
        Account account = getAccount(accountId);
        double stockPrice = marketStoreRemote.getStockPrice(company);
        if (account.getBalance() < stockPrice * stocks) {
            throw new StockException("Not enough money");
        }
        marketStoreRemote.buyStocks(company, stocks);
        account.getStocks().put(company, account.getStocks().getOrDefault(company, 0) + stocks);
        account.spendMoney(stockPrice * stocks);
        return accountRepository.save(account);
    }

    public Account sellStocks(long accountId, String company, int stocks) {
        Account account = getAccount(accountId);
        int availableStocks = account.getStocks().getOrDefault(company, 0);
        if (availableStocks < stocks) {
            throw new StockException("Not enough stocks");
        }
        double stockPrice = marketStoreRemote.getStockPrice(company);
        marketStoreRemote.sellStocks(company, stocks);
        account.getStocks().put(company, account.getStocks().get(company) - stocks);
        account.addMoney(stocks * stockPrice);
        return accountRepository.save(account);
    }

    public Account getAccount(long accountId) {
        return accountRepository.findById(accountId)
                                .orElseThrow(() -> new StockException("Account not found"));
    }

    public double stockBalance(long accountId) {
        Account account = getAccount(accountId);
        double balance = 0;
        for (var stock : account.getStocks().entrySet()) {
            double stockPrice = marketStoreRemote.getStockPrice(stock.getKey());
            balance += stockPrice * stock.getValue();
        }
        return balance;
    }
}
