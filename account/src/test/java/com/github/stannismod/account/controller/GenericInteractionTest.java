package com.github.stannismod.account.controller;

import com.github.stannismod.account.entity.Account;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.testcontainers.containers.FixedHostPortGenericContainer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.transaction.Transactional;

@Testcontainers
@SpringBootTest
@Transactional
public class GenericInteractionTest extends Assertions {

    @Autowired
    private AccountController controller;

    private final TestRestTemplate restTemplate = new TestRestTemplate();

    private final String MARKET_HOST = "http://localhost:8080/market";

    private static final String BASE_COMPANY_NAME = "OOO Berezka";
    private static final int BASE_AMOUNT = 20000;

    private long testAccountId;

    @Container
    public static GenericContainer market
            = new FixedHostPortGenericContainer("market:1.0-SNAPSHOT")
                .withFixedExposedPort(8080, 8080)
                .withExposedPorts(8080);

    @BeforeEach
    public void setUp() {
        market.start();
        addCompany(BASE_COMPANY_NAME, 20 * BASE_AMOUNT, 40);
        testAccountId = controller.addAccount(BASE_AMOUNT).getId();
    }

    @AfterEach
    public void cleanUp() {
        market.stop();
    }

    @Test
    public void testRegister() {
        Account res = controller.getAccount(testAccountId);

        assertEquals(BASE_AMOUNT, res.getBalance());
        assertTrue(res.getStocks().isEmpty());
    }

    @Test
    public void testAddMoney() {
        Account res = controller.addMoney(testAccountId, BASE_AMOUNT);

        assertEquals(2 * BASE_AMOUNT, res.getBalance());
        assertTrue(res.getStocks().isEmpty());
    }

    @Test
    public void testBuyStocks() {
        Account res = controller.buyStocks(testAccountId, BASE_COMPANY_NAME, 20);

        assertEquals(BASE_AMOUNT - 20 * 40, res.getBalance());
        assertEquals(20, res.getStocks().get(BASE_COMPANY_NAME));
        assertEquals(20 * 40, controller.balance(testAccountId));
    }

    @Test
    public void testPriceChangeThenBuyStocks() {
        controller.buyStocks(testAccountId, BASE_COMPANY_NAME, 20);
        changePrice(BASE_COMPANY_NAME, 80);
        final int BOUGHT = 20;
        Account res = controller.buyStocks(testAccountId, BASE_COMPANY_NAME, BOUGHT);

        assertEquals(BASE_AMOUNT - BOUGHT * 40 - BOUGHT * 80, res.getBalance());
        assertEquals(40, res.getStocks().get(BASE_COMPANY_NAME));
        assertEquals(40 * 80, controller.balance(testAccountId));
    }

    @Test
    public void testBuySellWithAnotherPrice() {
        controller.buyStocks(testAccountId, BASE_COMPANY_NAME, 20);
        changePrice(BASE_COMPANY_NAME, 80);
        final int SOLD = 20;
        Account res = controller.sellStocks(testAccountId, BASE_COMPANY_NAME, SOLD);

        assertEquals(BASE_AMOUNT - SOLD * 40 + SOLD * 80, res.getBalance());
        assertEquals(0, res.getStocks().get(BASE_COMPANY_NAME));
        assertEquals(0, controller.balance(testAccountId));
    }

    private void addCompany(String name, int stocks, double price) {
        restTemplate.postForObject(MARKET_HOST + "/add-company?name={name}&stocks={stocks}&price={price}",
                null, String.class, name, stocks, price);
    }

    private void changePrice(String name, double price) {
        restTemplate.postForObject(MARKET_HOST + "/update-price/{name}?price={price}",
                null, String.class, name, price);
    }
}
